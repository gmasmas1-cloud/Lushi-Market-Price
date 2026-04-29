package com.example.ecolush.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ecolush.data.model.ProductPrice
import com.example.ecolush.ui.viewmodel.PriceViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PriceListScreen(
    viewModel: PriceViewModel,
    onAddPrice: () -> Unit,
    onEditPrice: (ProductPrice) -> Unit,
    onCompare: () -> Unit,
    onShowHistory: () -> Unit,
    currentTheme: Boolean?,
    onThemeChange: (Boolean?) -> Unit
) {
    val prices by viewModel.prices.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    
    var selectedCategory by remember { mutableStateOf("Tous") }
    val categories = listOf("Tous", "Alimentation", "Électronique", "Divers")

    var showDeleteDialog by remember { mutableStateOf(false) }
    var priceToDelete by remember { mutableStateOf<ProductPrice?>(null) }

    val filteredPrices = prices.filter { 
        (selectedCategory == "Tous" || it.category == selectedCategory) &&
        it.name.contains(searchQuery, ignoreCase = true)
    }

    if (showDeleteDialog && priceToDelete != null) {
        AlertDialog(
            onDismissRequest = { 
                showDeleteDialog = false
                priceToDelete = null
            },
            title = { Text("Confirmer la suppression") },
            text = { Text("Voulez-vous supprimer le relevé pour \"${priceToDelete?.name}\" ?") },
            confirmButton = {
                TextButton(onClick = {
                    priceToDelete?.let { viewModel.deletePrice(it) }
                    showDeleteDialog = false
                    priceToDelete = null
                }) { Text("Supprimer", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { 
                    showDeleteDialog = false
                    priceToDelete = null
                }) { Text("Annuler") }
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPrice,
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter", tint = Color.White)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(bottom = padding.calculateBottomPadding())
        ) {
            // --- HEADER ---
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.95f)
                                )
                            )
                        )
                        .padding(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                modifier = Modifier.size(40.dp),
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.padding(8.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Bonjour !", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.bodySmall)
                                Text("Lushi Market Price", color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            }
                        }
                        
                        Row {
                            IconButton(onClick = {
                                onThemeChange(if (currentTheme == null) true else if (currentTheme) false else null)
                            }) {
                                val icon = if (currentTheme == null) Icons.Default.SettingsBrightness else if (currentTheme) Icons.Default.DarkMode else Icons.Default.LightMode
                                Icon(icon, null, tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                            IconButton(onClick = onShowHistory) {
                                Icon(Icons.Default.History, null, tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }

            // --- SEARCH BAR (STICKY) ---
            stickyHeader {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.9f), shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                        .padding(start = 24.dp, end = 24.dp, bottom = 16.dp, top = 4.dp)
                ) {
                    Surface(
                        modifier = Modifier.fillMaxWidth().height(48.dp).shadow(4.dp, RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { viewModel.onSearchQueryChange(it) },
                            placeholder = { Text("Rechercher...", color = Color.Gray) },
                            leadingIcon = { Icon(Icons.Default.Search, null, tint = MaterialTheme.colorScheme.primary) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent, focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent),
                            singleLine = true
                        )
                    }
                }
            }

            // --- CATEGORIES ---
            item {
                Text("Catégories", modifier = Modifier.padding(24.dp, 16.dp, 24.dp, 8.dp), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                LazyRow(modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(horizontal = 24.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(categories) { category ->
                        val isSelected = selectedCategory == category
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { selectedCategory = category }) {
                            Surface(modifier = Modifier.size(48.dp), shape = RoundedCornerShape(12.dp), color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface, shadowElevation = 1.dp) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(when(category){"Alimentation"->Icons.Default.ShoppingCart "Électronique"->Icons.Default.Devices "Divers"->Icons.Default.Category else->Icons.Default.AllInclusive}, null, tint = if (isSelected) Color.White else MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                }
                            }
                            Text(category, style = MaterialTheme.typography.labelSmall, color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray)
                        }
                    }
                }
            }

            // --- LOGIQUE D'AFFICHAGE (RESTE VISIBLE MÊME SI ON RE-TENTE) ---
            if (isLoading && prices.isEmpty() && errorMessage == null) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().fillParentMaxHeight(0.5f), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            } else if (errorMessage != null && prices.isEmpty()) {
                item {
                    Column(modifier = Modifier.fillMaxWidth().fillParentMaxHeight(0.5f).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        // Montre une progression discrète si on est en train de réessayer
                        if (isLoading) LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp))
                        
                        Icon(Icons.Default.CloudOff, null, modifier = Modifier.size(64.dp), tint = Color.Gray)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Pas de connexion internet", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                        Text("Vérifiez votre connexion et réessayez pour voir les prix.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { viewModel.refreshPrices() }, 
                            enabled = !isLoading, 
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Refresh, null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (isLoading) "Tentative..." else "Réessayer")
                        }
                    }
                }
            } else {
                item {
                    Row(modifier = Modifier.fillMaxWidth().padding(24.dp, 16.dp, 24.dp, 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Meilleures offres", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        TextButton(onClick = onCompare) { Text("Comparer", style = MaterialTheme.typography.labelSmall) }
                    }
                }
                if (filteredPrices.isEmpty() && !isLoading) {
                    item { Box(modifier = Modifier.fillMaxWidth().fillParentMaxHeight(0.3f), contentAlignment = Alignment.Center) { Text("Aucun résultat", color = Color.Gray) } }
                } else {
                    items(filteredPrices) { price ->
                        Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
                            ModernProductCard(price, onEdit = { onEditPrice(price) }, onDelete = { priceToDelete = price; showDeleteDialog = true })
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun ModernProductCard(price: ProductPrice, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(2.dp)) {
        Column {
            Box(modifier = Modifier.height(180.dp).fillMaxWidth()) {
                AsyncImage(model = price.imageUrl, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                Surface(modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp), shape = RoundedCornerShape(12.dp), color = Color(0xFF22C55E)) {
                    Text("${price.price} ${price.currency}", modifier = Modifier.padding(12.dp, 6.dp), color = Color.White, fontWeight = FontWeight.ExtraBold)
                }
                Row(modifier = Modifier.align(Alignment.TopEnd).padding(12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(modifier = Modifier.size(32.dp).clickable { onEdit() }, shape = CircleShape, color = Color.White.copy(0.9f)) { Icon(Icons.Default.Edit, null, modifier = Modifier.padding(8.dp), tint = Color.DarkGray) }
                    Surface(modifier = Modifier.size(32.dp).clickable { onDelete() }, shape = CircleShape, color = Color.White.copy(0.9f)) { Icon(Icons.Default.Delete, null, modifier = Modifier.padding(8.dp), tint = Color.Red.copy(0.7f)) }
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(price.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Store, null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("${price.storeName} • ${price.location.substringAfterLast(",")}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
        }
    }
}
