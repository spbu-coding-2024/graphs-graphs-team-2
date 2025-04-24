package view.algo

import algo.AlgoBridges
import model.Graph
import view.components.CoolColors
import viewModel.graph.GraphViewModel
import viewModel.graph.VertexViewModel

class FindBridgesView(val graph: Graph, val graphViewModel: GraphViewModel) {
    private val keys = mutableMapOf<Long, Int>()
    private val keysToVertex = mutableMapOf<Int, VertexViewModel>()
    private fun graphTransformation(graph: Graph): Array<ArrayDeque<Int>> {
        var key = 0
        val newGraph = Array<ArrayDeque<Int>>(graph.vertices.size) { ArrayDeque<Int>() }
        graph.vertices.forEach { vertex -> keys.put(vertex.id, key++) }
        graphViewModel.vertices.forEach { vertex ->
            keysToVertex.put(
                keys[vertex.ID] ?: throw IllegalArgumentException("no such vertex in graph"),
                vertex
            )
        }
        graph.edges.forEach { edge ->
            val firstKey = keys[edge.vertices.first.id] ?: throw IllegalArgumentException("no such vertex in graph")
            val secondKey = keys[edge.vertices.second.id] ?: throw IllegalArgumentException("no such vertex in graph")
            newGraph[firstKey].add(secondKey)
            newGraph[secondKey].add(firstKey)
        }
        return newGraph
    }

    fun DrawBridges() {
        val newGraph = graphTransformation(graph)
        val algoBridges = AlgoBridges(newGraph)
        algoBridges.findBridges()
        algoBridges.bridges.forEach { bridge ->
            graphViewModel.edges.forEach { edges ->
                if ((edges.v === keysToVertex[bridge.first] &&
                            edges.u === keysToVertex[bridge.second]
                            ) || (edges.u === keysToVertex[bridge.first] &&
                            edges.v === keysToVertex[bridge.second]
                            )
                ) {
                    print("1")
                    edges.color = CoolColors.Bardo
                }
            }
        }
    }
}