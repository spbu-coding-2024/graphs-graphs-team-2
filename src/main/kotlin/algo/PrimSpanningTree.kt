package algo

import model.Graph

class PrimSpanningTree(graph: Graph) {
    private val weightedMap = graph.weightedMap.toMutableMap()

    private var _minimalTree: MutableList<Pair<Long, Long>>? = null

    val minimalTree
        get() = _minimalTree?.toList()

    private val minEdge = graph.vertices.associate { it.id to Float.MAX_VALUE }.toMutableMap()
    private val endEdge: MutableMap<Long, Long?> =
        graph.vertices.associate { it.id to null }.toMutableMap()
    private val used = graph.vertices.associate { it.id to false }.toMutableMap()

    init {

        val queue = ArrayDeque<Pair<Float, Long>>()
        if (graph.vertices.isNotEmpty()){
            queue.add(Float.MIN_VALUE to graph.vertices.first().id)
            _minimalTree = mutableListOf()
        }

        while (queue.isNotEmpty()) {
            val u = queue.removeFirst().second
            used[u] = true

            val v = endEdge[u]
            if (v != null) {
                _minimalTree?.add(u to v)
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

        if (!used.values.all { it }) _minimalTree = null
    }
}
