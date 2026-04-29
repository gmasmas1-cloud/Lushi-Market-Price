package com.example.ecolush

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecolush.data.model.ProductPrice
import com.example.ecolush.ui.screens.*
import com.example.ecolush.ui.theme.EcoLushTheme
import com.example.ecolush.ui.viewmodel.PriceViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // État pour le thème : null = système, true = sombre, false = clair
            var darkThemeSetting by remember { mutableStateOf<Boolean?>(null) }
            
            val useDarkTheme = darkThemeSetting ?: isSystemInDarkTheme()
            
            EcoLushTheme(darkTheme = useDarkTheme) {
                EcoLushApp(
                    currentTheme = darkThemeSetting,
                    onThemeChange = { darkThemeSetting = it }
                )
            }
        }
    }
}

@Composable
fun EcoLushApp(
    currentTheme: Boolean?,
    onThemeChange: (Boolean?) -> Unit
) {
    val navController = rememberNavController()
    val viewModel: PriceViewModel = viewModel()
    var editingProduct by remember { mutableStateOf<ProductPrice?>(null) }
    
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Gestion globale des erreurs via Snackbar avec durée indéfinie
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            val result = snackbarHostState.showSnackbar(
                message = it,
                actionLabel = "OK",
                duration = SnackbarDuration.Indefinite
            )
            // On efface l'erreur seulement si l'utilisateur appuie sur OK
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.clearError()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        NavHost(
            navController = navController, 
            startDestination = "splash",
            modifier = Modifier.padding(padding)
        ) {
            composable("splash") {
                SplashScreen(onNavigateToMain = {
                    navController.navigate("price_list") {
                        popUpTo("splash") { inclusive = true }
                    }
                })
            }
            composable("price_list") {
                PriceListScreen(
                    viewModel = viewModel,
                    onAddPrice = { 
                        editingProduct = null
                        navController.navigate("add_price") 
                    },
                    onEditPrice = { product ->
                        editingProduct = product
                        navController.navigate("add_price")
                    },
                    onCompare = { navController.navigate("comparator") },
                    onShowHistory = { navController.navigate("history") },
                    currentTheme = currentTheme,
                    onThemeChange = onThemeChange
                )
            }
            composable("add_price") {
                AddPriceScreen(
                    viewModel = viewModel,
                    productToEdit = editingProduct,
                    onBack = { navController.popBackStack() }
                )
            }
            composable("comparator") {
                ComparatorScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable("history") {
                HistoryScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
