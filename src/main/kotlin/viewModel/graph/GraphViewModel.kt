package viewModel.graph

import algo.AlgoBridges
import algo.AlgoDijkstra

import algo.Components
import algo.SpanningTree
import algo.louvain

import algo.FordBellman
import algo.HarmonicCentrality
import algo.PrimSpanningTree
import algo.StronglyConnectedComponents

import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.random.Random
import kotlin.random.nextInt
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import model.Graph
import model.abstractGraph.AbstractVertex
import view.components.CoolColors

class GraphViewModel(
    private val graph: Graph,
    private val placement: Map<AbstractVertex, Pair<Dp?, Dp?>?>,
    showVerticesLabels: State<Boolean>,
    showVerticesIds: State<Boolean>,
    showEdgesWeights: State<Boolean>,
    showEdgesLabels: State<Boolean>,
) {

    private val _vertices =
        graph.vertices.associate { v ->
            v.id to
                VertexViewModel(
                    placement[v]?.first ?: Random.nextInt(0..800).dp,
                    placement[v]?.second ?: Random.nextInt(0..600).dp,
                    CoolColors.DarkPurple,
                    v,
                    showVerticesLabels,
                    showVerticesIds,
                )
        }

    internal val isDirected: Boolean
        get() = graph.isDirected

    internal val isWeighted: Boolean
        get() = graph.isWeighted

    private val _edges =
        graph.edges.associate { e ->
            val fst =
                _vertices[e.vertices.first.id]
                    ?: throw IllegalStateException("VertexView for ${e.vertices.first} not found")
            val snd =
                _vertices[e.vertices.second.id]
                    ?: throw IllegalStateException("VertexView for ${e.vertices.second} not found")
            fst.ID to
                snd.ID to
                EdgeViewModel(
                    fst,
                    snd,
                    CoolColors.DarkPurple,
                    2f,
                    e,
                    showEdgesWeights,
                    showEdgesLabels,
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
            val edges = Pair(way[i], way[i + 1]) to Pair(way[i + 1], way[i])
            _edges[edges.first]?.color = CoolColors.Bardo
            _edges[edges.first]?.width = 5f
            _edges[edges.second]?.color = CoolColors.Bardo
            _edges[edges.second]?.width = 5f
        }
    }

    suspend fun findKeyVertices() {
        coroutineScope {
            resetSizes()
            val harmonicCentrality = HarmonicCentrality(graph)
            vertices.forEach {
                val centrality = async { harmonicCentrality.getVertexCentrality(it.ID) }
                it.radius *= (1 + centrality.await() / 4)
                it.color =
                    Color(
                        red = it.color.red * (1 + centrality.await() / 4),
                        blue = it.color.blue * (1 + centrality.await() / 4),
                        green = it.color.green * (1 + centrality.await() / 4),
                    )
            }
        }
    }

    fun Louvain() {
        val result = louvain(graph)
        val colours = result.first.values.associateWith { CoolColors.RandomColor }
        result.first.forEach { community ->
            _vertices[community.key]?.color = colours[community.value] ?: CoolColors.DarkPurple
        }
        result.second.forEach { community ->
            _edges[community.key]?.color = colours[community.value] ?: CoolColors.DarkPurple
        }
    }

    suspend fun minimalSpanningTree() {
        coroutineScope {
            launch {
                val minimalSpanning = async { PrimSpanningTree(graph).minimalTree }
                for (edge in minimalSpanning.await()) {
                    _vertices[edge.first]?.color = CoolColors.Blue
                    _vertices[edge.second]?.color = CoolColors.Blue
                    _edges[edge]?.color = CoolColors.Blue
                    _edges[edge.second to edge.first]?.color = CoolColors.Blue
                }
            }
        }
    }

    suspend fun highlightComponents() {
        coroutineScope {
            launch {
                val strongComponents = async { StronglyConnectedComponents(graph).components }
                for (community in strongComponents.await()) {
                    val color = CoolColors.RandomColor
                    for (vertexId in community) {
                        _vertices[vertexId]?.color = color
                    }
                }
            }
        }
    }

    fun findPathByFordBellman(firstVId: String, secondVId: String) {
        resetColors()
        val firstId = firstVId.toLong()
        val secondId = secondVId.toLong()
        if (_vertices[firstId] == null || _vertices[secondId] == null) {
            throw IllegalArgumentException("No such vertexes in graph")
        }
        val algoFB = FordBellman(graph, firstId, secondId)
        val flag = algoFB.FordBellman()
        if (!flag) {
            return
        }
        val way =
            try {
                algoFB.pathFromStartToEnd
            } catch (e: Exception) {
                throw e
            }
        for (el in way) {
            val revEdge = Pair(el.second, el.first)
            _edges[el]?.color = CoolColors.Bardo
            _edges[el]?.width = 5f
            _edges[revEdge]?.color = CoolColors.Bardo
            _edges[revEdge]?.width = 20f
        }
    }


    fun resetView() {
        resetColors()
        resetSizes()
        resetCords()
    }

    private fun resetColors() {
        edges.onEach { it.color = CoolColors.Purple }
        vertices.onEach { it.color = CoolColors.Purple }
    }

    private fun resetSizes() {
        vertices.onEach { it.radius = 25.dp }
        edges.onEach { it.width = 1f }
    }

    private fun resetCords() {

        graph.vertices.onEach {
            _vertices[it.id]?.x = placement[it]?.first ?: Random.nextInt(0..800).dp
            _vertices[it.id]?.y = placement[it]?.second ?: Random.nextInt(0..800).dp
        }
    }
}
