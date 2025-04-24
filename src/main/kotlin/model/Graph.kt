package model

import model.abstractGraph.AbstractEdge
import model.abstractGraph.AbstractGraph
import model.abstractGraph.AbstractVertex

class Graph (
    private val direction: Boolean = false,
    private val weight: Boolean = false,
) : AbstractGraph {

    private val _vertices = hashMapOf<Long, Vertex>()
    private val _edges = hashMapOf<Long, Edge>()

    override val vertices: Collection<AbstractVertex>
        get() = _vertices.values
    override val edges: Collection<AbstractEdge>
        get() = _edges.values

    override val isDirected: Boolean
        get() = direction
    override val isWeighted: Boolean
        get() = weight


    override fun addVertex(id: Long, label: String): AbstractVertex = _vertices.getOrPut(id) { Vertex(label, id) }

    override fun addEdge(firstID: Long, secondID: Long,
                         edgeLabel: String, edgeID: Long,
                         weight: Float): AbstractEdge {

        val first = _vertices[firstID]
        val second = _vertices[secondID]
        if (first == null || second == null)
            throw IllegalStateException("Graph has no vertices with ID $firstID and $secondID")

        return _edges.getOrPut(edgeID) { Edge(edgeLabel, edgeID, first, second, weight) }
    }

    private var _graphMap = mapOf<Long, ArrayDeque<Long>>()
    val graphMap: Map<Long, ArrayDeque<Long>>
        get() {
            if(_graphMap.isEmpty()) computeMap()
            return _graphMap
        }
    private fun computeMap() {
        _graphMap = _vertices.keys.associateWith{ ArrayDeque() }
        edges.forEach {
            _graphMap[it.vertices.first.id]?.add(it.vertices.second.id)
                ?: throw IllegalStateException("Edge ${it.id} contains non-existing vertex")
        }
    }

    private data class Vertex(override var label: String, override var id: Long) : AbstractVertex

    private data class Edge(
        override val label: String,
        override val id: Long,
        var first: Vertex,
        var second: Vertex,
        override val weight: Float,
    ) : AbstractEdge {
        override val vertices
            get() = first to second
    }
}