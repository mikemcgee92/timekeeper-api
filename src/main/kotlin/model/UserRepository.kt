package com.model

interface UserRepository {
  fun allUsers(): List<User>
  fun usersByFrequency(frequency: Frequency): List<User>
  fun userByName(name: String): User?
  fun addUser(user: User)
  fun removeUser(displayName: String): Boolean
}
