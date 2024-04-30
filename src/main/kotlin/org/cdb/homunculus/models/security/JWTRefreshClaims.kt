package org.cdb.homunculus.models.security

import org.cdb.homunculus.models.identifiers.EntityId

data class JWTRefreshClaims(
	val userId: EntityId,
)
