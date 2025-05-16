package viewModel.io

import androidx.compose.ui.unit.Dp
import io.JsonConverter
import java.awt.FileDialog
import java.io.File
import model.Graph
import model.abstractGraph.AbstractVertex
import viewModel.graph.GraphViewModel

class JSONViewModel() {

    fun loadJson(fileDialog: FileDialog): Pair<Graph, Map<AbstractVertex, Pair<Dp?, Dp?>?>> {
        if (fileDialog.file == null) throw IllegalArgumentException("File wasn't selected")

        val fileToOpen = File(fileDialog.directory, fileDialog.file)
        val convertor = JsonConverter()
        try {
            val graphModel = convertor.loadJson(fileToOpen.readText())
            return graphModel
        } catch (e: Exception) {
            throw IllegalStateException(e.message)
        }
    }

    fun storeJson(graph: GraphViewModel, fileDialog: FileDialog) {
        if (fileDialog.file == null) throw IllegalArgumentException("File wasn't selected")

        val fileToSave = File(fileDialog.directory, fileDialog.file)
        val convertor = JsonConverter()
        try {
            fileToSave.writeText(convertor.saveJson(graph))
        } catch (e: Exception) {
            throw IllegalStateException("Conversation error: ${e.message}")
        }
    }
}
