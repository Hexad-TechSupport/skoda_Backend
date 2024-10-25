package skoda_backend.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

private const val secret = "skoda-secret-key" // read from secrets
private const val issuer = "skoda-issuer"
private const val audience = "skoda-audience"
private const val realm = "skoda-realm"

fun isValidEmail(email: String): Boolean {
    return Regex("^[A-Za-z0-9+_.-]+@(.+)$").matches(email)
}

val verifier = JWT.require(Algorithm.HMAC256(secret))
        .withAudience(audience)
        .withIssuer(issuer)
        .build()

fun generateToken(userId: String): String {
    return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("userId", userId)
            .sign(Algorithm.HMAC256(secret))
}