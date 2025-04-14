package viewModel

import androidx.compose.runtime.State
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import model.Graph
import model.Vertex
import view.components.CoolColors
import kotlin.random.Random
import kotlin.random.nextInt

class GraphViewModel<V, E> (
    private val graph: Graph<V, E>,
    private val placement: Map<Vertex<V>, Pair<Dp, Dp>?>,
    showVerticesLabels: State<Boolean>,
    showEdgesWeights: State<Boolean>,
    showEdgesDirections: State<Boolean>,
) {
    private val _vertices = graph.vertices.associateWith { v ->
        VertexViewModel(
            placement[v]?.first ?: Random.nextInt(0..800).dp,
            placement[v]?.first ?: Random.nextInt(0..600).dp,
            CoolColors.DarkPurple,
            v,
            showVerticesLabels,
        )
    }
    private val _edges = graph.edges.associateWith { e ->
        val fst = _vertices[e.vertices.first]
            ?: throw IllegalStateException("VertexView for ${e.vertices.first} not found")
        val snd = _vertices[e.vertices.second]
            ?: throw IllegalStateException("VertexView for ${e.vertices.second} not found")
        EdgeViewModel(fst, snd, e, showEdgesWeights, showEdgesDirections)
    }

    val vertices: Collection<VertexViewModel<V>>
        get() = _vertices.values

    val edges: Collection<EdgeViewModel<E, V>>
        get() = _edges.values
}