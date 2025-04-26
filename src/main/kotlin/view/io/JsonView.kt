package view.io

import androidx.compose.ui.unit.Dp
import io.JsonConverter
import model.Graph
import model.abstractGraph.AbstractVertex
import viewModel.graph.GraphViewModel
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.io.FilenameFilter

class JsonView {
    fun storeToJson(graph: GraphViewModel, onDismissRequest: () -> Unit) {
        val frame = Frame()
        val fileDialog = FileDialog(frame, "Save your graph in JSON:", FileDialog.SAVE).apply {
            this.setFilenameFilter(FilenameFilter { dir, file ->
                return@FilenameFilter file.endsWith(".json")
            })
            this.setFile("*.json")
            this.isVisible = true
        }

        if (fileDialog.file == null) { // file isn't selected
            frame.dispose()
            onDismissRequest()
            return
        }

        val fileToSave = File(fileDialog.directory, fileDialog.file)
        val convertor = JsonConverter()
        try {
            fileToSave.writeText(convertor.saveJson(graph))
            frame.dispose()
            onDismissRequest()
        } catch (e: Exception) {
            frame.dispose()
            throw IllegalStateException("Conversation error: ${e.message}")
        }
    }

    fun loadFromJson(): Pair<Graph, Map<AbstractVertex, Pair<Dp?, Dp?>?>>? {
        val frame = Frame()
        val fileDialog = FileDialog(frame, "Open your JSON file:", FileDialog.LOAD).apply {
            this.setFilenameFilter(FilenameFilter { dir, file ->
                return@FilenameFilter file.endsWith(".json")
            })
            this.setFile("*.json")
            this.isVisible = true
        }

        if (fileDialog.file == null) { // file isn't selected
            frame.dispose()
            return null
        }

        val fileToOpen = File(fileDialog.directory, fileDialog.file)
        val convertor = JsonConverter()
        frame.dispose()
        try {
            val graphModel = convertor.loadJson(fileToOpen.readText())
            return graphModel
        } catch (e: Exception) {
            throw IllegalStateException(e.message)
        }
    }
}