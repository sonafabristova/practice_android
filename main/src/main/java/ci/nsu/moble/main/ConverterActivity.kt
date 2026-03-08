package ci.nsu.mobile.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ci.nsu.mobile.main.navigation.ConverterRoutes
import ci.nsu.mobile.main.ui.screens.CelsiusToFahrenheitScreen
import ci.nsu.mobile.main.ui.screens.FahrenheitToCelsiusScreen
import ci.nsu.mobile.main.ui.theme.PracticeTheme

class ConverterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                ConverterScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Определяем текущий экран
    val currentScreen = when (currentRoute) {
        ConverterRoutes.CelsiusToFahrenheit.route -> ConverterRoutes.CelsiusToFahrenheit
        ConverterRoutes.FahrenheitToCelsius.route -> ConverterRoutes.FahrenheitToCelsius
        else -> ConverterRoutes.CelsiusToFahrenheit
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (currentScreen) {
                            ConverterRoutes.CelsiusToFahrenheit -> "Цельсий → Фаренгейт"
                            ConverterRoutes.FahrenheitToCelsius -> "Фаренгейт → Цельсий"
                        }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4CAF50),
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar {
                // Кнопка для Celsius → Fahrenheit
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "°C → °F") },
                    label = { Text("°C → °F") },
                    selected = currentScreen == ConverterRoutes.CelsiusToFahrenheit,
                    onClick = {
                        navController.navigate(ConverterRoutes.CelsiusToFahrenheit.route) {
                            popUpTo(ConverterRoutes.CelsiusToFahrenheit.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )

                // Кнопка для Fahrenheit → Celsius
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "°F → °C") },
                    label = { Text("°F → °C") },
                    selected = currentScreen == ConverterRoutes.FahrenheitToCelsius,
                    onClick = {
                        navController.navigate(ConverterRoutes.FahrenheitToCelsius.route) {
                            popUpTo(ConverterRoutes.CelsiusToFahrenheit.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ConverterRoutes.CelsiusToFahrenheit.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(ConverterRoutes.CelsiusToFahrenheit.route) {
                CelsiusToFahrenheitScreen()
            }
            composable(ConverterRoutes.FahrenheitToCelsius.route) {
                FahrenheitToCelsiusScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConverterScreenPreview() {
    PracticeTheme {
        ConverterScreen()
    }
}