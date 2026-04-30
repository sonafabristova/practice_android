package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import ci.nsu.moble.main.ui.screens.LoginScreen

// Временная тема
import androidx.compose.material3.MaterialTheme

@Composable
fun TempTheme(content: @Composable () -> Unit) {
    MaterialTheme(content = content)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TempTheme {  // ← используем временную тему
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    var isLoggedIn by remember { mutableStateOf(false) }
                    var showRegister by remember { mutableStateOf(false) }

                    when {
                        showRegister -> {
                            Text(
                                text = "Экран регистрации",
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        isLoggedIn -> {
                            Text(
                                text = "Экран пользователей",
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        else -> {
                            LoginScreen(
                                onLoginSuccess = { isLoggedIn = true },
                                onNavigateToRegister = { showRegister = true }
                            )
                        }
                    }
                }
            }
        }
    }
}