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
import viewModel.io.JSONViewModel
import viewModel.graph.GraphViewModel

@Composable
fun storeToJson(viewModel: JSONViewModel, graph: GraphViewModel, onDismissRequest: () -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
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
            viewModel.storeToJson(graph, fileDialog)
            onDismissRequest()
        } catch (e: Exception) {
            if (e is IllegalArgumentException) onDismissRequest() else throw e
        }
    }
}

@Composable
fun loadFromJson(viewModel: JSONViewModel, navigator: Navigator, onDismissRequest: () -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
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
            val graphModel = viewModel.loadFromJson(fileDialog)
            navigator.push(GraphScreen(graphModel.first, graphModel.second))
        } catch (e: Exception) {
            if (e is IllegalArgumentException) onDismissRequest() else throw e
        }
    }
}
