package com.example.ecolush.ui.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecolush.data.model.HistoryItem
import com.example.ecolush.data.model.ProductPrice
import com.example.ecolush.data.repository.PriceRepository
import com.example.ecolush.data.repository.PriceRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException

data class PriceWithColor(
    val price: Double,
    val magasin: String,
    val commune: String,
    val color: Color
)

data class ProductComparison(
    val productName: String,
    val items: List<PriceWithColor>,
    val cheapestPrice: Double,
    val cheapestStore: String,
    val cheapestCommune: String
)

class PriceViewModel(
    private val repository: PriceRepository = PriceRepositoryImpl()
) : ViewModel() {

    private val _prices = MutableStateFlow<List<ProductPrice>>(emptyList())
    val prices = _prices.asStateFlow()

    private val _history = MutableStateFlow<List<HistoryItem>>(emptyList())
    val history = _history.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    val comparisonResults: StateFlow<List<ProductComparison>> = prices
        .combine(searchQuery) { currentPrices, query ->
            currentPrices.filter { it.name.contains(query, ignoreCase = true) }
                .groupBy { it.name.lowercase().trim() }
                .map { (key, records) ->
                    val minPrice = records.minOfOrNull { it.price } ?: 0.0
                    val maxPrice = records.maxOfOrNull { it.price } ?: 0.0
                    
                    val itemsWithColor = records.map { record ->
                        val color = when {
                            records.size <= 1 || minPrice == maxPrice -> Color(0xFF9E9E9E)
                            record.price == minPrice -> Color(0xFF4CAF50)
                            record.price == maxPrice -> Color(0xFFF44336)
                            else -> Color(0xFF9E9E9E)
                        }
                        PriceWithColor(
                            price = record.price,
                            magasin = record.storeName,
                            commune = record.commune,
                            color = color
                        )
                    }.sortedBy { it.price }
                    
                    val cheapest = itemsWithColor.firstOrNull()
                    
                    ProductComparison(
                        productName = records.first().name,
                        items = itemsWithColor,
                        cheapestPrice = cheapest?.price ?: 0.0,
                        cheapestStore = cheapest?.magasin ?: "",
                        cheapestCommune = cheapest?.commune ?: ""
                    )
                }.sortedBy { it.cheapestPrice }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        refreshPrices()
        refreshHistory()
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun refreshPrices() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _prices.value = repository.getAllPrices()
                _errorMessage.value = null
            } catch (e: IOException) {
                _errorMessage.value = "Erreur de connexion. Vérifiez votre internet."
            } catch (e: Exception) {
                _errorMessage.value = "Une erreur est survenue lors de la récupération des prix."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshHistory() {
        viewModelScope.launch {
            try {
                _history.value = repository.getHistory().sortedByDescending { it.createdAt }
            } catch (e: Exception) {
                // Pour l'historique on reste discret ou on logue
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun addPrice(productPrice: ProductPrice, imageBytes: ByteArray?) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.addPrice(productPrice, imageBytes)
                .onSuccess {
                    _errorMessage.value = null
                    refreshPrices()
                    refreshHistory()
                }
                .onFailure { e ->
                    _errorMessage.value = when(e) {
                        is IOException -> "Erreur réseau. Impossible d'ajouter le produit."
                        else -> "Erreur lors de l'ajout : ${e.localizedMessage}"
                    }
                }
            _isLoading.value = false
        }
    }

    fun updatePrice(productPrice: ProductPrice, imageBytes: ByteArray?) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.updatePrice(productPrice, imageBytes)
                .onSuccess {
                    _errorMessage.value = null
                    refreshPrices()
                    refreshHistory()
                }
                .onFailure { e ->
                    _errorMessage.value = "Erreur lors de la modification. Elle a peut-être été supprimée ou modifiée par ailleurs."
                }
            _isLoading.value = false
        }
    }

    fun deletePrice(productPrice: ProductPrice) {
        viewModelScope.launch {
            repository.deletePrice(productPrice)
                .onSuccess {
                    _errorMessage.value = null
                    refreshPrices()
                    refreshHistory()
                }
                .onFailure { 
                    _errorMessage.value = "Erreur lors de la suppression."
                }
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory().onSuccess {
                refreshHistory()
            }
        }
    }
}
