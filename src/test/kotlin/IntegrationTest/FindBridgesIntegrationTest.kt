package IntegrationTest

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.random.Random
import model.Graph
import model.abstractGraph.AbstractVertex
import org.junit.jupiter.api.Test
import view.components.CoolColors
import viewModel.graph.GraphViewModel

class FindBridgesIntegrationTest {

    /**
     *       A ------ B    Test finds bridges in graph ( C -- D, D -- E)
     *       |        |    and checks whether the desired edges have been colored and enlarged,
     *       |        |
     *       F        C -- D -- E
     *      | \      /
     *      |  \    /      and whether the rest have remained the same.
     *      |   \  /
     *      G -- H
     */
    @Test
    fun `findBridges paints the path`() {
        val graph = Graph(false, true)
        graph.addVertex(0L, "A")
        graph.addVertex(1L, "B")
        graph.addVertex(2L, "C")
        graph.addVertex(3L, "D")
        graph.addVertex(4L, "E")
        graph.addVertex(5L, "F")
        graph.addVertex(6L, "G")
        graph.addVertex(7L, "H")
        graph.addEdge(5L, 6L, "F <-> G", 8L, 3F)
        graph.addEdge(6L, 7L, "G <-> H", 9L, 3F)
        graph.addEdge(7L, 5L, "H <-> F", 10L, 3F)
        graph.addEdge(0L, 1L, "A <-> B", 11L, 3F)
        graph.addEdge(1L, 2L, "B <-> C", 12L, 3F)
        graph.addEdge(2L, 3L, "C <-> D", 13L, 3F)
        graph.addEdge(3L, 4L, "D <-> E", 14L, 3F)
        graph.addEdge(2L, 7L, "C <-> H", 15L, 3F)
        graph.addEdge(0L, 5L, "A <-> F", 16L, 3F)

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
        viewModel.DrawBridges()

        viewModel.edges.forEach {
            when {
                it.u.ID == 2L && it.v.ID == 3L -> {
                    assert(it.width == 5F)
                    assert(it.color == CoolColors.Blue)
                }

                it.u.ID == 3L && it.v.ID == 4L -> {
                    assert(it.width == 5F)
                    assert(it.color == CoolColors.Blue)
                }

                else -> {
                    assert(it.width == 2F)
                    assert(it.color == CoolColors.DarkPurple)
                }
            }
        }
    }
}
