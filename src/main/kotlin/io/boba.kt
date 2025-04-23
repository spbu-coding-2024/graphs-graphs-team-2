package io

import inpout.SQLiteEXP

fun main(){
    val t = SQLiteEXP("app.db")
    t.addGraph("graph1",false,false)
    t.addGraph("graph2",false,false)
    t.addGraph("graph3",false,false)
    t.addGraph("graph4",false,false)
    t.addGraph("graph5",false,false)
    t.addGraph("graph6",false,false)
}