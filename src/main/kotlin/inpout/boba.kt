package inpout

fun main(){
    val t=SQLiteEXP("boba.db")
    t.addGraph("popada")
    val c= t.findGraph("popada")
    println(c)
    t.addVerrtex(c,2,1.0,1.0)
    t.deleteGraph(c)
}