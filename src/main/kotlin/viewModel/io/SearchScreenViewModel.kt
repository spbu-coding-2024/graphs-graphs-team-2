package viewModel.io

import androidx.compose.ui.unit.Dp
import io.SQLiteConverter
import io.SQLiteExposed.SQLiteEXP
import java.io.File
import java.nio.file.Paths
import model.Graph
import model.abstractGraph.AbstractVertex
import org.jetbrains.exposed.exceptions.ExposedSQLException
import viewModel.graph.GraphViewModel

class SQLiteSearchScreenViewModel {
    private val connection = SQLiteEXP(findPathForDB())
    private val converter = SQLiteConverter(connection)
    lateinit var graphList: MutableList<String>

    init {
        graphList = connection.makeListFromNames().toMutableList()
    }

    fun loadGraph(name: String): Pair<Graph, Map<AbstractVertex, Pair<Dp?, Dp?>?>>? {
        return converter.readFromSQLiteDB(name)
    }

    private fun findPathForDB(): String {
        val path =
            Paths.get(System.getProperty("user.home"), ".local", "share", "graphs2t", "databases")
                .toString()
        File(path).mkdirs()
        return "$path/app.db"
    }

    fun deleteGraph(name: String) {
        val graphInfo = connection.findGraph(name)
        if (graphInfo != null) {
            connection.deleteGraph(graphInfo.id)
        }
    }

    fun writeGraph(viewModel: GraphViewModel, name: String) {
        try {
            converter.saveToSQLiteDB(viewModel, name)
        } catch (e: ExposedSQLException) {
            throw e
        }
    }
}
