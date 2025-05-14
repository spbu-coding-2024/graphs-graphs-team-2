package viewModel.io

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.SQLiteConverter
import io.SQLiteExposed.SQLiteEXP
import java.io.File
import java.nio.file.Paths
import model.Graph
import model.abstractGraph.AbstractVertex
import org.jetbrains.exposed.exceptions.ExposedSQLException
import viewModel.graph.GraphViewModel

class SQLiteSearchScreenViewModel {
    private val _isLoading = mutableStateOf(false)
    val isLoading = _isLoading
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

fun main() {
    val graph = Graph()
    val placement = mutableMapOf<AbstractVertex, Pair<Dp, Dp>>()
    for (i in 1..1000) {
        placement.put(graph.addVertex(i.toLong(), i.toString()), 0.dp to 0.dp)
    }
    for (i in 1..1000) {
        try {
            graph.addEdge((1L..1000L).random(), (1L..1000L).random(), i.toString(), i.toLong(), 1f)
        } catch (e: IllegalStateException) {}
    }
    val gm =
        GraphViewModel(
            graph,
            placement,
            mutableStateOf(false),
            mutableStateOf(false),
            mutableStateOf(false),
            mutableStateOf(false),
        )
    SQLiteSearchScreenViewModel().writeGraph(gm, "megalolololol")

    /*val c = SQLiteEXP("app.db")
    for (i in 1..10000) {
        c.addGraph(i.toString(), false, false)
    }*/
}
