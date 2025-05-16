package algo

import kotlin.IllegalStateException
import model.Graph

class FordBellman(val graph: Graph, val firstVertexId: Long, val secondVertexId: Long) {
    private val infinity = 1_000_000_000_000_000_000F
    private val distance =
        graph.vertices
            .associate { it.id to if (it.id == firstVertexId) 0F else infinity }
            .toMutableMap()
    private val graphMap = graph.weightedMap
    val path = mutableMapOf<Long, Long>()
    val pathFromStartToEnd = LinkedHashSet<Pair<Long, Long>>()

    fun FordBellman(): Boolean {
        val isReachable = checkReachability()
        if (!isReachable) {
            return false
        }
        for (i in 1..graph.vertices.size - 1) {
            for (e in graphMap) {
                val d1 = distance[e.key]
                for (edge in e.value) {
                    val d2 = distance[edge.first]

                    if (d1 != null && d2 != null) {
                        if (d1 + edge.second < d2) {
                            distance[edge.first] = d1 + edge.second
                            path[edge.first] = e.key
                        }
                    }
                }
            }
        }
        var currentId = secondVertexId
        var prevId: Long = path[currentId] ?: return false
        val visited = mutableMapOf<Long, Boolean>()
        visited[currentId] = true
        while (prevId != firstVertexId) {
            prevId = path[currentId] ?: return false
            if (visited[prevId] == true) {
                throw IllegalStateException("Path contains negative loop")
            }
            visited[prevId] = true
            pathFromStartToEnd.addFirst(prevId to currentId)
            currentId = prevId
        }

        for (e in graphMap) {
            val d1 = distance[e.key]
            for (edge in e.value) {
                val d2 = distance[edge.first]
                if (d1 != null && d2 != null) {
                    if (d1 + edge.second < d2) {
                        if (pathFromStartToEnd.contains(e.key to edge.first)) {
                            throw IllegalStateException("Graph contains negative loop")
                        }
                    }
                }
            }
        }
        return true
    }

    fun checkReachability(): Boolean {
        val queue = ArrayDeque<Long>()
        val visitedVertices = HashSet<Long>()
        queue.add(firstVertexId)
        visitedVertices.add(firstVertexId)
        while (queue.isNotEmpty()) {
            val vertex = queue.removeFirst()
            for (neighbour in graphMap[vertex] ?: return false) {
                if (!visitedVertices.contains(neighbour.first)) {
                    if (neighbour.first == secondVertexId) {
                        return true
                    } else {
                        queue.addLast(neighbour.first)
                        visitedVertices.add(neighbour.first)
                    }
                }
            }
        }
        return false
    }
}
