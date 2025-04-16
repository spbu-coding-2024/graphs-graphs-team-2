package model

class Graph<V, E> (
    val direction: Boolean = false
) : AbstractGraph<V, E> {

    private val _vertices = hashMapOf<V, Vertex<V>>()
    private val _edges = hashMapOf<E, Edge<E, V>>()

    override val vertices: Collection<AbstractVertex<V>>
        get() = _vertices.values
    override val edges: Collection<AbstractEdge<E, V>>
        get() = _edges.values
    val isDirected: Boolean
        get() = direction

    override fun addVertex(v: V): AbstractVertex<V> = _vertices.getOrPut(v) { Vertex(v) }

    override fun addEdge(u: V, v: V, edgeLabel: E, weight: Float): AbstractEdge<E, V> {
        val first = addVertex(u)
        val second = addVertex(v)
        return _edges.getOrPut(edgeLabel) { Edge(edgeLabel, first, second, weight) }
    }

    private data class Vertex<V>(override var element: V) : AbstractVertex<V>

    private data class Edge<E, V>(
        override var element: E,
        var first: AbstractVertex<V>,
        var second: AbstractVertex<V>,
        override val weight: Float,
    ) : AbstractEdge<E, V> {
        override val vertices
            get() = first to second
    }
}