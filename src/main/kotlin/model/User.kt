package com.model

import kotlinx.serialization.Serializable

enum class Frequency {
  Daily, Weekly, Biweekly, Monthly, Quarterly, Yearly
}

@Serializable
data class User(
  val displayName: String,
  val imageUrl: String,
  val rate: Double,
  val frequency: Frequency
)
