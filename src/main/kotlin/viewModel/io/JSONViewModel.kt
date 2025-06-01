package viewModel.io

import androidx.compose.ui.unit.Dp
import io.JSONConverter
import java.awt.FileDialog
import java.io.File
import model.Graph
import model.abstractGraph.AbstractVertex
import viewModel.graph.GraphViewModel

class JSONViewModel() {

    fun loadJSON(fileDialog: FileDialog): Pair<Graph, Map<AbstractVertex, Pair<Dp?, Dp?>?>> {
        if (fileDialog.file == null) throw IllegalArgumentException("File wasn't selected")

        val fileToOpen = File(fileDialog.directory, fileDialog.file)
        val convertor = JSONConverter()
        try {
            val graphModel = convertor.fromJSON(fileToOpen.readText())
            return graphModel
        } catch (e: Exception) {
            throw IllegalStateException(e.message)
        }
    }

    fun storeJSON(graph: GraphViewModel, fileDialog: FileDialog) {
        if (fileDialog.file == null) throw IllegalArgumentException("File wasn't selected")

        val fileToSave = File(fileDialog.directory, fileDialog.file)
        val convertor = JSONConverter()
        try {
            fileToSave.writeText(convertor.toJSON(graph))
        } catch (e: Exception) {
            throw IllegalStateException("Conversation error: ${e.message}")
        }
    }
}
