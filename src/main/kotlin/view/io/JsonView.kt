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
    fun storeToJson(graph: GraphViewModel) {
        val frame = Frame()
        val fileDialog = FileDialog(frame, "Save your graph in JSON:", FileDialog.SAVE).also {
            it.setFilenameFilter(FilenameFilter { dir, file ->
                return@FilenameFilter file.endsWith(".json")
            })
            it.setFile("*.json")
            it.isVisible = true
        }

        if (fileDialog.file == null) { // file isn't selected
            frame.dispose()
            return
        }

        val fileToSave = File(fileDialog.directory, fileDialog.file)
        val convertor = JsonConverter()
        try {
            fileToSave.writeText(convertor.saveJson(graph))
            frame.dispose()
        } catch (e: Exception) {
            frame.dispose()
            throw IllegalStateException("Conversation error: ${e.message}")
        }
    }

    fun loadFromJson(): Pair<Graph, Map<AbstractVertex, Pair<Dp?, Dp?>?>>? {
        val frame = Frame()
        val fileDialog = FileDialog(frame, "Open your JSON file:", FileDialog.LOAD).also {
            it.setFile("*.json")
            it.isVisible = true
        }

        if (fileDialog.file == null) { // file isn't selected
            frame.dispose()
            return null
        }

        val fileToOpen = File(fileDialog.directory, fileDialog.file)
        val convertor = JsonConverter()
        try {
            val graphModel = convertor.loadJson(fileToOpen.readText())
            frame.dispose()
            return graphModel
        } catch (e: Exception) {
            frame.dispose()
            throw IllegalStateException(e.message)
        }
    }
}