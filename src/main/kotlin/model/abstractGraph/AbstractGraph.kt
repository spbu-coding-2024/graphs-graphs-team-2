package model.abstractGraph

import model.abstractGraph.AbstractVertex

interface AbstractGraph {
    val vertices: Collection<AbstractVertex>
    val edges: Collection<AbstractEdge>

    val isDirected: Boolean
    val isWeighted: Boolean

    fun addVertex(v: Long, vertexLabel: String): AbstractVertex
    fun addEdge(u: Long, v: Long, edgeLabel: String, edgeID: Long, weight: Float = 1.0f): AbstractEdge
}