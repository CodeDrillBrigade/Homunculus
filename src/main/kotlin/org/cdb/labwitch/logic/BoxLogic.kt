package org.cdb.labwitch.logic

import org.cdb.labwitch.models.Box
import org.cdb.labwitch.models.BoxCreationData
import org.cdb.labwitch.models.types.EntityId

interface BoxLogic {
    /**
     * Adds a new [Box] to the system
     *
     * @param [boxCreationData] the data necessary to create a new box
     * @return the newly created [Box]
     * */
    suspend fun addBox(boxCreationData: BoxCreationData): Box

    /**
     * Retrieves a [Box] via ID
     *
     * @param [boxId] the [EntityId] of the box to search
     * @return the searched [Box]
     * */
    suspend fun get(boxId: EntityId): Box
}
