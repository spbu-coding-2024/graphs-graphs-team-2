package algo

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
                error("Path contains negative loop")
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
                            error("Graph contains negative loop")
                        }
                    }
                }
            }
        }
        return true
    }
}
