package skoda_backend.Utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

// Utility function to validate email format
fun isValidEmail(email: String): Boolean {
    return Regex("^[A-Za-z0-9+_.-]+@(.+)$").matches(email)
}

fun generateToken(userId: String?, secret: String): String {
    val expiration = Date(System.currentTimeMillis() + 600_000) // Token valid for 10 minutes
    return JWT.create()
            .withClaim("userId", userId?.toString())
            .withExpiresAt(expiration)
            .sign(Algorithm.HMAC256(secret))
}