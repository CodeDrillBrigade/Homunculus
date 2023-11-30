package org.cdb.labwitch.dao

import org.cdb.labwitch.components.DBClient
import org.cdb.labwitch.models.Material

abstract class MaterialDao(client: DBClient) : GenericDao<Material>(client)
{
	abstract suspend fun get(id:String) : Material?
	abstract suspend fun getByName(name:String) : Material?

} // fine classe