import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import view.GreetingView
import java.awt.Dimension

@Composable
@Preview
fun App() {
    MaterialTheme {
        GreetingView()
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(width = 1400.dp, height = 1050.dp),
        title = "SE TOP"
    ) {
        window.minimumSize = Dimension(800, 600)
        App()
    }
}


