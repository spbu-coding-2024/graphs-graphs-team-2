package view.io

import androidx.compose.ui.unit.Dp
import io.JsonConverter
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.io.FilenameFilter
import model.Graph
import model.abstractGraph.AbstractVertex
import viewModel.graph.GraphViewModel

class JsonView {
    val frame = Frame()
    val fileDialog = FileDialog(frame)

    fun storeToJson(graph: GraphViewModel, onDismissRequest: () -> Unit) {
        fileDialog.apply {
            title = "Save your graph in JSON:"
            mode = FileDialog.SAVE
            filenameFilter = FilenameFilter { dir, file ->
                return@FilenameFilter file.endsWith(".json")
            }
            isVisible = true
        }

        if (fileDialog.file == null) { // file wasn't selected
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
        fileDialog.apply {
            title = "Open your JSON file:"
            mode = FileDialog.LOAD

            filenameFilter = FilenameFilter { dir, file ->
                return@FilenameFilter file.endsWith(".json")
            }
            isVisible = true
        }

        if (fileDialog.file == null) { // file wasn't selected
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
