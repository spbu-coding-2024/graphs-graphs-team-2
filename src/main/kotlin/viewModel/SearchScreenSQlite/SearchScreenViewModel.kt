package viewModel.SearchScreenSQlite

import inpout.SQLiteEXP
import io.SQLiteConverter
import viewModel.GraphViewModel
import java.io.File
import java.nio.file.Paths
import java.sql.SQLException

class SQLiteSearchScreenViewModel {
    private val connection = SQLiteEXP(findPathForDB())
    private val converter = SQLiteConverter(connection)
    val graphList
        get()=connection.makeListFromNames()
    fun loadGraph(name: String): GraphViewModel? {
        return converter.readFromSQLiteDB(name)
    }
    private fun findPathForDB():String{
        val path = Paths.get(System.getProperty("user.home"),".local","share","graphs2t","databases").toString()
        File(path).mkdirs()
        return "$path/app.db"
    }
}