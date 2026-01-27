package com.model

interface UserRepository {
  suspend fun allUsers(): List<User>
  suspend fun usersByFrequency(frequency: Frequency): List<User>
  suspend fun userByName(name: String): User?
  suspend fun addUser(user: User)
  suspend fun removeUser(displayName: String): Boolean
}
