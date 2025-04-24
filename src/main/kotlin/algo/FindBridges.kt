package algo

import kotlin.math.min


class AlgoBridges(val graph : Array<ArrayDeque<Int>>) {
    private val sizeOfGraph = graph.size
    private val used = Array<Boolean>(sizeOfGraph) { false }
    private var timer: Int = 0
    private val tin = Array<Int>(sizeOfGraph) { 0 }
    private val fup = Array<Int>(sizeOfGraph) { 0 }
    val bridges = ArrayDeque<Pair<Int,Int>>()

    private fun dfs(v: Int, p: Int = -1) {
        used[v] = true
        fup[v] = timer
        tin[v] = timer
        timer++
        for (i in 0..<graph[v].size){
            val to : Int = graph[v][i]
            if(to == p) continue
            if(used[to]){
                fup[v] = min(fup[v],tin[to])
            }
            else{
                dfs(to,v)
                fup[v] = min(fup[v],fup[to])
                if(fup[to] > tin[v]){
                    if(graph[v].count{u -> u == to} == 1) bridges.add(Pair(v,to))
                }
            }
        }
    }

    fun findBridges(){
        timer = 0
        for(i in 0..<sizeOfGraph){
            if(!used[i]){
                dfs(i)
            }
        }
    }
}