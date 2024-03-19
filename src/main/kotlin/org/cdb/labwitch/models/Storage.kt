package org.cdb.labwitch.models

data class Storage(
    val id: Int,
    val room: String?,
    val cabinet: String,
    val shelf: String,
    val closet: String?,
    val box: String?,
)
