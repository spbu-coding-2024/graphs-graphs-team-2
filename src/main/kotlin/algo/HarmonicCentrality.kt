package algo

import model.Graph

class HarmonicCentrality(private val graph: Graph) {
    fun getVertexCentrality(u: Long): Float {
        var sum = 0f
        graph.vertices.onEach {
            if (it.id != u) {
                val path = AlgoDijkstra(graph, u, it.id)
                path.dijkstra(u)
                val pathLength = path.way.size
                if (pathLength != 0) sum += 1 / (pathLength - 1)
            }
        }
        return sum
    }
}
