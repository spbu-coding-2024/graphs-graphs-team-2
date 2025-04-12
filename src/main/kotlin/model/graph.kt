package model

enum class GraphType {
    UnweightedUndirected,
    UnweightedDirected,
    WeightedUndirected,
    WeightedDirected,
}

interface Graph<V, E> {
    val vertices: Collection<Vertex<V>>
    val edges: Collection<Edge<E, V>>
    val type: GraphType

    fun addVertex(v: V): Vertex<V>
    fun addEdge(u: V, v: V, e: E): Edge<E, V>
}