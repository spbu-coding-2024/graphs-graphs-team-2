package viewModel.placement

import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.random.Random
import org.gephi.graph.api.Graph
import org.gephi.graph.api.GraphController
import org.gephi.graph.api.GraphModel
import org.gephi.graph.api.Node
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2
import org.gephi.project.api.ProjectController
import org.gephi.project.api.Workspace
import org.openide.util.Lookup
import viewModel.graph.GraphViewModel

fun place(width: Double, height: Double, graphViewModel: GraphViewModel) {
    val pc: ProjectController = Lookup.getDefault().lookup(ProjectController::class.java)
    pc.newProject()
    val ws: Workspace = pc.currentWorkspace
    val gm: GraphModel = Lookup.getDefault().lookup(GraphController::class.java).getGraphModel(ws)
    val gr: Graph = gm.getGraph()
    val con = mutableMapOf<String, Node>()
    val vertices = graphViewModel.vertices
    val edges = graphViewModel.edges
    for (vert in vertices) {
        val p = vert.ID.toString()
        val n: Node = gm.factory().newNode(p)
        n.label = p
        n.setX(abs(Random.nextFloat() * 1000))
        n.setY(abs(Random.nextFloat() * 1000))
        n.setSize(vert.radius.value)
        gr.addNode(n)
        con[p] = n
    }
    for (edg in edges) {
        gr.addEdge(gm.factory().newEdge(con[edg.u.ID.toString()], con[edg.v.ID.toString()], false))
    }
    val lay = ForceAtlas2(null)
    lay.setGraphModel(gm)
    lay.initAlgo()
    lay.isLinLogMode = true
    lay.gravity = 1.5
    lay.scalingRatio = 25.0

    var i = 0
    while (i < 100) {
        if (lay.canAlgo()) {
            lay.goAlgo()
        } else {
            break
        }
        for (nod in vertices) {
            val m = con[nod.ID.toString()]
            nod.x = m?.x()?.dp ?: nod.x
            nod.y = m?.y()?.dp ?: nod.y
        }

        i++
    }
    lay.endAlgo()
}
