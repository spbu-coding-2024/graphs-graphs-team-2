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
import viewModel.graph.EdgeViewModel
import viewModel.graph.VertexViewModel

fun place(
    vertices: Collection<VertexViewModel>,
    edges: Collection<EdgeViewModel>,
    isDirected: Boolean,
) {
    val pc: ProjectController = Lookup.getDefault().lookup(ProjectController::class.java)
    pc.newProject()
    val ws: Workspace = pc.currentWorkspace
    val graphModel: GraphModel =
        Lookup.getDefault().lookup(GraphController::class.java).getGraphModel(ws)
    val graph: Graph = graphModel.getGraph()
    val con = mutableMapOf<Long, Node>()
    for (vert in vertices) {
        val strID = vert.ID.toString()
        val n: Node = graphModel.factory().newNode(strID)
        n.label = strID
        n.setX(abs(Random.nextFloat() * 1000))
        n.setY(abs(Random.nextFloat() * 1000))
        n.setSize(vert.radius.value)
        graph.addNode(n)
        con[vert.ID] = n
    }
    for (edge in edges) {
        graph.addEdge(graphModel.factory().newEdge(con[edge.u.ID], con[edge.v.ID], isDirected))
    }
    val layout = ForceAtlas2(null)
    layout.setGraphModel(graphModel)
    layout.initAlgo()
    layout.isLinLogMode = true
    layout.gravity = 1.5
    layout.scalingRatio = 35.0

    var i = 0
    while (i < 100) {
        if (layout.canAlgo()) {
            layout.goAlgo()
        } else {
            break
        }
        for (nod in vertices) {
            val m = con[nod.ID]
            nod.x = m?.x()?.dp ?: nod.x
            nod.y = m?.y()?.dp ?: nod.y
        }

        i++
    }
    layout.endAlgo()
}
