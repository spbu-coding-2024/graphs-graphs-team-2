package io

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.SQLiteExposed.SQLiteEXP
import model.Graph
import model.abstractGraph.AbstractVertex

import viewModel.graph.GraphViewModel
import kotlin.collections.forEach
import kotlin.text.toFloat
import kotlin.to


class SQLiteConverter(val connection: SQLiteEXP){

    fun saveToSQLiteDB(viewModel: GraphViewModel, name :String){
        val id=connection.addGraph(name,viewModel.isDirected,viewModel.isWeighted)
        if(id==-1){
            return
        }
        connection.addAllvertices(id,viewModel.vertices)
        connection.addAllEdges(id,viewModel.edges)
    }

    fun readFromSQLiteDB(graphName:String):Pair<Graph, Map<AbstractVertex, Pair<Dp?, Dp?>?>>?{
        val gi =connection.findGraph(graphName)
        if(gi==null){
            return null
        }
        val vertices=connection.findVertices(gi.id)
        val edges=connection.findEdges(gi.id)
        val placement = mutableMapOf<AbstractVertex, Pair<Dp, Dp>>()
        val graph = Graph(gi.isDirected, gi.isWeighted)
        vertices.forEach {
            placement.put(graph.addVertex(it.vert,it.label),it.x.dp to it.y.dp)
        }
        edges.forEach {
            graph.addEdge(it.vertexFrom,it.vertexTo,it.label,it.id,it.weight)
        }
        return graph to placement
    }
}

