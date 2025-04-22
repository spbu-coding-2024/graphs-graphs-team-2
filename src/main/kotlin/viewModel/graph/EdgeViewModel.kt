package viewModel.graph

import androidx.compose.runtime.State
import model.abstractGraph.AbstractEdge

class EdgeViewModel (
    val u: VertexViewModel,
    val v: VertexViewModel,
    private val e: AbstractEdge,
    private val _weightVisible: State<Boolean>,
    private val _labelVisible: State<Boolean>,
) {
    val weight
        get() = e.weight.toString()

    val weightVisible
        get() = _weightVisible.value

    val label
        get() = e.label

    val labelVisibile
        get() = _labelVisible.value

    internal val ID
        get() = e.id

    val direciton
        get() = u to v
}