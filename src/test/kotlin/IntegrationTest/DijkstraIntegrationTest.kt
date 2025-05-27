package IntegrationTest

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.random.Random
import kotlin.test.Test
import model.Graph
import model.abstractGraph.AbstractVertex
import view.components.CoolColors
import viewModel.graph.GraphViewModel

class DijkstraIntegrationTest {

    @Test
    fun `dijkstra paints the path`() {
        val graph = Graph(true, true)
        graph.addVertex(1, "A")
        graph.addVertex(2, "B")
        graph.addVertex(3, "C")
        graph.addVertex(4, "D")
        graph.addVertex(5, "E")
        graph.addEdge(1, 2, "A -> B", 6, 4F)
        graph.addEdge(1, 3, "A -> C", 7, 2F)
        graph.addEdge(2, 3, "B -> C", 8, 5F)
        graph.addEdge(2, 4, "B -> D", 9, 10F)
        graph.addEdge(3, 4, "C -> D", 10, 3F)
        graph.addEdge(3, 5, "C -> E", 11, 8F)
        graph.addEdge(4, 5, "D -> E", 12, 1F)
        graph.addEdge(5, 4, "E -> D", 13, 4F)

        val placement = mutableMapOf<AbstractVertex, Pair<Dp, Dp>>()

        graph.vertices.forEach { vertex ->
            placement.put(
                vertex,
                Pair(Random.Default.nextInt(1, 1000).dp, Random.Default.nextInt(100, 1000).dp),
            )
        }

        val viewModel =
            GraphViewModel(
                graph,
                placement,
                mutableStateOf(false),
                mutableStateOf(false),
                mutableStateOf(false),
                mutableStateOf(false),
            )

        viewModel.firstIdDijkstra = "1"
        viewModel.secondIdDijkstra = "5"

        viewModel.Dijkstra()

        viewModel.edges.forEach {
            when {
                it.u.ID == 1L && it.v.ID == 3L -> {
                    assert(it.width == 20F)
                    assert(it.color == CoolColors.Bardo)
                }

                it.u.ID == 3L && it.v.ID == 4L -> {
                    assert(it.width == 20F)
                    assert(it.color == CoolColors.Bardo)
                }

                it.u.ID == 4L && it.v.ID == 5L -> {
                    assert(it.width == 20F)
                    assert(it.color == CoolColors.Bardo)
                }

                else -> {
                    assert(it.width == 1F)
                    assert(it.color == CoolColors.Purple)
                }
            }
        }
    }
}
