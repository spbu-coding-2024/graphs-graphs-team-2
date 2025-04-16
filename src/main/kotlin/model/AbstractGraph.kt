package model

interface AbstractGraph<V, E> {
    val isDirected: Boolean
    val isWeighted: Boolean

    val vertices: Collection<AbstractVertex<V>>
    val edges: Collection<AbstractEdge<E, V>>

    fun addVertex(v: V): AbstractVertex<V>
    fun addEdge(u: V, v: V, edgeLabel: E, weight: Long? = null): AbstractEdge<E, V>
}