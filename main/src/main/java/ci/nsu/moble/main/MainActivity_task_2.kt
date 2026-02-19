package ci.nsu.moble.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text


import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.ui.theme.PracticeTheme

import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height


class MainActivity_task_2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
                    ColorInputScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}

fun ColorInputScreen(modifier: Modifier= Modifier)
{
    var color_name by remember { mutableStateOf("") }
    TextField(
        value = color_name,
        onValueChange = {newText -> color_name = newText},// это для обработки ввода текста
        label = {Text(text = "Введите цвет")},
        modifier = Modifier.fillMaxWidth()//можно убрать

    )
    Spacer(modifier = Modifier.height(10.dp))

    Button(
        onClick = {/*gnoejrngoeii*/ },
        modifier = Modifier.fillMaxWidth()
    ){
        Text("Окрасить в цвет")
    }
}
@Preview(showBackground = true)
@Composable

fun ColorInputScreenPreview()
{
    PracticeTheme {
        ColorInputScreen()
    }
}
//fun GreetingPreview() {
//    PracticeTheme {
//        Greeting("Android")
//    }
//}