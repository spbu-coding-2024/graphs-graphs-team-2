package viewModel.io

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import io.ioNeo4j.ReadNeo4j
import io.ioNeo4j.WriteNeo4j
import model.Graph
import model.abstractGraph.AbstractVertex
import viewModel.graph.GraphViewModel

class Neo4jViewModel() {
    val username = mutableStateOf("")
    val password = mutableStateOf("")
    val passwordVisible = mutableStateOf(false)
    val errorMessage = mutableStateOf("")
    val showErrorDialog = mutableStateOf(false)

    fun read(): Pair<Graph, Map<AbstractVertex, Pair<Dp?, Dp?>?>>? {
        try {
            return ReadNeo4j(username.value, password.value)
        } catch (e: Exception) {
            errorMessage.value = e.message ?: "Error"
            showErrorDialog.value = true
            return null
        }
    }

    fun write(graphViewModel: GraphViewModel?): Boolean {
        try {
            WriteNeo4j(
                username.value,
                password.value,
                graphViewModel ?: throw IllegalArgumentException("no graph for write"),
            )
            return true
        } catch (e: Exception) {
            errorMessage.value = e.message ?: "Error"
            showErrorDialog.value = true
            return false
        }
    }
}
