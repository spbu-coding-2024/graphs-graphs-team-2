package algo

import model.Graph

class FindLoops(val graph: Graph, val vertex: Long) {
    private val isVisited = graph.vertices.associate { it.id to false }.toMutableMap()
    private val graphMap = graph.weightedMap
    private val parents = mutableMapOf<Long, Long>()
    val loopEdges = ArrayDeque<Long>()

    fun findLoopInDirectedGraph() {
        loopEdges.addLast(vertex)
        outer@ while (!loopEdges.isEmpty()) {
            val vert = loopEdges.first()
            isVisited[vert] = true
            val edges = graphMap[vert]

            if (edges != null) {
                for (el in edges) {
                    if (isVisited[el.first] == false) {
                        loopEdges.addFirst(el.first)
                        continue@outer
                    } else if (el.first == vertex) {
                        loopEdges.addFirst(vertex)
                        return
                    }
                }
            }
            loopEdges.removeFirst()
        }
        loopEdges.clear()
    }

    fun findLoopInUndirectedGraph() {
        val parents = mutableMapOf<Long, Long>()
        loopEdges.addFirst(vertex)
        outer@ while (!loopEdges.isEmpty()) {
            val vert = loopEdges.first()
            isVisited[vert] = true
            val edges = graphMap[vert]

            if (edges != null) {
                for (el in edges) {
                    if (isVisited[el.first] == false) {
                        parents[el.first] = vert
                        loopEdges.addFirst(el.first)
                        continue@outer
                    } else if (el.first == vertex && parents[vert] != vertex) {
                        loopEdges.addFirst(vertex)
                        return
                    }
                }
            }
            loopEdges.removeFirst()
        }
        loopEdges.clear()
    }
}
