package org.cdb.homunculus.models.security

import org.cdb.homunculus.models.identifiers.EntityId
import org.cdb.homunculus.utils.DynamicBitArray

/**
 * This class represents all the data stored in the JWT.
 */
data class JWTClaims(
	val userId: EntityId,
	val permissions: DynamicBitArray,
)
