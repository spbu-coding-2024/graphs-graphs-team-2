package algo

import io.VertexInfo
import model.Graph

class FindLoops (graph: Graph, val vertex: Long) {
    private val isVisited =
        graph.vertices.associate { it.id to false }.toMutableMap()
    private val graph = graph.weightedMap
    private val parents = mutableMapOf<Long, Long>()
    val loopEdges=ArrayDeque<Long>()
    fun callByGraphDirection(){

    }

    fun findLoopInDirectedGraph(){
        loopEdges.addLast(vertex)
        outer@ while (!loopEdges.isEmpty()) {
            val vert = loopEdges.first()
            isVisited[vert]=true
            val edges= graph[vert]

            if (edges != null) {
                for (el in edges) {
                    if(isVisited[el.first]==false) {
                        loopEdges.addFirst(el.first)
                        continue@outer
                    }else if(el.first==vertex){
                        loopEdges.addFirst(vertex)
                        return
                    }
                }
            }
            loopEdges.removeFirst()
        }
    }

    fun findLoopInUndirectedGraph(){
        val parents = mutableMapOf<Long, Long>()
        loopEdges.addFirst(vertex)
        outer@ while (!loopEdges.isEmpty()) {
            val vert = loopEdges.first()
            isVisited[vert]=true
            val edges= graph[vert]

            if (edges != null) {
                for (el in edges) {
                    if(isVisited[el.first]==false) {
                        parents[el.first]=vert
                        loopEdges.addFirst(el.first)
                        continue@outer
                    }else if(el.first==vertex && parents[vert]!=vertex){
                        loopEdges.addFirst(vertex)
                        return
                    }
                }
            }
            loopEdges.removeFirst()
        }
    }
}