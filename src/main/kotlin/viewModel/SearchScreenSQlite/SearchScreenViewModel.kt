package viewModel.SearchScreenSQlite

import androidx.compose.ui.unit.Dp
import io.SQLiteExposed.SQLiteEXP
import io.SQLiteConverter
import model.Graph
import model.abstractGraph.AbstractVertex
import java.io.File
import java.nio.file.Paths


class SQLiteSearchScreenViewModel {
    private val connection = SQLiteEXP("app.db")
    private val converter = SQLiteConverter(connection)
    val graphList
        get()=connection.makeListFromNames().toMutableList()
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
}