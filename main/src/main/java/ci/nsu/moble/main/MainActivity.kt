package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.moble.main.auth.TokenManager
import ci.nsu.moble.main.ui.screens.*
import ci.nsu.moble.main.ui.theme.PracticeTheme
import ci.nsu.moble.main.viewmodel.AuthViewModel
import ci.nsu.moble.main.viewmodel.AuthViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tokenManager = TokenManager(applicationContext)
        val factory = AuthViewModelFactory(tokenManager)

        setContent {
            PracticeTheme {
                val viewModel: AuthViewModel = viewModel(factory = factory)
                var isLoggedIn by remember { mutableStateOf(false) }
                var showRegister by remember { mutableStateOf(false) }

                when {
                    showRegister -> {
                        RegisterScreen(
                            onRegisterSuccess = {
                                isLoggedIn = true
                                showRegister = false
                            },
                            onBackToLogin = { showRegister = false }
                        )
                    }
                    isLoggedIn -> {
                        UsersScreen(
                            onLogout = {
                                viewModel.loadUsers() // перезагружаем при возврате
                                isLoggedIn = false
                            }
                        )
                    }
                    else -> {
                        LoginScreen(
                            onLoginSuccess = {
                                viewModel.loadUsers()
                                isLoggedIn = true
                            },
                            onNavigateToRegister = { showRegister = true }
                        )
                    }
                }
            }
        }
    }
}