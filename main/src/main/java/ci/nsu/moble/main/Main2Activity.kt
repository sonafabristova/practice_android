package ci.nsu.moble.main

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ci.nsu.moble.main.navigation.NavRoutes

import ci.nsu.moble.main.ui.pages.StartPage
import ci.nsu.moble.main.ui.pages.FirstPage
import ci.nsu.moble.main.ui.pages.SecondPage
import ci.nsu.moble.main.ui.theme.PracticeTheme

class Main2Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Main2Screen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Main2Screen() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // получаем переданные данные
    val receivedMessage = remember {
        (context as? Activity)?.intent?.getStringExtra("user_message") ?: "Нет данных"
    }

    // получаем текущий путь
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val currentScreen = NavRoutes.fromRoute(currentRoute)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (currentScreen) {
                            NavRoutes.Start -> "Старт"
                            NavRoutes.First -> "Первый"
                            NavRoutes.Second -> "Второй"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            // возвращаемся в MainActivity
                            (context as? Activity)?.finish()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Назад",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EE),
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavRoutes.bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = when (screen) {
                                    NavRoutes.Start -> Icons.Filled.Home
                                    NavRoutes.First -> Icons.Filled.Star
                                    NavRoutes.Second -> Icons.Filled.Person
                                },
                                contentDescription = screen.title
                            )
                        },
                        label = { Text(screen.title) },
                        selected = currentScreen == screen,
                        onClick = {
                            navController.navigate(screen.route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Start.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(NavRoutes.Start.route) {
                StartPage(
                    message = receivedMessage
                )
            }

            composable(NavRoutes.First.route) {
                FirstPage()
            }

            composable(NavRoutes.Second.route) {
                SecondPage()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Main2ScreenPreview() {
    PracticeTheme {
        Main2Screen()
    }
}