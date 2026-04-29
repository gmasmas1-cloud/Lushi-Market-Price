package com.example.ecolush.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HistoryItem(
    val id: Long? = null,
    @SerialName("product_name")
    val productName: String,
    val action: String, // "ADD", "DELETE", "UPDATE"
    val details: String,
    @SerialName("created_at")
    val createdAt: String? = null
)
