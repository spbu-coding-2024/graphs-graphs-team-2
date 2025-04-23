package algoritms

/*import model.AbstractEdge
import model.AbstractGraph
import model.AbstractVertex

fun FordBellman(graph:AbstractGraph, start: AbstractVertex){
    val mapa= mutableMapOf<AbstractVertex,AbstractEdge>()
    val dist = mutableMapOf<AbstractVertex, Float>()
    for (v in graph.vertices) {
        dist[v] = Float.POSITIVE_INFINITY
    }
    dist[start] = 0f
    val path = mutableMapOf<AbstractVertex, AbstractVertex>()
    for (i in 0..graph.vertices.size - 1) {
        for (e in graph.edges) {
            val d1=dist[e.vertices.first]
            val d2=dist[e.vertices.second]
            if (d1!=null && d2!=null){
                if(d1+e.weight<d2){
                    dist[e.vertices.second] = d1+e.weight
                    path[e.vertices.second]=e.vertices.first
                }
            }
        }
    }
    for (e in graph.edges) {
        val d1=dist[e.vertices.first]
        val d2=dist[e.vertices.second]
        if (d1!=null && d2!=null){
            if(d1+e.weight<d2){
                error("Graph contains negative loop")
            }
        }
    }
}*/