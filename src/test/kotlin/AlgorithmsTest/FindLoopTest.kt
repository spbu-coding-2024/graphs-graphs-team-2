package AlgorithmsTest

import algo.FindLoop
import kotlin.test.Test
import model.Graph
import org.junit.jupiter.api.Assertions.assertEquals

/**
 * * FindLoopTest class contains unit tests for finding loop for vertex in directed and undirected
 *   graphs
 */
class FindLoopTest {
    /** Test finds loop for vertex with ID 1 in undirected graph. The correct loop is 1-2-3-1 */
    @Test
    fun testFindLoopInUndirectedGraph() {
        val graph = Graph()
        for (i in 1L..4L) {
            graph.addVertex(i, i.toString())
        }
        graph.addEdge(1L, 2L, "1", 1L, 1f)
        graph.addEdge(2L, 3L, "2", 2L, 1f)
        graph.addEdge(3L, 1L, "3", 3L, 1f)
        graph.addEdge(4L, 2L, "4", 4L, 1f)
        val algoLoop = FindLoop(graph, 1L)
        algoLoop.findLoopInUndirectedGraph()
        val loop = algoLoop.loop.toList()
        assertEquals(4, loop.size)
        assertEquals(loop[0], 1L)
        assertEquals(loop[1], 3L)
        assertEquals(loop[2], 2L)
        assertEquals(loop[3], 1L)
    }

    /** In this test undirected graph doesn't contain loop with vertex 1 */
    @Test
    fun testFindLoopInUnDirectedGraphWithoutLoops() {
        val graph = Graph()
        for (i in 1L..4L) {
            graph.addVertex(i, i.toString())
        }
        graph.addEdge(1L, 2L, "1", 1L, 1f)
        graph.addEdge(2L, 3L, "2", 2L, 1f)
        graph.addEdge(4L, 2L, "4", 4L, 1f)
        val algoLoop = FindLoop(graph, 1L)
        algoLoop.findLoopInUndirectedGraph()
        val loop = algoLoop.loop
        assert(loop.isEmpty)
    }

    /** In this test undirected graph doesn't contain loop with vertex 1 */
    @Test
    fun testFindLoopInUnDirectedGraphWithoutLoopWithVertex() {
        val graph = Graph()
        for (i in 1L..4L) {
            graph.addVertex(i, i.toString())
        }
        graph.addEdge(1L, 2L, "1", 1L, 1f)
        graph.addEdge(2L, 3L, "2", 2L, 1f)
        graph.addEdge(3L, 4L, "3", 3L, 1f)
        graph.addEdge(4L, 2L, "4", 4L, 1f)
        val algoLoop = FindLoop(graph, 1L)
        algoLoop.findLoopInUndirectedGraph()
        val loop = algoLoop.loop
        assert(loop.isEmpty)
    }

    /** Test finds loop for vertex with ID 1 in directed graph. The correct loop is 1-4-3-2-1 */
    @Test
    fun testFindLoopInDirectedGraph() {
        val graph = Graph(true)
        for (i in 1L..4L) {
            graph.addVertex(i, i.toString())
        }
        graph.addEdge(1L, 2L, "1", 1L, 1f)
        graph.addEdge(2L, 3L, "2", 2L, 1f)
        graph.addEdge(3L, 4L, "3", 3L, 1f)
        graph.addEdge(4L, 1L, "4", 4L, 1f)
        val algoLoop = FindLoop(graph, 1L)
        algoLoop.findLoopInDirectedGraph()
        val loop = algoLoop.loop
        assertEquals(loop.size, 5)
        assertEquals(loop[0], 1L)
        assertEquals(loop[1], 4L)
        assertEquals(loop[2], 3L)
        assertEquals(loop[3], 2L)
        assertEquals(loop[4], 1L)
    }

    /** In this test directed graph doesn't contain any loop */
    @Test
    fun testFindLoopInDirectedGraphWithoutLoops() {
        val graph = Graph(true)
        for (i in 1L..4L) {
            graph.addVertex(i, i.toString())
        }
        graph.addEdge(1L, 2L, "1", 1L, 1f)
        graph.addEdge(2L, 3L, "2", 2L, 1f)
        graph.addEdge(3L, 4L, "3", 3L, 1f)
        val algoLoop = FindLoop(graph, 1L)
        algoLoop.findLoopInDirectedGraph()
        val loop = algoLoop.loop
        assert(loop.isEmpty)
    }

    /** In this test directed graph doesn't contain loop with vertex 1 */
    @Test
    fun testFindLoopInDirectedGraphWithoutLoopWithVertexVertex() {
        val graph = Graph(true)
        for (i in 1L..4L) {
            graph.addVertex(i, i.toString())
        }
        graph.addEdge(1L, 2L, "1", 1L, 1f)
        graph.addEdge(2L, 3L, "2", 2L, 1f)
        graph.addEdge(3L, 4L, "3", 3L, 1f)
        graph.addEdge(4L, 2L, "4", 4L, 1f)
        val algoLoop = FindLoop(graph, 1L)
        algoLoop.findLoopInDirectedGraph()
        val loop = algoLoop.loop
        assert(loop.isEmpty)
    }
}
