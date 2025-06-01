import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import java.awt.Dimension
import model.Graph
import model.abstractGraph.AbstractVertex
import view.GreetingView
import view.MainScreen
import viewModel.GreetingScreenViewModel
import viewModel.MainScreenViewModel

object WelcomeScreen : Screen {
    @Composable override fun Content() = GreetingView(GreetingScreenViewModel())
}

data class GraphScreen(val graph: Graph, val placement: Map<AbstractVertex, Pair<Dp?, Dp?>?>) :
    Screen {
    @Composable override fun Content() = MainScreen(MainScreenViewModel(graph, placement))
}

@Composable
@Preview
fun App() {
    MaterialTheme { Navigator(WelcomeScreen) }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(width = 1200.dp, height = 900.dp),
        title = "SE TOP",
    ) {
        window.minimumSize = Dimension(800, 600)
        App()
    }
}
