package viewModel

import androidx.compose.runtime.State
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import model.AbstractGraph
import model.AbstractVertex
import view.components.CoolColors
import kotlin.random.Random
import kotlin.random.nextInt

class GraphViewModel (
    private val graph: AbstractGraph,
    private val placement: Map<AbstractVertex, Pair<Dp, Dp>?>,
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

    internal val isDirected: Boolean
        get() = graph.isDirected
    internal val isWeighted: Boolean
        get() = graph.isWeighted

    private val _edges = graph.edges.associateWith { e ->
        val fst = _vertices[e.vertices.first]
            ?: throw IllegalStateException("VertexView for ${e.vertices.first} not found")
        val snd = _vertices[e.vertices.second]
            ?: throw IllegalStateException("VertexView for ${e.vertices.second} not found")
        EdgeViewModel(fst, snd, e, showEdgesWeights, showEdgesDirections)
    }

    val vertices: Collection<VertexViewModel>
        get() = _vertices.values

    val edges: Collection<EdgeViewModel>
        get() = _edges.values
}