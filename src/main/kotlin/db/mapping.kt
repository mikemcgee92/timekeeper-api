package com.db 

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import com.model.Frequency
import com.model.User
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object UserTable : IntIdTable("user") {
  val displayName = varchar("displayName", 50)
  val imageUrl = varchar("imageUrl", 50)
  val rate = double("rate")
  val frequency = varchar("frequency", 50)
}

class UserDAO(id: EntityID<Int>) : IntEntity(id) {
  companion object : IntEntityClass<UserDAO>(UserTable)

  var displayName by UserTable.displayName
  var imageUrl by UserTable.imageUrl
  var rate by UserTable.rate
  var frequency by UserTable.frequency
}

// suspendTransaction() takes a block of code and runs it within a database transaction, through the IO Dispatcher. This is designed to offload blocking jobs of work onto a thread pool
suspend fun <T> suspendTransaction(block: Transaction.() -> T): T =
  newSuspendedTransaction(Dispatchers.IO, statement = block)
  
// daoToModel() transforms an instance of the UserDAO type to the User object.
fun daoToModel(dao: UserDAO) = User(
  dao.displayName,
  dao.imageUrl,
  dao.rate,
  Frequency.valueOf(dao.frequency)
)
