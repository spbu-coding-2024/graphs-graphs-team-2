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

    /**
     *           A              Test finds the shortest path from A -> E ( A -> C -> D -> E)
     *         /   \            and checks whether the desired edges have been colored and enlarged,
     *       4/     \2          and whether the rest have remained the same.
     *       /       \
     *      ↓         ↓
     *      B ---5--→  C
     *       \        / \
     *       10\    /3   \8
     *          \  /      \
     *           ↓↓        ↓
     *            D --1--→ E
     *            ↑        |
     *            |<---4---|
     */
    @Test
    fun `dijkstra paints the path`() {

        val graph = Graph(true, true)
        graph.addVertex(1L, "A")
        graph.addVertex(2L, "B")
        graph.addVertex(3L, "C")
        graph.addVertex(4L, "D")
        graph.addVertex(5L, "E")
        graph.addEdge(1L, 2L, "A -> B", 6L, 4F)
        graph.addEdge(1L, 3L, "A -> C", 7L, 2F)
        graph.addEdge(2L, 3L, "B -> C", 8L, 5F)
        graph.addEdge(2L, 4L, "B -> D", 9L, 10F)
        graph.addEdge(3L, 4L, "C -> D", 10L, 3F)
        graph.addEdge(3L, 5L, "C -> E", 11L, 8F)
        graph.addEdge(4L, 5L, "D -> E", 12L, 1F)
        graph.addEdge(5L, 4L, "E -> D", 13L, 4F)

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
                    assert(it.width == 5F)
                    assert(it.color == CoolColors.Bardo)
                }

                it.u.ID == 3L && it.v.ID == 4L -> {
                    assert(it.width == 5F)
                    assert(it.color == CoolColors.Bardo)
                }

                it.u.ID == 4L && it.v.ID == 5L -> {
                    assert(it.width == 5F)
                    assert(it.color == CoolColors.Bardo)
                }

                else -> {
                    assert(it.width == 2F)
                    assert(it.color == CoolColors.DarkPurple)
                }
            }
        }
    }
}
