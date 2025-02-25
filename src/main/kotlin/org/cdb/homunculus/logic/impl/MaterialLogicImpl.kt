package org.cdb.homunculus.logic.impl

import com.github.benmanes.caffeine.cache.Caffeine
import com.mongodb.client.model.Sorts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.future.await
import kotlinx.coroutines.future.future
import kotlinx.coroutines.launch
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.cdb.homunculus.dao.BoxDao
import org.cdb.homunculus.dao.BoxDefinitionDao
import org.cdb.homunculus.dao.MaterialDao
import org.cdb.homunculus.exceptions.NotFoundException
import org.cdb.homunculus.logic.MaterialLogic
import org.cdb.homunculus.models.Material
import org.cdb.homunculus.models.embed.BoxDefinition
import org.cdb.homunculus.models.embed.BoxUnit
import org.cdb.homunculus.models.filters.Filter
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.identifiers.Identifier
import org.cdb.homunculus.utils.StringNormalizer
import org.cdb.homunculus.utils.exist
import java.io.ByteArrayOutputStream
import java.util.Date
import java.util.concurrent.TimeUnit

class MaterialLogicImpl(
	private val materialDao: MaterialDao,
	private val boxDefinitionDao: BoxDefinitionDao,
	private val boxDao: BoxDao,
) : MaterialLogic {
	private val materialLogicScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
	private val reportKey = "MATERIAL_REPORT_KEY"
	private val materialReportCache =
		Caffeine.newBuilder()
			.expireAfterWrite(1, TimeUnit.DAYS)
			.buildAsync<String, ByteArray>()

	override suspend fun create(material: Material): Identifier =
		materialDao.save(material.copy(creationDate = Date(), normalizedName = StringNormalizer.normalize(material.name))).also {
			invalidateReport()
		}

	override suspend fun get(materialId: EntityId): Material =
		materialDao.getById(materialId) ?: throw NotFoundException("Material $materialId not found")

	override fun getAll(): Flow<Material> = materialDao.get().filter { it.deletionDate == null }

	override fun findByFuzzyName(
		query: String,
		limit: Int?,
	): Flow<Material> =
		materialDao.getByFuzzyName(
			StringNormalizer.normalize(query),
			includeDeleted = false,
			limit = limit,
			skip = null,
		)

	override fun getByReferenceCode(referenceCode: String): Flow<Material> =
		materialDao.getByReferenceCode(referenceCode, includeDeleted = false)

	override suspend fun delete(id: EntityId): EntityId {
		val material = materialDao.getById(id) ?: throw NotFoundException("Material $id not found")
		return materialDao.update(
			material.copy(
				deletionDate = Date(),
			),
		)?.id?.also {
			invalidateReport()
		} ?: throw IllegalStateException("Cannot delete the material with id $id")
	}

	override suspend fun modify(material: Material) {
		val currentMaterial = exist({ materialDao.getById(material.id) }) { "Material ${material.id} not found" }
		materialDao.update(
			currentMaterial.copy(
				name = material.name,
				brand = material.brand,
				referenceCode = material.referenceCode ?: currentMaterial.referenceCode,
				description = material.description ?: currentMaterial.description,
				tags = material.tags,
				deletionDate = material.deletionDate ?: currentMaterial.deletionDate,
				normalizedName = StringNormalizer.normalize(material.name),
			),
		).also {
			invalidateReport()
		}
	}

	private fun search(
		query: String,
		tagIds: Set<EntityId>?,
		limit: Int?,
	): Flow<Material> =
		flow {
			if (query.isBlank()) {
				emitAll(
					materialDao.getSorted(Sorts.ascending(Material::normalizedName.name)).filter {
						it.deletionDate == null
					},
				)
			} else {
				emitAll(materialDao.getByFuzzyName(query, includeDeleted = false, limit = null, skip = null))
				emitAll(materialDao.searchByReferenceCode(query, includeDeleted = false, limit = null, skip = null))
				emitAll(materialDao.getByBrand(query, includeDeleted = false, limit = null, skip = null))
			}
		}.filter {
			tagIds == null || it.tags.intersect(tagIds).isNotEmpty()
		}.let {
			if (limit != null) {
				it.take(limit)
			} else {
				it
			}
		}

	override suspend fun searchIds(
		query: String,
		tagIds: Set<EntityId>?,
		limit: Int?,
	): Set<EntityId> = search(query, tagIds, limit).fold(mutableSetOf()) { acc, it -> acc.apply { add(it.id) } }

	override suspend fun searchNames(
		query: String,
		tagIds: Set<EntityId>?,
		limit: Int?,
	): Set<String> = search(query, tagIds, limit).fold(mutableSetOf()) { acc, it -> acc.apply { add(it.name.text) } }

	override fun getByIds(ids: Set<EntityId>): Flow<Material> = materialDao.getByIds(ids)

	override fun getLastCreated(limit: Int): Flow<Material> = materialDao.getLastCreated(limit)

	override fun filter(filter: Filter): Flow<Material> = materialDao.find(filter.toBson()).filter { it.deletionDate == null }

	override fun invalidateReport() {
		materialLogicScope.launch {
			materialReportCache.getIfPresent(reportKey)?.await()?.also {
				materialReportCache.synchronous().invalidate(reportKey)
			}
		}
	}

	override suspend fun createMaterialsReport(): ByteArray =
		materialLogicScope.async {
			materialReportCache.get(reportKey) { _, _ ->
				future {
					val boxDefinitions = mutableMapOf<EntityId, BoxDefinition>()
					XSSFWorkbook().also { workbook ->
						val workSheet = workbook.createSheet()
						workSheet.setHeader(0)
						materialDao.get().filter {
							it.deletionDate == null
						}.collectIndexed { index, material ->
							val rowIndex = index + 1
							workSheet.createRow(rowIndex).let { row ->
								row.set(0, material.name.text)
								row.set(1, material.brand)
								row.set(2, material.referenceCode ?: "UNKNOWN")
								val boxDefinition =
									boxDefinitions[material.boxDefinition]
										?: boxDefinitionDao.getById(material.boxDefinition)?.also {
											boxDefinitions[material.boxDefinition] = it
										}
								var unit = boxDefinition?.boxUnit
								var total =
									boxDao.getByMaterial(material.id, includeDeleted = false).filter {
										it.deletionDate == null && it.quantity.quantity > 0
									}.map { it.quantity.quantity }.toList().sum()
								row.set(6, "$total total pieces")
								var firstBag = true
								do {
									when {
										firstBag && unit != null && unit.metric == null -> {
											firstBag = false
											val totalUnit = unit.boxUnit.quantityRecursive()
											val boxes = total / totalUnit
											row.set(3, "$boxes full box of ${unit.boxUnit?.quantity} ${if (unit.boxUnit?.metric != null) "pieces" else "bags"}")
											total -= (boxes * totalUnit)
										}
										!firstBag && unit != null && unit.metric == null -> {
											val totalUnit = unit.boxUnit.quantityRecursive()
											val boxes = total / totalUnit
											row.set(4, "$boxes full bags of ${unit.boxUnit?.quantity} pieces")
											total -= (boxes * totalUnit)
										}
										else -> {
											row.set(5, "$total single pieces")
											total = 0
										}
									}
									unit = unit?.boxUnit
								} while (unit != null)
							}
						}
					}.let { workbook ->
						val byteArrayOutputStream = ByteArrayOutputStream()
						workbook.write(byteArrayOutputStream)
						byteArrayOutputStream.toByteArray().also {
							byteArrayOutputStream.close()
							workbook.close()
						}
					}
				}
			}.await()
		}.await()

	private fun BoxUnit?.quantityRecursive(quantity: Int = 1): Int =
		if (this == null) {
			quantity
		} else {
			boxUnit.quantityRecursive(quantity * this.quantity)
		}

	private fun XSSFSheet.setHeader(rowIndex: Int) =
		createRow(rowIndex).let { row ->
			listOf("Name", "Brand", "Reference Code", "Boxes", "Bags", "Units", "Total").forEachIndexed { index, s ->
				row.createCell(index).setCellValue(s)
			}
		}

	private fun XSSFRow.set(
		col: Int,
		value: String,
	) = createCell(col).setCellValue(value)
}
