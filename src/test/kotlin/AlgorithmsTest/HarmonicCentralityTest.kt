package AlgorithmsTest

import algo.HarmonicCentrality
import model.Graph
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class HarmonicCentralityTest {
    private lateinit var graph: Graph

    @Test
    fun `isolated vertex`() {
        graph = Graph()
        graph.addVertex(0, "a")
        val centrality = HarmonicCentrality(graph)
        assertEquals(0f, centrality.getVertexCentrality(0))
    }

    @Test
    fun `unweighted undirected isosceles triangle`() {
        graph = Graph()
        graph.addVertex(0, "a")
        graph.addVertex(1, "b")
        graph.addVertex(2, "c")

        graph.addEdge(0, 1, "A", 0)
        graph.addEdge(2, 1, "B", 1)
        graph.addEdge(0, 2, "C", 2)

        val centrality = HarmonicCentrality(graph)

        assertEquals(centrality.getVertexCentrality(0), centrality.getVertexCentrality(1))
        assertEquals(centrality.getVertexCentrality(2), centrality.getVertexCentrality(1))
    }

    @Test
    fun `weighted directed triangle`() {
        graph = Graph(direction = true, weight = true)
        graph.addVertex(0, "a")
        graph.addVertex(1, "b")
        graph.addVertex(2, "c")

        graph.addEdge(0, 1, "A", 0, 1f)
        graph.addEdge(2, 1, "B", 1, 2f)
        graph.addEdge(0, 2, "C", 2, 3f)

        val expectedResult = mapOf(0L to 2f, 1L to 0f, 2L to 1f)

        val centrality = HarmonicCentrality(graph)

        graph.vertices.forEach {
            assertEquals(expectedResult[it.id], centrality.getVertexCentrality(it.id))
        }
    }
}