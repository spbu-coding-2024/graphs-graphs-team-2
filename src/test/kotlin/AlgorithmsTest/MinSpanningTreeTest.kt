package AlgorithmsTest

import algo.PrimSpanningTree
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import model.Graph
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MinSpanningTreeTest {
    private lateinit var graph: Graph

    @BeforeEach
    fun init() {
        graph = Graph(direction = false, weight = true)
    }

    @Test
    fun `empty graph`() {
        val minTree = PrimSpanningTree(graph).minimalTree
        assertNull(minTree)
    }

    @Test
    fun `not connected graph`() {
        graph.addVertex(0, "A")
        graph.addVertex(1, "B")
        graph.addVertex(2, "A")
        graph.addVertex(3, "C")

        graph.addEdge(0, 1, "0", 0, 1f)
        graph.addEdge(1, 2, "1", 1, 2f)

        val minTree = PrimSpanningTree(graph).minimalTree
        assertNull(minTree)
    }

    @Test
    fun `one way graph`() {
        graph.addVertex(0, "A")
        graph.addVertex(1, "B")
        graph.addVertex(2, "C")
        graph.addVertex(3, "D")

        graph.addEdge(0, 1, "0", 0, 1f)
        graph.addEdge(1, 2, "1", 1, 2f)
        graph.addEdge(2, 3, "2", 2, 3f)

        val minTree = PrimSpanningTree(graph).minimalTree
        assertNotNull(minTree)
        for (i in 0..<(minTree.size)) {
            assert(
                minTree.contains(i.toLong() to (i + 1).toLong()) ||
                    minTree.contains((i + 1).toLong() to i.toLong())
            )
        }
    }

    @Test
    fun `with negative weight`() {
        graph.addVertex(0, "A")
        graph.addVertex(1, "B")
        graph.addVertex(2, "C")

        graph.addEdge(0, 1, "0", 0, -2f)
        graph.addEdge(1, 2, "1", 1, 2f)
        graph.addEdge(0, 2, "2", 2, 3f)

        val actualResult = PrimSpanningTree(graph).minimalTree
        assertNotNull(actualResult)

        val expectedResult = listOf(1L to 0L, 2L to 1L)
        assertEquals(expectedResult, actualResult)
    }


}
