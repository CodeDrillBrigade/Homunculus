package org.cdb.labwitch.logic.impl

import kotlinx.coroutines.flow.Flow
import org.cdb.labwitch.dao.StorageDao
import org.cdb.labwitch.logic.StorageLogic
import org.cdb.labwitch.models.StorageRoom

class StorageLogicImpl(
    private val storageDao: StorageDao,
) : StorageLogic {
    override suspend fun create(storage: StorageRoom) = storageDao.save(storage)

    override fun getAll(): Flow<StorageRoom> = storageDao.get()
}
