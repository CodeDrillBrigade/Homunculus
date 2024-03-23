package org.cdb.labwitch.models.security

import org.cdb.labwitch.models.types.EntityId

data class JWTRefreshClaims(
    val userId: EntityId,
)
