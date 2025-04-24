package model

import model.abstractGraph.AbstractEdge
import model.abstractGraph.AbstractGraph
import model.abstractGraph.AbstractVertex

class Graph(private val direction: Boolean = false, private val weight: Boolean = false) :
    AbstractGraph {

    private val _vertices = hashMapOf<Long, Vertex>()
    private val _edges = hashMapOf<Pair<Long, Long>, Edge>()

    override val vertices: Collection<AbstractVertex>
        get() = _vertices.values

    override val edges: Collection<AbstractEdge>
        get() = _edges.values

    override val isDirected: Boolean
        get() = direction

    override val isWeighted: Boolean
        get() = weight

    override fun addVertex(id: Long, label: String): AbstractVertex =
        _vertices.getOrPut(id) { Vertex(label, id) }

    override fun addEdge(
        firstID: Long,
        secondID: Long,
        edgeLabel: String,
        edgeID: Long,
        weight: Float,
    ): AbstractEdge {

        val first =
            _vertices[firstID]
                ?: throw IllegalStateException("Error: Graph has no vertex with ID $firstID")
        val second =
            _vertices[secondID]
                ?: throw IllegalStateException("Error: Graph has no vertex with ID $secondID")
        if (_edges.containsKey(firstID to secondID))
            throw IllegalStateException(
                "Error: Graph already contains edge from $firstID to $secondID"
            )

        return _edges.getOrPut(firstID to secondID) {
            Edge(edgeLabel, edgeID, first, second, weight)
        }
    }

    private var _map = mapOf<Long, MutableList<Long>>()
    val map: Map<Long, MutableList<Long>>
        get() {
            if (_map.isEmpty()) computeMap()
            return _map
        }

    private fun computeMap() {
        _map = _vertices.keys.associateWith { mutableListOf() }
        edges.forEach {
            _map[it.vertices.first.id]?.add(it.vertices.second.id)
                ?: throw IllegalStateException("Edge ${it.id} contains non-existing vertex")
            if (!isDirected) {
                _map[it.vertices.second.id]?.add(it.vertices.first.id)
                    ?: throw IllegalStateException("Edge ${it.id} contains non-existing vertex")
            }
        }
    }

    private var _weightedMap = mapOf<Long, MutableList<Pair<Long, Float>>>()
    val weightedMap: Map<Long, MutableList<Pair<Long, Float>>>
        get() {
            if (_weightedMap.isEmpty()) computeWeightedMap()
            return _weightedMap
        }

    private fun computeWeightedMap() {
        _weightedMap = _vertices.keys.associateWith { mutableListOf() }
        edges.forEach {
            _weightedMap[it.vertices.first.id]?.add(it.vertices.second.id to it.weight)
                ?: throw IllegalStateException("Edge ${it.id} contains non-existing vertex")
            if (!isDirected) {
                _weightedMap[it.vertices.second.id]?.add(it.vertices.first.id to it.weight)
                    ?: throw IllegalStateException("Edge ${it.id} contains non-existing vertex")
            }
        }
    }

    private var _graphWeightedMap = mapOf<Long, ArrayDeque<Pair<Long,Float>>>()
    val graphWeightedMap: Map<Long, ArrayDeque<Pair<Long,Float>>>
        get() {
            if(_graphWeightedMap.isEmpty()) computeWeightedMap()
            return _graphWeightedMap
        }
    private fun computeWeightedMap() {
        _graphWeightedMap = _vertices.keys.associateWith{ ArrayDeque() }
        edges.forEach {
            _graphWeightedMap[it.vertices.first.id]?.add(it.vertices.second.id to it.weight)
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
