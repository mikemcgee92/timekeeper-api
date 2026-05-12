package com.model

import kotlinx.serialization.Serializable

@Serializable
data class Invoice(
    val timeBlocks: String, // TODO: change this to TimeBlock object
    val totalHours: Double,
    val totalAmount: Double
)