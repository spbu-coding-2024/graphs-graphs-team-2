package algo

import org.gephi.graph.api.*
import org.gephi.project.api.ProjectController
import org.gephi.project.api.Workspace
import org.gephi.statistics.plugin.Modularity
import org.openide.util.Lookup

fun louvain(graphModelApp: model.Graph): Pair<Map<Long, Int>, Map<Pair<Long, Long>, Int>> {

    val projectController = Lookup.getDefault().lookup(ProjectController::class.java)
    projectController.newProject()
    val workspace: Workspace = projectController.currentWorkspace

    val graphModel = Lookup.getDefault().lookup(GraphController::class.java).graphModel
    val directed = graphModelApp.isDirected
    val graph = if (directed) graphModel.directedGraph else graphModel.undirectedGraph

    val nodeMap = mutableMapOf<Long, Node>()

    graphModelApp.vertices.forEach { vertex ->
        val newNode = graphModel.factory().newNode(vertex.id.toString())
        nodeMap[vertex.id] = newNode
        graph.addNode(newNode)
    }

    graphModelApp.edges.forEach { edge ->
        val source = nodeMap[edge.vertices.first.id]
        val target = nodeMap[edge.vertices.second.id]
        if (source != null && target != null) {
            val weight = edge.weight
            val newEdge = graphModel.factory().newEdge(source, target, (weight.toFloat() * 100).toInt(), directed)
            graph.addEdge(newEdge)
        }
    }

    val modularity = Modularity().apply {
        setResolution(1.0)
        setRandom(false)
    }

    modularity.execute(graphModel)

    val communityMapVertex = mutableMapOf<Long, Int>()
    val communityMapEdge = mutableMapOf<Pair<Long, Long>, Int>()

    graph.nodes.toArray().forEach { node ->
        val id = node.id.toString().toLong()
        val community = node.getAttribute(Modularity.MODULARITY_CLASS) as? Int
        if (community != null) {
            communityMapVertex[id] = community
        }
    }

    graph.edges.toArray().forEach { edge ->
        val src = edge.source
        val tgt = edge.target
        val srcComm = src.getAttribute(Modularity.MODULARITY_CLASS) as? Int
        val tgtComm = tgt.getAttribute(Modularity.MODULARITY_CLASS) as? Int
        if (srcComm != null && srcComm == tgtComm) {
            val key = Pair(
                src.id.toString().toLong(),
                tgt.id.toString().toLong()
            )
            communityMapEdge[key] = srcComm
        }
    }

    return Pair(communityMapVertex, communityMapEdge)
}
