package viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import model.Graph
import model.abstractGraph.AbstractVertex
import viewModel.graph.GraphViewModel
import viewModel.placement.place

class MainScreenViewModel(val graph: Graph, placement: Map<AbstractVertex, Pair<Dp?, Dp?>?>) {

    val showVerticesLabels = mutableStateOf(false)
    val showEdgesWeights = mutableStateOf(false)
    val showEdgesLabels = mutableStateOf(false)

    val graphViewModel = GraphViewModel(graph, placement, showVerticesLabels, showEdgesWeights, showEdgesLabels)
    val isLoading = mutableStateOf(true)
    init{
        val scope = CoroutineScope(Dispatchers.Default)
        scope.launch {
            isLoading.value = true
            place(800.0, 600.0, graphViewModel)
            isLoading.value = false
        }
    }
}