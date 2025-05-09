package view.io

import GraphScreen
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import java.awt.FileDialog
import java.awt.Frame
import java.io.FilenameFilter
import kotlin.apply
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import viewModel.GreetingScreenViewModel
import viewModel.MainScreenViewModel
import viewModel.graph.GraphViewModel
import viewModel.io.JSONViewModel

@Composable
fun storeToJson(
    screenViewModel: MainScreenViewModel,
    graph: GraphViewModel,
    onDismissRequest: () -> Unit,
) {
    CoroutineScope(Dispatchers.IO).launch {
        val viewModel = JSONViewModel()
        val frame = Frame()
        val fileDialog =
            FileDialog(frame).apply {
                title = "Save your graph in JSON:"
                mode = FileDialog.SAVE
                filenameFilter = FilenameFilter { dir, file ->
                    return@FilenameFilter file.endsWith(".json")
                }
                isVisible = true
            }

        frame.dispose()
        try {
            viewModel.storeJson(graph, fileDialog)
            onDismissRequest()
        } catch (e: Exception) {
            if (e is IllegalArgumentException) onDismissRequest()
            else
                screenViewModel.apply {
                    errorMessage = e.message ?: "Cannot store to JSON-file: Unknown error"
                    showErrorDialog = true
                    onDismissRequest()
                }
        }
    }
}

@Composable
fun loadFromJson(
    screenViewModel: GreetingScreenViewModel,
    navigator: Navigator,
    onDismissRequest: () -> Unit,
) {
    CoroutineScope(Dispatchers.IO).launch {
        val viewModel = JSONViewModel()
        val frame = Frame()
        val fileDialog =
            FileDialog(frame).apply {
                title = "Open your JSON file:"
                mode = FileDialog.LOAD

                filenameFilter = FilenameFilter { dir, file ->
                    return@FilenameFilter file.endsWith(".json")
                }
                isVisible = true
            }

        frame.dispose()
        try {
            val graphModel = viewModel.loadJson(fileDialog)
            navigator.push(GraphScreen(graphModel.first, graphModel.second))
        } catch (e: Exception) {
            if (e is IllegalArgumentException) onDismissRequest()
            else
                screenViewModel.apply {
                    errorMessage = e.message ?: "Cannot load JSON-file: Unknown error"
                    showErrorDialog = true
                    onDismissRequest()
                }
        }
    }
}
