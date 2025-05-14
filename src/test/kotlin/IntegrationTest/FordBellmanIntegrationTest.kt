package IntegrationTest

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import model.Graph
import model.abstractGraph.AbstractVertex
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import view.components.CoolColors
import viewModel.graph.GraphViewModel

class FordBellmanIntegrationTest {
    @Test
    fun findShortestWayTest() {
        val graph = Graph(true, true)
        val placement = mutableMapOf<AbstractVertex, Pair<Dp, Dp>>()
        for (i in 1L..6L) {
            placement.put(graph.addVertex(i, i.toString()), 0.dp to 0.dp)
        }
        graph.addEdge(1L, 2L, "1", 1, 2F)
        graph.addEdge(2L, 3L, "2", 2, 3F)
        graph.addEdge(3L, 6L, "3", 3, 4F)
        graph.addEdge(4L, 5L, "4", 4, 1F)
        graph.addEdge(5L, 6L, "5", 5, 7F)
        graph.addEdge(1L, 4L, "6", 6, 1F)
        graph.addEdge(2L, 5L, "7", 7, -10F)
        val viewModel =
            GraphViewModel(
                graph,
                placement,
                mutableStateOf(false),
                mutableStateOf(false),
                mutableStateOf(false),
                mutableStateOf(false),
            )
        viewModel.firstIDFB = "1"
        viewModel.secondIDFB = "6"
        viewModel.findPathByFordBellman()
        viewModel.edges.forEach {
            when {
                it.u.ID == 1L && it.v.ID == 2L -> {
                    assert(it.width == 20F)
                    assert(it.color == CoolColors.Bardo)
                }
                it.u.ID == 2L && it.v.ID == 5L -> {
                    assert(it.width == 20F)
                    assert(it.color == CoolColors.Bardo)
                }
                it.u.ID == 5L && it.v.ID == 6L -> {
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

    @Test
    fun testUnreachableEnd() {
        val graph = Graph(true, true)
        val placement = mutableMapOf<AbstractVertex, Pair<Dp, Dp>>()
        for (i in 1L..3L) {
            placement.put(graph.addVertex(i, i.toString()), 0.dp to 0.dp)
        }
        graph.addEdge(1L, 2L, "1", 1, -1F)
        graph.addEdge(2L, 1L, "2", 2, -1F)
        val viewModel =
            GraphViewModel(
                graph,
                placement,
                mutableStateOf(false),
                mutableStateOf(false),
                mutableStateOf(false),
                mutableStateOf(false),
            )
        viewModel.firstIDFB = "1"
        viewModel.secondIDFB = "3"

        val exception = assertThrows<IllegalStateException> { viewModel.findPathByFordBellman() }
        assert(exception.message?.contains("Path does not exist") ?: false)
        viewModel.edges.forEach {
            assert(it.width == 1F)
            assert(it.color == CoolColors.Purple)
        }
    }
}
