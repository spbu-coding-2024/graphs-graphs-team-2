package algo

import model.Graph
import java.util.LinkedList

class FordBellman (val graph: Graph, val firstVertexId: Long, val secondVertexId: Long) {
    private val infinity =  Float.POSITIVE_INFINITY
    private val distance =
        graph.vertices
            .associate { it.id to if (it.id == firstVertexId) 0F else infinity }
            .toMutableMap()
    private val graphMap = graph.weightedMap
    val path=mutableMapOf<Long,Long>()
    val pathFromStartToEnd = LinkedHashSet<Pair<Long, Long>>()
    fun FordBellman():Boolean {
        for (i in 0..graph.vertices.size - 1) {
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
        var currentId=secondVertexId
        var prevId: Long= path[currentId] ?: return false
        pathFromStartToEnd.add(prevId to currentId)
        while(prevId != firstVertexId) {
            prevId = path[currentId] ?:  return false
            pathFromStartToEnd.addFirst(prevId to currentId)
            currentId = prevId
        }
        for (e in graphMap) {
            val d1 = distance[e.key]
            for (edge in e.value) {
                val d2 = distance[edge.first]
                if (d1 != null && d2 != null) {
                    if (d1 + edge.second < d2) {
                        if(pathFromStartToEnd.contains(e.key to edge.first)) {
                            error("Graph contains negative loop")
                        }
                    }
                }
            }
        }
        return true
    }
}