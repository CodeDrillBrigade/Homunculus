package org.cdb.labwitch.logic.impl

import org.cdb.labwitch.dao.MaterialDao
import org.cdb.labwitch.logic.MaterialLogic
import org.cdb.labwitch.models.Material
import org.cdb.labwitch.models.MaterialCreationData
import org.cdb.labwitch.models.types.EntityId

class MaterialLogicImpl(private val materialDao: MaterialDao) : MaterialLogic {
    override suspend fun addMaterial(creationData: MaterialCreationData): Material {
        val newMaterial =
            Material(
                id = EntityId.generate(),
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
    }

    override suspend fun get(materialId: EntityId): Material {
        return requireNotNull(materialDao.get(materialId)) {
            "Material not found"
        }
    }
}
