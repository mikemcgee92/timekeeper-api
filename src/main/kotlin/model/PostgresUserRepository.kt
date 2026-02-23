package com.model

import com.db.UserDAO
import com.db.UserTable
import com.db.daoToModel
import com.db.suspendTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere

class PostgresUserRepository : UserRepository {
  override suspend fun allUsers(): List<User> = suspendTransaction {
    UserDAO.all().map(::daoToModel)
  }

  override suspend fun usersByFrequency(frequency: Frequency): List<User> = suspendTransaction {
    UserDAO
      .find { (UserTable.frequency eq frequency.toString()) }
      .map(::daoToModel)
  }

  override suspend fun userByName(displayName: String): User? = suspendTransaction {
    UserDAO
      .find { (UserTable.displayName eq displayName)}
      .limit(1)
      .map(::daoToModel)
      .firstOrNull()
  }

  override suspend fun addUser(user: User): Unit = suspendTransaction {
    UserDAO.new {
      displayName = user.displayName
      imageUrl = user.imageUrl
      rate = user.rate
      frequency = user.frequency.toString()
    }
  }

  override suspend fun removeUser(displayName: String): Boolean = suspendTransaction {
    val rowsDeleted = UserTable.deleteWhere {
      UserTable.displayName eq displayName
    }
    rowsDeleted == 1
  }
}
