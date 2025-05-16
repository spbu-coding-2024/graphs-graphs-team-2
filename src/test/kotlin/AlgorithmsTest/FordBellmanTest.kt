package AlgorithmsTest

import algo.FordBellman
import kotlin.test.assertEquals
import model.Graph
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class FordBellmanTest {
    @Test
    fun testFindTheShortestWay() {
        val graph = Graph(true, true)
        for (i in 1L..6L) {
            graph.addVertex(i, i.toString())
        }
        graph.addEdge(1L, 2L, "1", 1, 2F)
        graph.addEdge(2L, 3L, "2", 2, 3F)
        graph.addEdge(3L, 6L, "3", 3, 4F)
        graph.addEdge(4L, 5L, "4", 4, 1F)
        graph.addEdge(5L, 6L, "5", 5, 7F)
        graph.addEdge(1L, 4L, "6", 6, 1F)
        graph.addEdge(2L, 5L, "7", 7, -10F)
        val algoFB = FordBellman(graph, 1L, 6L)
        algoFB.FordBellman()
        val way = algoFB.pathFromStartToEnd.toList()
        assertEquals(way.size, 3)
        assertEquals(way[0], Pair(1L, 2L))
        assertEquals(way[1], Pair(2L, 5L))
        assertEquals(way[2], Pair(5L, 6L))
    }

    @Test
    fun findTheShortestWayWIthUnreachableNegativeLoop() {
        val graph = Graph(true, true)
        for (i in 1L..8L) {
            graph.addVertex(i, i.toString())
        }
        graph.addEdge(1L, 2L, "1", 1, 2F)
        graph.addEdge(2L, 3L, "2", 2, 3F)
        graph.addEdge(3L, 6L, "3", 3, 4F)
        graph.addEdge(4L, 5L, "4", 4, 1F)
        graph.addEdge(5L, 6L, "5", 5, 7F)
        graph.addEdge(1L, 4L, "6", 6, 1F)
        graph.addEdge(2L, 5L, "7", 7, -10F)
        graph.addEdge(5L, 7L, "8", 8, 1F)
        graph.addEdge(7L, 8L, "9", 9, -1F)
        graph.addEdge(8L, 7L, "10", 10, -1F)
        val algoFB = FordBellman(graph, 1L, 6L)
        algoFB.FordBellman()
        val way = algoFB.pathFromStartToEnd.toList()
        assertEquals(way.size, 3)
        assertEquals(way[0], Pair(1L, 2L))
        assertEquals(way[1], Pair(2L, 5L))
        assertEquals(way[2], Pair(5L, 6L))
    }

    @Test
    fun findTheShortestWayWIthNegativeLoopInPath() {
        val graph = Graph(true, true)
        for (i in 1L..6L) {
            graph.addVertex(i, i.toString())
        }
        graph.addEdge(1L, 2L, "1", 1, 2F)
        graph.addEdge(2L, 3L, "2", 2, 3F)
        graph.addEdge(3L, 6L, "3", 3, 4F)
        graph.addEdge(4L, 5L, "4", 4, 1F)
        graph.addEdge(5L, 6L, "5", 5, 7F)
        graph.addEdge(1L, 4L, "6", 6, 1F)
        graph.addEdge(2L, 5L, "7", 7, -10F)
        graph.addEdge(5L, 2L, "8", 8, -1F)
        val algoFB = FordBellman(graph, 1L, 6L)
        val exception = assertThrows<IllegalStateException> { algoFB.FordBellman() }
        assert(exception.message?.contains("Path contains negative loop") ?: false) {}
    }

    @Test
    fun testNegativeLoopInStart() {
        val graph = Graph(true, true)
        for (i in 1L..3L) {
            graph.addVertex(i, i.toString())
        }
        graph.addEdge(1L, 2L, "1", 1, -1F)
        graph.addEdge(2L, 1L, "2", 2, -1F)
        graph.addEdge(2L, 3L, "3", 3, 1F)
        val algoFB = FordBellman(graph, 1L, 3L)
        val exception = assertThrows<IllegalStateException> { algoFB.FordBellman() }
        assert(exception.message?.contains("Graph contains negative loop") ?: false) {}
    }

    @Test
    fun testUnreachableEnd() {
        val graph = Graph(true, true)
        for (i in 1L..3L) {
            graph.addVertex(i, i.toString())
        }
        graph.addEdge(1L, 2L, "1", 1, -1F)
        graph.addEdge(2L, 1L, "2", 2, -1F)
        val algoFB = FordBellman(graph, 1L, 3L)
        val result = algoFB.FordBellman()
        assert(result == false)
    }
}
