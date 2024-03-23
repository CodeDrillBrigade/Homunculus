package org.cdb.labwitch.models.security

import org.cdb.labwitch.models.identifiers.EntityId

data class JWTRefreshClaims(
    val userId: EntityId,
)
