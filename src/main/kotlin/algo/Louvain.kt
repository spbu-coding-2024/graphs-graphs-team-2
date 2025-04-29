package algo

import org.gephi.graph.api.Graph
import org.gephi.graph.api.GraphModel
import org.gephi.statistics.plugin.Modularity
import org.gephi.graph.api.Node


fun louvain(graphModelApp: model.Graph): Pair<Map<Long, Int>, Map<Pair<Long, Long>, Int>> {

    val graphModel = GraphModel.Factory.newInstance()
    val graph: Graph


    if (graphModelApp.isDirected) {
        graph = graphModel.directedGraph
    } else {
        graph = graphModel.undirectedGraph
    }

    val nodes = mutableMapOf<Long, Node>()

    graphModelApp.vertices.forEach { vertex ->
        val newNode = graphModel.factory().newNode(vertex.id.toString())
        graph.addNode(newNode)
        nodes.put(vertex.id, newNode)
    }

    graphModelApp.edges.forEach { edge ->

        graph.addEdge(
            graphModel.factory()
                .newEdge(
                    nodes[edge.vertices.first.id],
                    nodes[edge.vertices.second.id],
                    (edge.weight * 100).toInt(),
                    graphModelApp.isDirected
                )
        )
    }

    val modularity: Modularity = Modularity()
    modularity.setResolution(1.0)
    modularity.setRandom(true)
    modularity.execute(graph)

    val communityMapVertex = mutableMapOf<Long, Int>()
    val communityMapEdge = mutableMapOf<Pair<Long, Long>, Int>()


    graph.getNodes().forEach { node ->
        val nodeId = node.id.toString().toLong()
        val community = node.getAttribute(Modularity.MODULARITY_CLASS) as? Int ?: 0
        communityMapVertex[nodeId] = community
    }

    graph.getEdges().forEach { edge ->
        val firstNode = edge.source
        val secondNode = edge.target
        val firstNodeCommunity = firstNode.getAttribute(Modularity.MODULARITY_CLASS) as? Int ?: 0
        val secondNodeCommunity = secondNode.getAttribute(Modularity.MODULARITY_CLASS) as? Int ?: 0
        if (firstNodeCommunity == secondNodeCommunity) {
            val edgeId = Pair(firstNode.id.toString().toLong(), secondNode.id.toString().toLong())
            communityMapEdge[edgeId] = firstNodeCommunity
        }
    }

    return Pair(communityMapVertex, communityMapEdge)
}


