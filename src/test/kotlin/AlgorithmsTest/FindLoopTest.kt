package AlgorithmsTest

import algo.FindLoop
import kotlin.test.Test
import model.Graph
import org.junit.jupiter.api.Assertions.assertEquals

class FindLoopTest {
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
        val loop = algoLoop.loopEdges.toList()
        assertEquals(4, loop.size)
        assertEquals(loop[0], 1L)
        assertEquals(loop[1], 3L)
        assertEquals(loop[2], 2L)
        assertEquals(loop[3], 1L)
    }

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
        val loop = algoLoop.loopEdges
        assert(loop.isEmpty)
    }

    @Test
    fun testFindLoopInDirectedGraphWithoutLoopWithVertex() {
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
        val loop = algoLoop.loopEdges
        assert(loop.isEmpty)
    }

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
        val loop = algoLoop.loopEdges
        assertEquals(loop.size, 5)
        assertEquals(loop[0], 1L)
        assertEquals(loop[1], 4L)
        assertEquals(loop[2], 3L)
        assertEquals(loop[3], 2L)
        assertEquals(loop[4], 1L)
    }

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
        val loop = algoLoop.loopEdges
        assert(loop.isEmpty)
    }

    @Test
    fun testFindLoopInDirectedGraphWithoutLoopAndVertex() {
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
        val loop = algoLoop.loopEdges
        assert(loop.isEmpty)
    }
}
