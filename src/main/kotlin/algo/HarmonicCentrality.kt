package algo

import model.Graph

class HarmonicCentrality(private val graph: Graph) {
    fun getVertexCentrality(u: Long): Float {
        var sum = 0f
        graph.vertices.onEach {
            if(it.id != u) {
                val path = AlgoDijkstra(graph, u, it.id)
                path.dijkstra(u)
                val pathWeight = path.weightMinWay
                if(pathWeight != null) sum += 1 / pathWeight
            }
        }
        return sum
    }
}
