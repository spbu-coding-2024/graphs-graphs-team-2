package algo

import model.Graph


class AlgoDijkstra(val graph: Graph, val firstVertexId: Long, val secondVertexId: Long) {
    private val infinity = 1_000_000_000_000_000_000F
    private val distance =
        graph.vertices.associate { it.id to if (it.id == firstVertexId) 0F else infinity }.toMutableMap()
    private val labels = graph.vertices.associate { it.id to (it.id == firstVertexId) }.toMutableMap()
    private val parents = graph.vertices.associate { it.id to -1L }.toMutableMap()
    private val graphMap = graph.graphWeightedMap
    val way = ArrayDeque<Long>()

    var weightMinWay : Float? = null


    fun dijkstra(Vid: Long) {
        val edges = graphMap[Vid]
        for (i in 0..<edges!!.size) {
            val OutVid = edges[i].first
            val weight = edges[i].second
            if (!(labels[OutVid]!!)) {
                if (distance[Vid]!! + weight < distance[OutVid]!!) {
                    distance[OutVid] = distance[Vid]!! + weight
                    parents[OutVid] = Vid
                }
            }
        }


        var minDistance = infinity

        var new_Vid: Long? = null

        for (i in labels) {
            if (!i.value) {
                if (distance[i.key]!! < minDistance) {
                    minDistance = distance[i.key]!!
                    new_Vid = i.key
                }
            }
        }

        if (new_Vid != null) {
            labels[new_Vid] = true
            if (new_Vid == secondVertexId) {
                weightMinWay = minDistance

                findMinWay(new_Vid)
            } else {
                dijkstra(new_Vid)
            }
        }
    }


    private fun findMinWay(V: Long) {
        way.addFirst(V)
        if (V != firstVertexId) {
            findMinWay(parents[V]!!)
        }
    }
}
