import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import model.Graph
import model.abstractGraph.AbstractVertex
import viewModel.graph.GraphViewModel
import java.io.File
import io.JsonConverter

fun main() {
    val content = File("/home/dmitrii/homework/graphs-graphs-team-2/src/main/kotlin/input.txt").readText().split("\n")
    val (countOfVertex, countOfEdges) = content[0].split(" ")
    val graph = Graph(true, true)

    val placement = mutableMapOf<AbstractVertex, Pair<Dp?, Dp?>?>()
    for (i in 1..countOfVertex.toInt()) {
        val (id, name) = content[i].split(" ", limit = 2)
        val v = graph.addVertex(id.toLong(), name)
        placement.put(v, null)
    }
    for (i in countOfVertex.toInt() + 1..<countOfVertex.toInt() + 1 + countOfEdges.toInt()) {
        val (id1, id2, weight) = content[i].split(" ")
        graph.addEdge(id1.toLong(), id2.toLong(), "", i.toLong(), weight.toFloat())
    }
    val viewModel = GraphViewModel(
        graph, placement, mutableStateOf(false),
        mutableStateOf(false),
        mutableStateOf(false),
        mutableStateOf(false),
    )

    File("/home/dmitrii/homework/graphs-graphs-team-2/src/main/kotlin/degree.txt").writeText(
        JsonConverter().saveJson(
            viewModel
        )
    )
}