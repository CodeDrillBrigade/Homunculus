package org.cdb.labwitch.models.security

import org.cdb.labwitch.models.identifiers.EntityId
import org.cdb.labwitch.utils.DynamicBitArray

/**
 * This class represents all the data stored in the JWT.
 */
data class JWTClaims(
	val userId: EntityId,
	val permissions: DynamicBitArray,
)
