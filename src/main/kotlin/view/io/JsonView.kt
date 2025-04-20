package view.io

import io.JsonConverter
import viewModel.GraphViewModel
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
            throw IllegalStateException("Conversation error: ${e.message?.removePrefix("Exception")}. Cannot save the graph")
        }
    }

    fun loadFromJson(): GraphViewModel? {
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
        } catch (e: IllegalStateException) {
            throw IllegalStateException("Cannot open the graph: ${e.message}")
        }
    }
}