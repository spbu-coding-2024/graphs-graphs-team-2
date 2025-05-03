package AlgorithmsTest

import algo.StronglyConnectedComponents
import kotlin.test.assertEquals
import model.Graph
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ComponentsTest {
    private lateinit var graph: Graph

    @BeforeEach
    fun initGraph() {
        graph = Graph(direction = true, weight = false)
    }

    @Test
    fun `isolated vertex`() {
        graph.addVertex(0, "a")

        val components = StronglyConnectedComponents(graph).components
        assertEquals(setOf(setOf(0L)), components)
    }

    @Test
    fun `one cycle`() {
        graph.addVertex(0, "b")
        graph.addVertex(1, "c")
        graph.addVertex(2, "d")
        graph.addEdge(0, 1, "A", 0)
        graph.addEdge(1, 2, "B", 1)
        graph.addEdge(2, 0, "C", 2)

        val components = StronglyConnectedComponents(graph).components
        assertEquals(setOf(setOf(0L, 1L, 2L)), components)
    }

    @Test
    fun `two components without path`() {
        graph.addVertex(0, "b")
        graph.addVertex(1, "c")
        graph.addVertex(2, "d")
        graph.addEdge(0, 1, "A", 0)
        graph.addEdge(1, 2, "B", 1)
        graph.addEdge(2, 0, "C", 2)
        graph.addVertex(3, "e")
        graph.addVertex(4, "f")
        graph.addVertex(5, "g")
        graph.addEdge(3, 4, "D", 3)
        graph.addEdge(4, 5, "E", 4)
        graph.addEdge(5, 3, "F", 5)

        val components = StronglyConnectedComponents(graph).components
        val expectedResult = setOf(setOf(0L, 1L, 2L), setOf(3L, 4L, 5L))
        assertEquals(expectedResult, components)
    }

    @Test
    fun `two components with path between them`() {
        graph.addVertex(0, "b")
        graph.addVertex(1, "c")
        graph.addVertex(2, "d")
        graph.addEdge(0, 1, "A", 0)
        graph.addEdge(1, 2, "B", 1)
        graph.addEdge(2, 0, "C", 2)
        graph.addVertex(3, "e")
        graph.addVertex(4, "f")
        graph.addVertex(5, "g")
        graph.addEdge(3, 4, "D", 3)
        graph.addEdge(4, 5, "E", 4)
        graph.addEdge(5, 3, "F", 5)
        graph.addEdge(0, 3, "G", 6)

        val components = StronglyConnectedComponents(graph).components
        val expectedResult = setOf(setOf(0L, 1L, 2L), setOf(3L, 4L, 5L))
        assertEquals(expectedResult, components)
    }
}
