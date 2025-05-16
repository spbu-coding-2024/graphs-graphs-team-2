package viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import model.Graph
import model.abstractGraph.AbstractVertex
import view.DataSystems
import viewModel.graph.GraphViewModel

class MainScreenViewModel(graph: Graph, placement: Map<AbstractVertex, Pair<Dp?, Dp?>?>) {
    private var _dataSystem = mutableStateOf<DataSystems?>(null)
    var dataSystem
        get() = _dataSystem.value
        set(value) {
            _dataSystem.value = value
        }

    private var _showErrorDialog = mutableStateOf(false)
    var showErrorDialog
        get() = _showErrorDialog.value
        set(value) {
            _showErrorDialog.value = value
        }

    private var _showInformationDialog = mutableStateOf(false)
    var showInformationDialog
        get() = _showInformationDialog.value
        set(value) {
            _showInformationDialog.value = value
        }

    private var _errorMessage = mutableStateOf("")
    var errorMessage
        get() = _errorMessage.value
        set(value) {
            _errorMessage.value = value
        }

    private var _isLoading = mutableStateOf(false)
    var isLoading: Boolean
        get() = _isLoading.value
        set(value) {
            _isLoading.value = value
        }

    private var _graphName: MutableState<String?> = mutableStateOf(null)
    var graphName
        get() = _graphName.value
        set(value) {
            _graphName.value = value
        }

    private var _showMenuState = mutableStateOf(false)
    var showMenuState
        get() = _showMenuState.value
        set(value) {
            _showMenuState.value = value
        }

    private var _saveMenuState = mutableStateOf(false)
    var saveMenuState
        get() = _saveMenuState.value
        set(value) {
            _saveMenuState.value = value
        }

    private var _openNewGraph = mutableStateOf(false)
    var openNewGraph
        get() = _openNewGraph.value
        set(value) {
            _openNewGraph.value = value
        }

    val showVerticesLabels = mutableStateOf(false)
    val showVerticesIds = mutableStateOf(false)
    val showEdgesWeights = mutableStateOf(false)
    val showEdgesLabels = mutableStateOf(false)

    val graphViewModel =
        GraphViewModel(
            graph,
            placement,
            showVerticesLabels,
            showVerticesIds,
            showEdgesWeights,
            showEdgesLabels,
        )
}
