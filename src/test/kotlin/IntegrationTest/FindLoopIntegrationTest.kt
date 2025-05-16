package IntegrationTest

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import model.Graph
import model.abstractGraph.AbstractVertex
import org.junit.jupiter.api.assertThrows
import view.components.CoolColors
import viewModel.graph.GraphViewModel

class FindLoopIntegrationTest {
    @Test
    fun testFindLoop() {
        val graph = Graph(true)
        val placement = mutableMapOf<AbstractVertex, Pair<Dp, Dp>>()
        for (i in 1L..4L) {
            placement.put(graph.addVertex(i, i.toString()), 0.dp to 0.dp)
        }
        graph.addEdge(1L, 2L, "1", 1L, 1f)
        graph.addEdge(2L, 3L, "2", 2L, 1f)
        graph.addEdge(3L, 4L, "3", 3L, 1f)
        graph.addEdge(4L, 1L, "4", 4L, 1f)
        val viewModel =
            GraphViewModel(
                graph,
                placement,
                mutableStateOf(false),
                mutableStateOf(false),
                mutableStateOf(false),
                mutableStateOf(false),
            )
        viewModel.idForLoop = "1"
        viewModel.findLoopForVertex()
        viewModel.edges.forEach {
            when {
                it.u.ID == 1L && it.v.ID == 2L -> {
                    assert(it.width == 20f)
                    assert(it.color == CoolColors.Bardo)
                }
                it.u.ID == 2L && it.v.ID == 3L -> {
                    assert(it.width == 20F)
                    assert(it.color == CoolColors.Bardo)
                }
                it.u.ID == 3L && it.v.ID == 4L -> {
                    assert(it.width == 20F)
                    assert(it.color == CoolColors.Bardo)
                }
                it.u.ID == 4L && it.v.ID == 1L -> {
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
    fun testVertexWithoutLoop() {
        val graph = Graph(true)
        val placement = mutableMapOf<AbstractVertex, Pair<Dp, Dp>>()
        for (i in 1L..4L) {
            placement.put(graph.addVertex(i, i.toString()), 0.dp to 0.dp)
        }
        graph.addEdge(1L, 2L, "1", 1L, 1f)
        graph.addEdge(2L, 3L, "2", 2L, 1f)
        graph.addEdge(3L, 4L, "3", 3L, 1f)
        val viewModel =
            GraphViewModel(
                graph,
                placement,
                mutableStateOf(false),
                mutableStateOf(false),
                mutableStateOf(false),
                mutableStateOf(false),
            )
        viewModel.idForLoop = "1"
        val exception = assertThrows<IllegalStateException> { viewModel.findLoopForVertex() }
        assert(
            exception.message?.contains("Graph does not contain any loop with this vertex") ?: false
        )
    }

    @Test
    fun testGraphDoesNotContainVertex() {
        val graph = Graph(true)
        val placement = mutableMapOf<AbstractVertex, Pair<Dp, Dp>>()
        for (i in 1L..4L) {
            placement.put(graph.addVertex(i, i.toString()), 0.dp to 0.dp)
        }
        graph.addEdge(1L, 2L, "1", 1L, 1f)
        graph.addEdge(2L, 3L, "2", 2L, 1f)
        graph.addEdge(3L, 4L, "3", 3L, 1f)
        val viewModel =
            GraphViewModel(
                graph,
                placement,
                mutableStateOf(false),
                mutableStateOf(false),
                mutableStateOf(false),
                mutableStateOf(false),
            )
        viewModel.idForLoop = "50"
        val exception = assertThrows<IllegalArgumentException> { viewModel.findLoopForVertex() }
        assert(exception.message?.contains("Graph does not contain vertex with id 50") ?: false)
    }
}
