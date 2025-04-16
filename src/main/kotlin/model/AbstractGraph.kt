package model

interface AbstractGraph<V, E> {

    val vertices: Collection<AbstractVertex<V>>
    val edges: Collection<AbstractEdge<E, V>>

    fun addVertex(v: V): AbstractVertex<V>
    fun addEdge(u: V, v: V, edgeLabel: E, weight: Float = 1.0f): AbstractEdge<E, V>
}