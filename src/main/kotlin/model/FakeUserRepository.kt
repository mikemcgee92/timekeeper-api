package com.model

class FakeUserRepository : UserRepository {
  private val users = mutableListOf(
    User(
      "Bobson Dugnutt",
      "http://www.example.com/image.jpg",
      19.50,
      Frequency.Weekly
    ),
    User(
      "Mike Truk",
      "http://www.sample.com/johnqsample.png",
      20.26,
      Frequency.Biweekly
    ),
    User(
      "Peter Parker",
      "http://www.marvel.com/spiderman.jpg",
      19.80,
      Frequency.Monthly
    ),
    User(
      "The Chosen One",
      "http://www.internet.com/image.jpg",
      100.00,
      Frequency.Daily
    )
  )

  override fun allUsers(): List<User> = users

  override fun usersByFrequency(frequency: Frequency) = users.filter {
    it.frequency == frequency
  }

  override fun userByName(name: String) = users.find {
    it.displayName.equals(name, ignoreCase = true)
  }

  override fun addUser(user: User) {
    if (userByName(user.displayName) != null) {
      throw IllegalStateException("Cannot duplicate user names")
    }
    users.add(user)
  }

  override fun removeUser(displayName: String): Boolean {
    return users.removeIf { it.displayName == displayName }
  }
}
