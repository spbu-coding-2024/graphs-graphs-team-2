package SQLiteTest

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.SQLiteExposed.Edges
import io.SQLiteExposed.Graphs
import io.SQLiteExposed.SQLiteEXP
import io.SQLiteExposed.Vertices
import java.io.File
import kotlin.test.assertEquals
import model.Graph
import model.abstractGraph.AbstractVertex
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import viewModel.graph.GraphViewModel

class SQLiteTest {

    private val connection = SQLiteEXP("test.db")

    @AfterEach
    fun tearDown() {
        transaction { SchemaUtils.drop(Graphs, Edges, Vertices) }
    }

    @Test
    fun test_addGraph() {
        val id = connection.addGraph("graph1", false, false)
        assert(id > 0)
    }

    @Test
    fun test_addGraphTwice() {
        val id = connection.addGraph("graph2", false, false)
        assert(id > 0)
        val exception =
            assertThrows<ExposedSQLException> { connection.addGraph("graph2", false, false) }

        assert(exception.message?.contains("A UNIQUE constraint failed") ?: false)
    }

    @Test
    fun testGraphFinding() {
        val id = connection.addGraph("graph3", false, false)
        val graphInfo = connection.findGraph("graph3")
        assertEquals(id, graphInfo?.id)
        assertEquals(false, graphInfo?.isWeighted)
        assertEquals(false, graphInfo?.isWeighted)
    }

    @Test
    fun deleteAllGraphs() {
        val graphArray = arrayOf("graph4", "graph5", "graph6", "graph7", "graph8")
        graphArray.forEach { connection.addGraph(it, false, false) }
        connection.deleteAll()
        val isGraphsTableEmpty = transaction { Graphs.selectAll().empty() }
        assert(isGraphsTableEmpty)
    }

    @Test
    fun testGraphList() {
        connection.deleteAll()
        val graphArray = arrayOf("graph9", "graph10", "graph11", "graph12", "graph13")
        graphArray.forEach { connection.addGraph(it, false, false) }
        val graphNames = connection.makeListFromNames()
        assertEquals(graphArray.size, graphNames.size)
        graphArray.forEach { assert(graphNames.contains(it)) }
    }

    @Test
    fun addAndGetVerticesAndEdges() {
        val id = connection.addGraph("graph17", false, false)
        val graph = Graph()
        val placement = mutableMapOf<AbstractVertex, Pair<Dp, Dp>>()
        for (i in 0..10) {
            placement.put(graph.addVertex(i.toLong(), i.toString()), 0.dp to 0.dp)
        }
        for (i in 0..10) {
            graph.addEdge(i % 11L, (i + 1) % 11L, i.toString(), i.toLong(), 1f)
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
        connection.addAllVertices(id, graphVM.vertices)
        connection.addAllEdges(id, graphVM.edges)
        val vertices = connection.findVertices(id)
        val edges = connection.findEdges(id)
        assertEquals(graphVM.vertices.size, vertices.size)
        assertEquals(graphVM.edges.size, edges.size)
        val graphFromDB = Graph()
        val placement2 = mutableMapOf<AbstractVertex, Pair<Dp, Dp>>()
        vertices.forEach {
            placement2.put(graphFromDB.addVertex(it.vert, it.label), it.x.dp to it.y.dp)
        }
        edges.forEach {
            graphFromDB.addEdge(it.vertexFrom, it.vertexTo, it.label, it.id, it.weight)
        }
        assertEquals(placement2.size, placement.size)
        for (key in placement2.keys) {
            assert(placement.keys.contains(key))
            assertEquals(placement2[key], placement[key])
        }
        for (vertex in graphFromDB.vertices) {
            assert(graph.vertices.contains(vertex))
        }
        for (edge in graphFromDB.edges) {
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
