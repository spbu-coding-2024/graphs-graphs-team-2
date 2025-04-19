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
    val p = findDBPath()
    val dbp=p+"/app.db"
    println(dbp)
    val con = SQLiteEXP(dbp)
    var c=con.addGraph(name)
    if(c==-1){
        println("This database is already exists")
        c=con.findGraph("popada")
    }
    con.addVerrtex(c,1L,0.0,0.0)
    //con.deleteGraph(c)
}
fun readFromSQLite(name:String){
    val p = findDBPath()
    val dbp=p+"/app.db"
//    val con = SQLiteEXP()
}

fun findDBPath():String {
    val path = Paths.get(System.getProperty("user.home"),".local","share","graphs2t","databases").toString()
    File(path).mkdirs()
    return path
}