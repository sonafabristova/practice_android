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
import ci.nsu.moble.main.navigation.Destination
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

    val incomingText = remember {
        (context as? Activity)?.intent?.getStringExtra("message_key") ?: "Пусто"
    }

    val navStackEntry by navController.currentBackStackEntryAsState()
    val currentPath = navStackEntry?.destination?.route
    val activeDestination = Destination.getDestinationByRoute(currentPath)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (activeDestination) {
                            Destination.Start -> "Главная страница"
                            Destination.First -> "Первая страница"
                            Destination.Second -> "Вторая страница"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        (context as? Activity)?.finish()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Назад",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2196F3),
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar {
                Destination.bottomItems.forEach { destination ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = when (destination) {
                                    Destination.Start -> Icons.Filled.Home
                                    Destination.First -> Icons.Filled.Star
                                    Destination.Second -> Icons.Filled.Person
                                },
                                contentDescription = destination.label
                            )
                        },
                        label = { Text(destination.label) },
                        selected = activeDestination == destination,
                        onClick = {
                            navController.navigate(destination.route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Destination.Start.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Destination.Start.route) {
                StartPage(
                    message = incomingText
                )
            }

            composable(Destination.First.route) {
                FirstPage()
            }

            composable(Destination.Second.route) {
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