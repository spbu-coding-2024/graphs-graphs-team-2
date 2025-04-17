package viewModel

import androidx.compose.runtime.State
import model.AbstractEdge

class EdgeViewModel (
    val u: VertexViewModel,
    val v: VertexViewModel,
    private val e: AbstractEdge,
    private val _weightVisible: State<Boolean>,
    private val _directionVisible: State<Boolean>,
) {
    val weight
        get() = e.weight.toString()

    val label
        get() = e.label

    internal val ID
        get() = e.id

    val weightVisible
        get() = _weightVisible.value

    val direciton
        get() = u to v

    val directionVisible
        get() = _directionVisible.value
}