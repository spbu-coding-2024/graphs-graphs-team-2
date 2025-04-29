package algo

import kotlin.let
import kotlin.math.min
import model.Graph

class AlgoBridges(val graph: Graph) {
    private val used = graph.vertices.associate { it.id to false }.toMutableMap()
    private var timer: Int = 0
    private val tin = graph.vertices.associate { it.id to 0 }.toMutableMap()
    private val fup = graph.vertices.associate { it.id to 0 }.toMutableMap()

    val graphMap = graph.map

    val bridges = ArrayDeque<Pair<Long, Long>>()

    private fun dfs(v: Long, p: Long = -1) {
        used[v] = true
        fup[v] = timer
        tin[v] = timer
        timer++
        for (i in 0..<(graphMap[v]?.size ?: 0)) {
            val to = graphMap[v]?.get(i) ?: continue
            if (to == p) continue
            if (used[to] == true) {
                fup[v] = min(fup[v] ?: 0, tin[to] ?: 0)
            } else {
                dfs(to, v)
                fup[v] = min(fup[v] ?: 0, fup[to] ?: 0)
                fup[to]?.let {
                    if (it > (tin[v] ?: 0)) {
                        if (graphMap[v]?.count { u -> u == to } == 1) bridges.add(Pair(v, to))
                    }
                }
            }
        }
    }

    fun findBridges() {
        timer = 0
        graphMap.forEach {
            used[it.key]?.let { isUsed ->
                if (!isUsed) {
                    dfs(it.key)
                }
            }
        }
    }
}
