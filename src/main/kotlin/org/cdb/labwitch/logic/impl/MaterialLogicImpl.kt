package org.cdb.labwitch.logic.impl

import org.cdb.labwitch.dao.MaterialDao
import org.cdb.labwitch.logic.MaterialLogic
import org.cdb.labwitch.models.Material
import org.cdb.labwitch.models.MaterialCreationData
import java.util.UUID

class MaterialLogicImpl(private val materialDao: MaterialDao) : MaterialLogic {
    override suspend fun addMaterial(creationData: MaterialCreationData): Material {
        val newMaterial =
            Material(
                id = UUID.randomUUID().toString(),
                name = creationData.name,
                boxDefinition = creationData.boxDefinition,
                brand = creationData.brand,
                tags = creationData.tags,
                description = creationData.description,
                noteList = creationData.noteList,
            )
        val createId =
            requireNotNull(materialDao.save(newMaterial)) {
                "Error during creation of new material"
            }
        return checkNotNull(materialDao.get(createId)) {
            "Error during retrieval of material"
        }
    } // fine metodo addMaterial()

    override suspend fun get(materialId: String): Material {
        return requireNotNull(materialDao.get(materialId)) {
            "Material not found"
        }
    } // fine metodo get()
} // fine MaterialLogicImpl
