package algo

import model.Graph

class FindLoop(val graph: Graph, val vertex: Long) {
    private val visitedVertices = graph.vertices.associate { it.id to false }.toMutableMap()
    private val graphMap = graph.weightedMap
    val loop = ArrayDeque<Long>()

    fun findLoopInDirectedGraph() {
        loop.addLast(vertex)
        outer@ while (!loop.isEmpty()) {
            val currentVertex = loop.first()
            visitedVertices[currentVertex] = true
            val edges = graphMap[currentVertex]

            if (edges != null) {
                for (el in edges) {
                    if (visitedVertices[el.first] == false) {
                        loop.addFirst(el.first)
                        continue@outer
                    } else if (el.first == vertex) {
                        loop.addFirst(vertex)
                        return
                    }
                }
            }
            loop.removeFirst()
        }
        loop.clear()
    }

    fun findLoopInUndirectedGraph() {
        val parents = mutableMapOf<Long, Long>()
        loop.addFirst(vertex)
        outer@ while (!loop.isEmpty()) {
            val currentVertex = loop.first()
            visitedVertices[currentVertex] = true
            val edges = graphMap[currentVertex]

            if (edges != null) {
                for (el in edges) {
                    if (visitedVertices[el.first] == false) {
                        parents[el.first] = currentVertex
                        loop.addFirst(el.first)
                        continue@outer
                    } else if (el.first == vertex && parents[currentVertex] != vertex) {
                        loop.addFirst(vertex)
                        return
                    }
                }
            }
            loop.removeFirst()
        }
        loop.clear()
    }
}
