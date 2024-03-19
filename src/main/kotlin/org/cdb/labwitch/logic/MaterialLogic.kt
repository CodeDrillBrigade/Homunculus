package org.cdb.labwitch.logic

import org.cdb.labwitch.models.Material
import org.cdb.labwitch.models.MaterialCreationData

interface MaterialLogic {
    /**Adds a new [Material]
     * @param creationData the data necessary for creating a new Material
     * @return the [Material] created*/
    suspend fun addMaterial(creationData: MaterialCreationData): Material

    /**Retrieves a [Material]
     * @param materialId the ID of the material to find
     * @return the [Material] requested*/
    suspend fun get(materialId: String): Material
} // fine interface
