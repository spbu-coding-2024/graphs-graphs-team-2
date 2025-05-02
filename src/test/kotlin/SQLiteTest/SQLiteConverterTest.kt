package SQLiteTest

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.SQLiteConverter
import io.SQLiteExposed.Edges
import io.SQLiteExposed.Graphs
import io.SQLiteExposed.SQLiteEXP
import io.SQLiteExposed.Vertices
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import model.Graph
import model.abstractGraph.AbstractVertex
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import viewModel.graph.GraphViewModel

class SQLiteConverterTest {
    private val connection = SQLiteEXP("test.db")
    private val converter = SQLiteConverter(connection)

    @AfterEach
    fun tearDown() {
        transaction { SchemaUtils.drop(Graphs, Edges, Vertices) }
    }

    @Test
    fun testSaveAndLoad() {
        val graph = Graph()
        val placement = mutableMapOf<AbstractVertex, Pair<Dp, Dp>>()
        for (i in 1..10) {
            placement.put(graph.addVertex(i.toLong(), i.toString()), 0.dp to 0.dp)
        }
        for (i in 1L..9L) {
            graph.addEdge(i, i + 1, i.toString(), i, 1f)
        }
        val graphVM =
            GraphViewModel(
                graph,
                placement,
                mutableStateOf(false),
                mutableStateOf(false),
                mutableStateOf(false),
                mutableStateOf(false),
            )
        converter.saveToSQLiteDB(graphVM, "graph")
        val model = converter.readFromSQLiteDB("graph")
        if (model == null) {
            assert(false)
            return
        }
        val graph2 = model.first
        val placement2 = model.second
        assertEquals(graph2.vertices.size, graph.vertices.size)
        assertEquals(graph2.edges.size, graph.edges.size)
        assertEquals(placement2.size, placement.size)
        for (key in placement2.keys) {
            assert(placement.keys.contains(key))
            assertEquals(placement2[key], placement[key])
        }
        for (vertex in graph2.vertices) {
            assert(graph.vertices.contains(vertex))
        }
        for (edge in graph2.edges) {
            assert(graph.edges.contains(edge))
        }
    }

    companion object {
        @JvmStatic
        @AfterAll
        fun cleanUp(): Unit {
            File("test.db").delete()
        }
    }
}
