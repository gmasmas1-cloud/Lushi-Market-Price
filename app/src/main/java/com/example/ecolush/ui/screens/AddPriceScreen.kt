package com.example.ecolush.ui.screens

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.ecolush.data.model.LocationData
import com.example.ecolush.data.model.ProductPrice
import com.example.ecolush.ui.viewmodel.PriceViewModel
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPriceScreen(
    viewModel: PriceViewModel,
    productToEdit: ProductPrice? = null,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf(productToEdit?.name ?: "") }
    var price by remember { mutableStateOf(productToEdit?.price?.toString() ?: "") }
    var storeName by remember { mutableStateOf(productToEdit?.storeName ?: "") }
    var currency by remember { mutableStateOf(productToEdit?.currency ?: "CDF") }
    var category by remember { mutableStateOf(productToEdit?.category ?: "Alimentation") }
    
    // Localisation initialisation
    val initialCommune = LocationData.communes.find { it.name == productToEdit?.commune } ?: LocationData.communes[0]
    var selectedCommune by remember { mutableStateOf(initialCommune) }
    var selectedQuartier by remember { mutableStateOf(productToEdit?.quartier ?: initialCommune.quartiers[0]) }
    var addressDetail by remember { 
        mutableStateOf(
            if (productToEdit != null && productToEdit.location != "${productToEdit.commune}, ${productToEdit.quartier}") {
                productToEdit.location.substringBefore(", ${productToEdit.quartier}")
            } else ""
        ) 
    }
    
    var communeExpanded by remember { mutableStateOf(false) }
    var quartierExpanded by remember { mutableStateOf(false) }
    var categoryExpanded by remember { mutableStateOf(false) }

    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    
    val categories = listOf("Alimentation", "Électronique", "Divers")
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                ImageDecoder.decodeBitmap(source)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (productToEdit == null) "Ajouter un Relevé" else "Modifier le Relevé") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nom du produit") },
                placeholder = { Text("ex: Sac de Farine 25kg") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )

            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = !categoryExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Catégorie") },
                    leadingIcon = { Icon(Icons.Default.Category, contentDescription = null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )
                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false }
                ) {
                    categories.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = {
                                category = cat
                                categoryExpanded = false
                            }
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Prix") },
                    leadingIcon = { Icon(Icons.Default.Payments, contentDescription = null) },
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilterChip(
                    selected = currency == "CDF",
                    onClick = { currency = "CDF" },
                    label = { Text("CDF") }
                )
                Spacer(modifier = Modifier.width(4.dp))
                FilterChip(
                    selected = currency == "USD",
                    onClick = { currency = "USD" },
                    label = { Text("USD") }
                )
            }

            OutlinedTextField(
                value = storeName,
                onValueChange = { storeName = it },
                label = { Text("Nom du Magasin / Marché") },
                leadingIcon = { Icon(Icons.Default.Store, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )

            HorizontalDivider()
            Text("Localisation", style = MaterialTheme.typography.titleSmall)

            ExposedDropdownMenuBox(
                expanded = communeExpanded,
                onExpandedChange = { communeExpanded = !communeExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedCommune.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Commune") },
                    leadingIcon = { Icon(Icons.Default.LocationCity, contentDescription = null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = communeExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )
                ExposedDropdownMenu(
                    expanded = communeExpanded,
                    onDismissRequest = { communeExpanded = false }
                ) {
                    LocationData.communes.forEach { commune ->
                        DropdownMenuItem(
                            text = { Text(commune.name) },
                            onClick = {
                                selectedCommune = commune
                                selectedQuartier = commune.quartiers[0]
                                communeExpanded = false
                            }
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = quartierExpanded,
                onExpandedChange = { quartierExpanded = !quartierExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedQuartier,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Quartier") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = quartierExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
                )
                ExposedDropdownMenu(
                    expanded = quartierExpanded,
                    onDismissRequest = { quartierExpanded = false }
                ) {
                    selectedCommune.quartiers.forEach { quartier ->
                        DropdownMenuItem(
                            text = { Text(quartier) },
                            onClick = {
                                selectedQuartier = quartier
                                quartierExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = addressDetail,
                onValueChange = { addressDetail = it },
                label = { Text("Adresse ou repère (optionnel)") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )

            Button(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(if (productToEdit?.imageUrl != null) "Changer la photo" else "Choisir une photo")
            }

            bitmap?.let {
                Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium) {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth().height(200.dp)
                    )
                }
            }

            Button(
                onClick = {
                    val priceDouble = price.toDoubleOrNull() ?: 0.0
                    val imageBytes = bitmap?.let {
                        val stream = ByteArrayOutputStream()
                        it.compress(Bitmap.CompressFormat.JPEG, 80, stream)
                        stream.toByteArray()
                    }
                    
                    val updatedProduct = ProductPrice(
                        id = productToEdit?.id,
                        name = name,
                        category = category,
                        price = priceDouble,
                        currency = currency,
                        storeName = storeName,
                        commune = selectedCommune.name,
                        quartier = selectedQuartier,
                        location = if (addressDetail.isBlank()) "${selectedCommune.name}, ${selectedQuartier}" else "${addressDetail}, ${selectedQuartier}",
                        imageUrl = productToEdit?.imageUrl
                    )

                    if (productToEdit == null) {
                        viewModel.addPrice(updatedProduct, imageBytes)
                    } else {
                        viewModel.updatePrice(updatedProduct, imageBytes)
                    }
                    onBack()
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E7D32),
                    contentColor = Color.White
                ),
                enabled = name.isNotBlank() && price.isNotBlank() && storeName.isNotBlank()
            ) {
                Text(if (productToEdit == null) "Enregistrer le relevé" else "Mettre à jour", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
