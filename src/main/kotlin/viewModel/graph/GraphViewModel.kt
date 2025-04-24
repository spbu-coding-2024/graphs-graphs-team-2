package viewModel.graph

import algo.AlgoBridges
import algo.AlgoDijkstra
import algo.Components
import androidx.compose.runtime.State
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import model.Graph
import model.abstractGraph.AbstractGraph
import model.abstractGraph.AbstractVertex
import org.objectweb.asm.Label
import view.components.CoolColors
import viewModel.graph.VertexViewModel
import kotlin.random.Random
import kotlin.random.nextInt

class GraphViewModel(
    private val graph: Graph,
    private val placement: Map<AbstractVertex, Pair<Dp?, Dp?>?>,
    showVerticesLabels: State<Boolean>,
    showEdgesWeights: State<Boolean>,
    showEdgesLabels: State<Boolean>,
) {
    private val _vertices = graph.vertices.associate { v ->
        v.id to
                VertexViewModel(
                    placement[v]?.first ?: Random.Default.nextInt(0..800).dp,
                    placement[v]?.first ?: Random.Default.nextInt(0..600).dp,
                    CoolColors.DarkPurple,
                    v,
                    showVerticesLabels,
                )
    }

    internal val isDirected: Boolean
        get() = graph.isDirected
    internal val isWeighted: Boolean
        get() = graph.isWeighted

    private val _edges = graph.edges.associate { e ->
        val fst = _vertices[e.vertices.first.id]
            ?: throw IllegalStateException("VertexView for ${e.vertices.first} not found")
        val snd = _vertices[e.vertices.second.id]
            ?: throw IllegalStateException("VertexView for ${e.vertices.second} not found")
        fst.ID to snd.ID to
                EdgeViewModel(
                    fst, snd,
                    CoolColors.DarkPurple,
                    2f,
                    e,
                    showEdgesWeights,
                    showEdgesLabels
                )
    }

    val vertices: Collection<VertexViewModel>
        get() = _vertices.values

    val edges: Collection<EdgeViewModel>
        get() = _edges.values

    fun DrawBridges() {
        val algoBridges = AlgoBridges(graph)
        algoBridges.findBridges()
        algoBridges.bridges.forEach { bridge ->
            _edges[bridge]?.color = CoolColors.Blue
            _edges[bridge]?.width = 5f
        }
    }

    fun Dijkstra(firstVId: String, secondVId: String) {
        val firstId = firstVId.toLong()
        val secondId = secondVId.toLong()
        if (_vertices[firstId] == null || _vertices[secondId] == null) {
            throw IllegalArgumentException("No such vertexes in graph")
        }
        val algoDijkstra = AlgoDijkstra(graph, firstId, secondId)
        algoDijkstra.dijkstra(firstId)
        val way = algoDijkstra.way
        for (i in 0..way.size - 2) {
            val edge = Pair(way[i], way[i + 1])
            _edges[edge]?.color = CoolColors.Bardo
            _edges[edge]?.width = 5f
        }
    }

    fun highlightComponents() {
        val strongComponents = Components(graph).components
        for ((_, community) in strongComponents.withIndex()) {
            val color = CoolColors.RandomColor
            for (vertexId in community) {
                _vertices[vertexId]?.color = color
            }
        }
    }
}