package viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import model.Graph
import model.abstractGraph.AbstractVertex
import viewModel.graph.GraphViewModel

class MainScreenViewModel(val graph: Graph, placement: Map<AbstractVertex, Pair<Dp?, Dp?>?>) {

    val showVerticesLabels = mutableStateOf(false)
    val showEdgesWeights = mutableStateOf(false)
    val showEdgesLabels = mutableStateOf(false)

    val graphViewModel = GraphViewModel(graph, placement, showVerticesLabels, showEdgesWeights, showEdgesLabels)
}