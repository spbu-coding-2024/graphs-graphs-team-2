package JSONTest

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.JsonConverter
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import model.Graph
import model.abstractGraph.AbstractVertex
import org.junit.jupiter.api.Test
import viewModel.graph.GraphViewModel

class JSONConverterTest {
    private lateinit var graph: Graph
    private lateinit var graphViewModel: GraphViewModel
    private val converter = JsonConverter()

    @Test
    fun `simple weighted directed save`() {
        graph = Graph(direction = true, weight = true)
        val firstVertex = graph.addVertex(0, "A")
        val secondVertex = graph.addVertex(1, "B")
        graph.addEdge(0, 1, "a", 0, 2f)
        val placement = mapOf(firstVertex to Pair(1.dp, 2.dp), secondVertex to Pair(343.dp, 500.dp))

        val showVerticesLabels = mutableStateOf(false)
        val showVerticesIds = mutableStateOf(false)
        val showEdgesWeights = mutableStateOf(false)
        val showEdgesLabels = mutableStateOf(false)
        graphViewModel =
            GraphViewModel(
                graph,
                placement,
                showVerticesLabels,
                showVerticesIds,
                showEdgesWeights,
                showEdgesLabels,
            )

        val actualResult = converter.saveJson(graphViewModel)
        val expectedResult =
            "{\"direction\":true,\"weight\":true," +
                "\"vertices\":{\"0\":{\"label\":\"A\",\"x\":{\"value\":1.0},\"y\":{\"value\":2.0}}," +
                "\"1\":{\"label\":\"B\",\"x\":{\"value\":343.0},\"y\":{\"value\":500.0}}}," +
                "\"edges\":{\"0\":{\"label\":\"a\",\"from\":0,\"to\":1,\"weight\":2.0}}}"

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `simple weighted directed load`() {
        val jsonFormat =
            "{\"direction\":true,\"weight\":true," +
                "\"vertices\":{\"0\":{\"label\":\"A\",\"x\":{\"value\":1.0},\"y\":{\"value\":2.0}}," +
                "\"1\":{\"label\":\"B\",\"x\":{\"value\":343.0},\"y\":{\"value\":500.0}}}," +
                "\"edges\":{\"0\":{\"label\":\"a\",\"from\":0,\"to\":1,\"weight\":2.0}}}"

        graph = Graph(direction = true, weight = true)
        val firstVertex = graph.addVertex(0, "A")
        val secondVertex = graph.addVertex(1, "B")
        graph.addEdge(0, 1, "a", 0, 2f)
        val expectedPlacement =
            mapOf(firstVertex to Pair(1.dp, 2.dp), secondVertex to Pair(343.dp, 500.dp))

        val (actualGraph, actualPlacement) = converter.loadJson(jsonFormat)

        assert(actualGraph.isDirected)
        assert(actualGraph.isWeighted)

        assertContentEquals(graph.edges, actualGraph.edges)

        assertContentEquals(graph.vertices, actualGraph.vertices)

        assertEquals(expectedPlacement.size, actualPlacement.size)
        expectedPlacement.forEach { assertEquals(it.value, actualPlacement[it.key]) }
    }

    @Test
    fun `load without weight and direction tags`() {
        val jsonFormat =
            "{\"vertices\":{\"0\":{\"label\":\"A\",\"x\":{\"value\":1.0},\"y\":{\"value\":2.0}}," +
                "\"1\":{\"label\":\"B\",\"x\":{\"value\":343.0},\"y\":{\"value\":500.0}}}," +
                "\"edges\":{\"0\":{\"label\":\"a\",\"from\":0,\"to\":1}}}"

        graph = Graph(direction = false, weight = false)
        val firstVertex = graph.addVertex(0, "A")
        val secondVertex = graph.addVertex(1, "B")
        graph.addEdge(0, 1, "a", 0)
        val expectedPlacement =
            mapOf(firstVertex to Pair(1.dp, 2.dp), secondVertex to Pair(343.dp, 500.dp))

        val (actualGraph, actualPlacement) = converter.loadJson(jsonFormat)

        assert(!actualGraph.isDirected)
        assert(!actualGraph.isWeighted)

        assertContentEquals(graph.edges, actualGraph.edges)

        assertContentEquals(graph.vertices, actualGraph.vertices)

        assertEquals(expectedPlacement.size, actualPlacement.size)
        expectedPlacement.forEach { assertEquals(it.value, actualPlacement[it.key]) }
    }

    @Test
    fun `load without weight and direction tags and cords`() {
        val jsonFormat =
            "{\"vertices\":{\"0\":{\"label\":\"A\"}," +
                "\"1\":{\"label\":\"B\"}}," +
                "\"edges\":{\"0\":{\"label\":\"a\",\"from\":0,\"to\":1}}}"

        graph = Graph(direction = false, weight = false)
        val firstVertex = graph.addVertex(0, "A")
        val secondVertex = graph.addVertex(1, "B")
        graph.addEdge(0, 1, "a", 0)
        val expectedPlacement: Map<AbstractVertex, Pair<Dp?, Dp?>?> =
            mapOf(firstVertex to Pair(null, null), secondVertex to Pair(null, null))

        val (actualGraph, actualPlacement) = converter.loadJson(jsonFormat)

        assert(!actualGraph.isDirected)
        assert(!actualGraph.isWeighted)

        assertContentEquals(graph.edges, actualGraph.edges)

        assertContentEquals(graph.vertices, actualGraph.vertices)

        assertEquals(expectedPlacement.size, actualPlacement.size)
        expectedPlacement.forEach { assertEquals(it.value, actualPlacement[it.key]) }
    }

    @Test
    fun `integration converter test`() {
        graph = Graph(direction = true, weight = true)
        val firstVertex = graph.addVertex(0, "A")
        val secondVertex = graph.addVertex(1, "B")
        graph.addEdge(0, 1, "a", 0, 2f)
        val expectedPlacement =
            mapOf(firstVertex to Pair(1.dp, 2.dp), secondVertex to Pair(343.dp, 500.dp))

        val showVerticesLabels = mutableStateOf(false)
        val showVerticesIds = mutableStateOf(false)
        val showEdgesWeights = mutableStateOf(false)
        val showEdgesLabels = mutableStateOf(false)
        graphViewModel =
            GraphViewModel(
                graph,
                expectedPlacement,
                showVerticesLabels,
                showVerticesIds,
                showEdgesWeights,
                showEdgesLabels,
            )

        val jsonFormat = converter.saveJson(graphViewModel)
        val (actualGraph, actualPlacement) = converter.loadJson(jsonFormat)

        assertEquals(graph.isDirected, actualGraph.isDirected)
        assertEquals(graph.isWeighted, actualGraph.isWeighted)

        assertContentEquals(graph.edges, actualGraph.edges)

        assertContentEquals(graph.vertices, actualGraph.vertices)

        assertEquals(expectedPlacement.size, actualPlacement.size)
        expectedPlacement.forEach { assertEquals(it.value, actualPlacement[it.key]) }
    }
}
