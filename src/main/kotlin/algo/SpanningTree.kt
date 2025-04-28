package algo

import model.Graph

class SpanningTree(graph: Graph) {
    private val weightedMap = graph.weightedMap.toMutableMap()

    val minimalTree = mutableListOf<Pair<Long, Long>>()

    private val minEdge = graph.vertices.associate { it.id to Float.MAX_VALUE }.toMutableMap()
    private val endEdge: MutableMap<Long, Long?> =
        graph.vertices.associate { it.id to null }.toMutableMap()
    private val used = graph.vertices.associate { it.id to false }.toMutableMap()

    init {

        graph.edges.onEach {
            weightedMap[it.vertices.second.id]?.add(it.vertices.first.id to it.weight)
                ?: throw IllegalStateException("Edge ${it.id} contains non-existing vertex")
        }

        val queue = ArrayDeque<Pair<Float, Long>>()
        queue.add(-Float.MIN_VALUE to graph.vertices.first().id)
        while (queue.isNotEmpty()) {
            val u = queue.removeFirst().second
            used[u] = true

            if (endEdge[u] != null) {
                val v = endEdge[u] ?: u
                minimalTree.add(u to v)
            }

            weightedMap[u]?.onEach { edge ->
                val v = edge.first
                val cost = edge.second
                if (!used[v]!! && (endEdge[v] == null || cost < (minEdge[v] ?: Float.MAX_VALUE))) {
                    queue.remove(minEdge[v] to v)
                    minEdge[v] = cost
                    endEdge[v] = u
                    queue.add(cost to v)
                }
            }
        }
    }
}
