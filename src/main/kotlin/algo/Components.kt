package algo

import model.Graph

class Components(private val graph: Graph) {

    private val _components = mutableListOf<MutableList<Long>>()
    val components: List<MutableList<Long>>
        get() {
            if(_components.isEmpty()) compute()
            return _components.toList()
        }


    private val size = graph.vertices.size
    private var used = graph.vertices.associate { it.id to false }.toMutableMap()
    private val order = mutableListOf<Long>()
    private var currComponent = mutableListOf<Long>()

    val graphMap = graph.graphMap

    val transGraphMap: Map<Long, ArrayDeque<Long>> = graph.vertices.associate { it.id to ArrayDeque() }

    private fun prepare() {
        graph.edges.forEach {
            transGraphMap[it.vertices.first.id]?.add(it.vertices.second.id)
                ?: throw IllegalStateException("Edge ${it.id} contains non-existing vertices")
        }
    }

    private fun dfsPrepare(v: Long) {
        used[v] = true
        graphMap[v]?.forEach {
            used[it]?.let { isUsed ->
                if(!isUsed) {
                    dfsPrepare(it)
                }
            }
        }
        order.add(v)
    }

    private fun dfsBack(v: Long) {
        used[v] = true
        currComponent.add(v)
        transGraphMap[v]?.forEach {
            used[it]?.let { isUsed -> if(!isUsed) dfsBack(it) }
        }
    }

    private fun compute() {
        prepare()
        graph.vertices.forEach {
            used[it.id]?.let { isUsed -> if(!isUsed) dfsPrepare(it.id) }
        }
        used = graph.vertices.associate { it.id to false }.toMutableMap()
        for(i in 0..<size) {
            val v = order[size - i - 1]
            used[v]?.let {isUsed ->
                if(!isUsed) {
                    dfsBack(v)
                    _components.add(currComponent)
                    currComponent = mutableListOf()
                }
            }
        }
    }

}