package ci.nsu.moble.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.ui.theme.PracticeTheme

class FirstActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PracticeTheme {
                FirstScreen(
                    onNavigateToSecond = { message ->

                        val intent = Intent(this, SecondActivity::class.java).apply {
                            putExtra("EXTRA_MESSAGE", message)
                        }
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun FirstScreen(
    onNavigateToSecond: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Введите сообщение") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (text.isNotBlank()) {
                    onNavigateToSecond(text)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Перейти на второй экран")
        }
    }
}