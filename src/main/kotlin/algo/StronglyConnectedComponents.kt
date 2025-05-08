package algo

import model.Graph

class StronglyConnectedComponents(private val graph: Graph) {


    private var _components = mutableSetOf<Set<Long>>()
    val components: Set<Set<Long>>
        get() {
            return _components.toSet()
        }

    private val size = graph.vertices.size
    private var used = graph.vertices.associate { it.id to false }.toMutableMap()
    private val order = mutableListOf<Long>()

    private var currComponent = mutableSetOf<Long>()


    private val graphMap = graph.map

    private val transGraphMap: Map<Long, MutableList<Long>> =
        graph.vertices.associate { it.id to mutableListOf() }

    private fun prepare() {
        graph.edges.forEach {
            transGraphMap[it.vertices.second.id]?.add(it.vertices.first.id)
                ?: throw IllegalStateException("Edge ${it.id} contains non-existing vertices")
        }
    }

    private fun dfsPrepare(v: Long) {
        used[v] = true
        graphMap[v]?.forEach {
            used[it]?.let { isUsed ->
                if (!isUsed) {
                    dfsPrepare(it)
                }
            }
        }
        order.add(v)
    }

    private fun dfsBack(v: Long) {
        used[v] = true
        currComponent = currComponent.plusElement(v)
        transGraphMap[v]?.forEach { used[it]?.let { isUsed -> if (!isUsed) dfsBack(it) } }
    }

    init {
        prepare()
        graph.vertices.forEach { used[it.id]?.let { isUsed -> if (!isUsed) dfsPrepare(it.id) } }
        used = graph.vertices.associate { it.id to false }.toMutableMap()
        for (i in 0..<size) {
            val v = order[size - i - 1]
            used[v]?.let { isUsed ->
                if (!isUsed) {
                    dfsBack(v)

                    _components.add(currComponent)
                    currComponent = mutableSetOf()

                }
            }
        }
    }
}
