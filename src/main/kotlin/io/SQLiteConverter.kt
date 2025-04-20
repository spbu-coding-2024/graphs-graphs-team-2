package io

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import inpout.SQLiteEXP
import model.AbstractVertex
import model.Graph
import viewModel.GraphViewModel


class SQLiteConverter(val connection: SQLiteEXP){

    fun saveToSQLiteDB(viewModel:GraphViewModel,name :String){
        val id=connection.addGraph(name,viewModel.isDirected,viewModel.isWeighted)
        if(id==-1){
            return
        }
        viewModel.vertices.forEach{
            connection.addVertex(id,it.ID,it.x.value,it.y.value,it.label)
        }
        viewModel.edges.forEach{
            connection.addEdge(id,it.u.ID,it.v.ID,it.weight.toFloat(),it.ID,it.label)
        }
    }

    fun readFromSQLiteDB(graphName:String):GraphViewModel?{
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
        return GraphViewModel(graph,placement, mutableStateOf(false),mutableStateOf(false),mutableStateOf(false))
    }
}

