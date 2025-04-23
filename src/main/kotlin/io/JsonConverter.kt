package io

import androidx.compose.ui.unit.Dp
import com.google.gson.Gson
import model.Graph
import model.abstractGraph.AbstractVertex
import viewModel.graph.GraphViewModel

data class VertexInfo(
    val label: String,
    val x: Dp?,
    val y: Dp?,
)

data class EdgeInfo(
    val label: String,
    val from: Long,
    val to: Long,
    val weight: Float?,
)

data class GraphInfo(
    val direction: Boolean?,
    val weight: Boolean?,
    var vertices: MutableMap<Long, VertexInfo>,
    var edges: MutableMap<Long, EdgeInfo>,
)

class JsonConverter() {

    fun saveJson(graph: GraphViewModel): String {
        val jsonSaver = Gson()
        try {
            val graphInfo = writeGraphInfo(graph)
            return jsonSaver.toJson(graphInfo)
        } catch(e: Exception) {
            throw IllegalStateException("Cannot save graph to JSON file: ${e.message}")
        }
    }

    fun loadJson(json: String): Pair<Graph, Map<AbstractVertex, Pair<Dp?, Dp?>?>> {
        val jsonReader = Gson()
        try {
            val info = jsonReader.fromJson(json, GraphInfo::class.java)
            return readGraphInfo(info)
        } catch (e: Exception) {
            print("ono")
            throw IllegalStateException("Cannot read JSON file: ${e.message}")
        }
    }


    private fun writeGraphInfo(graphViewModel: GraphViewModel): GraphInfo {
        val verticesInfo = mutableMapOf<Long, VertexInfo>()
        graphViewModel.vertices.forEach {
            verticesInfo[it.ID] = VertexInfo(
                it.label,
                it.x,
                it.y,
            )
        }

        val edgesInfo = mutableMapOf<Long, EdgeInfo>()
        graphViewModel.edges.forEach {
            edgesInfo[it.ID] = EdgeInfo(it.label,
                it.u.ID,
                it.v.ID,
                it.weight.toFloat()
            )
        }

        val info = GraphInfo (
            graphViewModel.isDirected,
            graphViewModel.isWeighted,
            verticesInfo,
            edgesInfo,
        )
        return info
    }

    private fun readGraphInfo(graphInfo: GraphInfo): Pair<Graph, Map<AbstractVertex, Pair<Dp?, Dp?>?>> {
        val place = mutableMapOf< AbstractVertex, Pair<Dp?, Dp?>>()
        val graph = Graph(graphInfo.direction == true, graphInfo.weight == true)
        graphInfo.vertices.forEach {
            place.put(graph.addVertex(it.key, it.value.label), it.value.x to it.value.y)
        }

        graphInfo.edges.forEach {
            graph.addEdge(it.value.from,
                it.value.to,
                it.value.label,
                it.key,
                it.value.weight ?: 1.0f)
        }

        return graph to place
    }
}