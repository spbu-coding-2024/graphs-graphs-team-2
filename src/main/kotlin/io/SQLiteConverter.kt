package io

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.SQLiteExposed.SQLiteEXP
import kotlin.collections.forEach
import kotlin.to
import model.Graph
import model.abstractGraph.AbstractVertex
import org.jetbrains.exposed.exceptions.ExposedSQLException
import viewModel.graph.GraphViewModel

class SQLiteConverter(val connection: SQLiteEXP) {

    fun saveToSQLiteDB(viewModel: GraphViewModel, name: String) {
        var graphID = 0
        try {
            val id = connection.addGraph(name, viewModel.isDirected, viewModel.isWeighted)
            graphID = id
        } catch (e: ExposedSQLException) {
            throw e
        }
        try {
            connection.addAllVertices(graphID, viewModel.vertices)
        } catch (e: ExposedSQLException) {
            throw e
        }
        connection.addAllEdges(graphID, viewModel.edges)
    }

    fun readFromSQLiteDB(graphName: String): Pair<Graph, Map<AbstractVertex, Pair<Dp?, Dp?>?>>? {
        val graphInfo = connection.findGraph(graphName)
        if (graphInfo == null) {
            return null
        }
        val vertices = connection.findVertices(graphInfo.id)
        val edges = connection.findEdges(graphInfo.id)
        val placement = mutableMapOf<AbstractVertex, Pair<Dp, Dp>>()
        val graph = Graph(graphInfo.isDirected, graphInfo.isWeighted)
        vertices.forEach { placement.put(graph.addVertex(it.vert, it.label), it.x.dp to it.y.dp) }
        edges.forEach { graph.addEdge(it.vertexFrom, it.vertexTo, it.label, it.id, it.weight) }
        return graph to placement
    }
}
