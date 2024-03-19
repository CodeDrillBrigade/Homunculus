package org.cdb.labwitch.models.security

/**
 * This class represents all the data stored in the JWT.
 */
data class JWTClaims(
    val userId: String,
)
