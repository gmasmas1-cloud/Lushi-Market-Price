package com.example.ecolush.data.model

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class ProductPrice(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val id: Long? = null,
    val name: String,
    val category: String = "Alimentation",
    val price: Double,
    val currency: String,
    @SerialName("store_name")
    val storeName: String,
    val commune: String,
    val quartier: String,
    val location: String, // Keeping it for compatibility or detailed address
    @SerialName("image_url")
    val imageUrl: String? = null,
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    @SerialName("created_at")
    val createdAt: String? = null
)
