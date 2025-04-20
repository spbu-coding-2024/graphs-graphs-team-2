package inpout

import org.sqlite.SQLiteConnection
import viewModel.GraphViewModel
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths


/*fun main(){
    writeToSQLite()
}*/
fun writeToSQLite(name: String,viewModel: GraphViewModel) {
    val dbpath = findDBPath()
    val connection =SQLiteEXP(dbpath)
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
fun readFromSQLite(name:String){
    val p = findDBPath()
    val connection = SQLiteEXP(p)
    val gi = connection.findGraph(name)
    if(gi==null){
        return
    }
    if(gi.id==-1){
        return
    }
    val vertices = connection.findVertices(gi.id)
    val edges = connection.findEdges(gi.id)
}

fun findDBPath():String {
    val path = Paths.get(System.getProperty("user.home"),".local","share","graphs2t","databases").toString()
    File(path).mkdirs()
    return "$path/app.db"
}