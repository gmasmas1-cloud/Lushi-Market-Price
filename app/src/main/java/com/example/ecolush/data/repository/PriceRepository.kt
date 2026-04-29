package com.example.ecolush.data.repository

import android.util.Log
import com.example.ecolush.data.model.HistoryItem
import com.example.ecolush.data.model.ProductPrice
import com.example.ecolush.data.remote.supabase
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import io.ktor.http.ContentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface PriceRepository {
    suspend fun getAllPrices(): List<ProductPrice>
    suspend fun addPrice(productPrice: ProductPrice, imageBytes: ByteArray?): Result<Unit>
    suspend fun updatePrice(productPrice: ProductPrice, imageBytes: ByteArray?): Result<Unit>
    suspend fun deletePrice(productPrice: ProductPrice): Result<Unit>
    suspend fun getHistory(): List<HistoryItem>
    suspend fun addHistoryEntry(item: HistoryItem): Result<Unit>
    suspend fun clearHistory(): Result<Unit>
}

class PriceRepositoryImpl : PriceRepository {
    override suspend fun getAllPrices(): List<ProductPrice> = withContext(Dispatchers.IO) {
        try {
            val result = supabase.from("comparatif_prix").select().decodeList<ProductPrice>()
            Log.d("PriceRepository", "Fetched ${result.size} prices from database")
            result
        } catch (e: Exception) {
            Log.e("PriceRepository", "Error fetching prices: ${e.message}", e)
            throw e
        }
    }

    override suspend fun addPrice(productPrice: ProductPrice, imageBytes: ByteArray?): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            try {
                var finalImageUrl = productPrice.imageUrl
                
                if (imageBytes != null) {
                    val fileName = "${System.currentTimeMillis()}.jpg"
                    val bucket = supabase.storage.from("product-images")
                    bucket.upload(fileName, imageBytes) {
                        contentType = ContentType.Image.JPEG
                    }
                    finalImageUrl = bucket.publicUrl(fileName)
                }
                
                val priceToSave = productPrice.copy(
                    imageUrl = finalImageUrl,
                    createdAt = null
                )
                
                supabase.from("comparatif_prix").insert(priceToSave)
                
                // Record History
                addHistoryEntry(HistoryItem(
                    productName = productPrice.name,
                    action = "AJOUT",
                    details = "Prix: ${productPrice.price} ${productPrice.currency}, Magasin: ${productPrice.storeName}"
                ))
            } catch (e: Exception) {
                Log.e("PriceRepository", "Supabase Error: ${e.message}", e)
                throw e
            }
        }
    }

    override suspend fun updatePrice(productPrice: ProductPrice, imageBytes: ByteArray?): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            var finalImageUrl = productPrice.imageUrl
            
            if (imageBytes != null) {
                val fileName = "${System.currentTimeMillis()}.jpg"
                val bucket = supabase.storage.from("product-images")
                bucket.upload(fileName, imageBytes) {
                    contentType = ContentType.Image.JPEG
                }
                finalImageUrl = bucket.publicUrl(fileName)
            }

            val priceToUpdate = productPrice.copy(imageUrl = finalImageUrl)

            supabase.from("comparatif_prix").update(priceToUpdate) {
                filter {
                    eq("id", productPrice.id ?: 0L)
                }
            }
            // Record History
            addHistoryEntry(HistoryItem(
                productName = productPrice.name,
                action = "MODIFICATION",
                details = "Nouveau prix: ${productPrice.price} ${productPrice.currency}, Magasin: ${productPrice.storeName}"
            ))
        }
    }

    override suspend fun deletePrice(productPrice: ProductPrice): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            supabase.from("comparatif_prix").delete {
                filter {
                    eq("id", productPrice.id ?: 0L)
                }
            }
            // Record History
            addHistoryEntry(HistoryItem(
                productName = productPrice.name,
                action = "SUPPRESSION",
                details = "Était à ${productPrice.price} ${productPrice.currency} chez ${productPrice.storeName}"
            ))
        }
    }

    override suspend fun getHistory(): List<HistoryItem> = withContext(Dispatchers.IO) {
        try {
            supabase.from("historique").select().decodeList<HistoryItem>()
        } catch (e: Exception) {
            Log.e("PriceRepository", "Error fetching history: ${e.message}")
            emptyList()
        }
    }

    override suspend fun addHistoryEntry(item: HistoryItem): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            supabase.from("historique").insert(item)
        }
    }

    override suspend fun clearHistory(): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            supabase.from("historique").delete {
                filter {
                    neq("id", 0L) // Delete all records where id != 0 (everything)
                }
            }
        }
    }
}
