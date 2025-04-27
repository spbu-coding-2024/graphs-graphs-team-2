package viewModel.SearchScreenSQlite

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.SQLiteExposed.SQLiteEXP
import io.SQLiteConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import model.Graph
import model.abstractGraph.AbstractGraph
import model.abstractGraph.AbstractVertex
import org.jetbrains.exposed.exceptions.ExposedSQLException
import viewModel.graph.GraphViewModel
import java.io.File
import java.nio.file.Paths
import java.sql.SQLException


class SQLiteSearchScreenViewModel {
    private val _isLoading=mutableStateOf(false)
    val isLoading = _isLoading
    private val connection = SQLiteEXP("app.db")
    private val converter = SQLiteConverter(connection)
    lateinit var graphList: MutableList<String>
    init{
        graphList = connection.makeListFromNames().toMutableList()
    }





    fun loadGraph(name: String): Pair<Graph, Map<AbstractVertex, Pair<Dp?, Dp?>?>>? {
        return converter.readFromSQLiteDB(name)
    }
    private fun findPathForDB():String{
        val path = Paths.get(System.getProperty("user.home"),".local","share","graphs2t","databases").toString()
        File(path).mkdirs()
        return "$path/app.db"
    }

    fun deleteGraph(name: String) {
        val graphInfo =connection.findGraph(name)
        if(graphInfo != null){
            connection.deleteGraph(graphInfo.id)
        }

    }

    fun writeGraph(viewModel: GraphViewModel, name: String){
        try {
            converter.saveToSQLiteDB(viewModel, name)
        }catch (e: ExposedSQLException){
            throw e
        }
    }
}

fun main(){
    val graph= Graph()
    val viewModel = SQLiteSearchScreenViewModel()
    val placement = mutableMapOf<AbstractVertex, Pair<Dp, Dp>>()
    for (i in 1..10000){
        placement.put(graph.addVertex(i.toLong(),i.toString()),0.dp to 0.dp)

    }
    for (i in 1..10000){
        graph.addEdge((1L..10000L).random(),(1L..10000L).random(),i.toString(),i.toLong(),1f)
    }
    val gm = GraphViewModel(graph,placement, mutableStateOf(false),mutableStateOf(false),mutableStateOf(false),mutableStateOf(false))
    SQLiteSearchScreenViewModel().writeGraph(gm,"megalo")
}