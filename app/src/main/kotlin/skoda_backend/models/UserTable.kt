package skoda_backend.models

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Table.Dual.entityId
import org.jetbrains.exposed.sql.Table.Dual.long
import org.jetbrains.exposed.sql.Table.Dual.nullable
import org.jetbrains.exposed.sql.Table.Dual.text
import org.jetbrains.exposed.sql.Table.Dual.uniqueIndex
import org.jetbrains.exposed.sql.Table.Dual.uuid
import org.jetbrains.exposed.sql.Table.Dual.varchar
import java.util.UUID
import org.jetbrains.exposed.sql.javatime.timestamp

object Users : IdTable<String>("Users") {
    override val id = varchar("userId", 255).entityId()
    val email = varchar("email", 255).uniqueIndex()
    val passwordHash = varchar("passwordHash", 255)
    val firstName = varchar("firstName", 255).nullable()
    val lastName = varchar("lastName", 255).nullable()
    val phoneNumber = varchar("phoneNumber", 20).nullable()
    val address = text("address").nullable()
    val createdAt = long("createdAt")
    val updatedAt = long("updatedAt").nullable()
}
