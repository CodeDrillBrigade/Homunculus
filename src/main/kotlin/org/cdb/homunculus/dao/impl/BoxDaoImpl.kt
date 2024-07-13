package org.cdb.homunculus.dao.impl

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.cdb.homunculus.annotations.Index
import org.cdb.homunculus.components.DBClient
import org.cdb.homunculus.dao.BoxDao
import org.cdb.homunculus.models.Box
import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.models.identifiers.HierarchicalId
import java.util.regex.Pattern

class BoxDaoImpl(client: DBClient) : BoxDao(client) {
	override fun getByMaterials(
		materialId: EntityId,
		includeDeleted: Boolean,
	): Flow<Box> =
		find(
			Filters.and(
				listOfNotNull(
					eq(Box::material.name, materialId),
					Filters.exists(Box::deletionDate.name, false).takeIf { !includeDeleted },
				),
			),
		)

	override fun getByMaterials(
		materials: Set<EntityId>,
		includeDeleted: Boolean,
	): Flow<Box> =
		find(
			Filters.and(
				listOfNotNull(
					Filters.`in`(Box::material.name, materials.map { it.id }),
					Filters.exists(Box::deletionDate.name, false).takeIf { !includeDeleted },
				),
			),
		)

	@Index(name = "by_batch_number", property = "batchNumber", unique = false)
	override fun getByBatchNumber(
		query: String,
		includeDeleted: Boolean,
	): Flow<Box> =
		find(
			Filters.and(
				listOfNotNull(
					Filters.regex(Box::batchNumber.name, Pattern.compile("^$query.*")),
					Filters.exists(Box::deletionDate.name, false).takeIf { !includeDeleted },
				),
			),
		)

	@Index(name = "by_shelf_id", property = "position", unique = false)
	override fun getByPosition(
		shelfId: HierarchicalId,
		includeDeleted: Boolean,
	): Flow<Box> =
		find(
			Filters.and(
				listOfNotNull(
					eq(Box::position.name, shelfId),
					Filters.exists(Box::deletionDate.name, false).takeIf { !includeDeleted },
				),
			),
		)
}
