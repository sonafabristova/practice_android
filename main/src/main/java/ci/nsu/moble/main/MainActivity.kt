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
import ci.nsu.moble.main.ui.screens.RegisterScreen
import ci.nsu.moble.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    var isLoggedIn by remember { mutableStateOf(false) }
                    var showRegister by remember { mutableStateOf(false) }

                    when {
                        showRegister -> {
                            RegisterScreen(
                                onRegisterSuccess = {
                                    showRegister = false
                                },
                                onBackToLogin = { showRegister = false }
                            )
                        }
                        isLoggedIn -> {
                            Text(
                                text = "Экран пользователей (будет в этапе 9)",
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