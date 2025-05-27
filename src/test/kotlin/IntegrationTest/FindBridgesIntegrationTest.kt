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

    @Test
    fun `findBridges paints the path`() {
        val graph = Graph(false, true)
        graph.addVertex(0, "A")
        graph.addVertex(1, "B")
        graph.addVertex(2, "C")
        graph.addVertex(3, "D")
        graph.addVertex(4, "E")
        graph.addVertex(5, "F")
        graph.addVertex(6, "G")
        graph.addVertex(7, "H")
        graph.addEdge(5, 6, "F <-> G", 8, 3F)
        graph.addEdge(6, 7, "G <-> H", 9, 3F)
        graph.addEdge(7, 5, "H <-> F", 10, 3F)
        graph.addEdge(0, 1, "A <-> B", 11, 3F)
        graph.addEdge(1, 2, "B <-> C", 12, 3F)
        graph.addEdge(2, 3, "C <-> D", 13, 3F)
        graph.addEdge(3, 4, "D <-> E", 14, 3F)
        graph.addEdge(2, 7, "C <-> H", 14, 3F)
        graph.addEdge(0, 5, "A <-> F", 14, 3F)

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
                it.u.ID == 0L && it.v.ID == 1L -> {
                    assert(it.width == 20F)
                    assert(it.color == CoolColors.Blue)
                }

                it.u.ID == 1L && it.v.ID == 2L -> {
                    assert(it.width == 20F)
                    assert(it.color == CoolColors.Blue)
                }

                it.u.ID == 2L && it.v.ID == 3L -> {
                    assert(it.width == 20F)
                    assert(it.color == CoolColors.Blue)
                }

                it.u.ID == 3L && it.v.ID == 4L -> {
                    assert(it.width == 20F)
                    assert(it.color == CoolColors.Blue)
                }

                else -> {
                    assert(it.width == 1F)
                    assert(it.color == CoolColors.Purple)
                }
            }
        }
    }
}
