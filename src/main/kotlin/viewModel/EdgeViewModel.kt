package viewModel

import androidx.compose.runtime.State
import model.Edge

class EdgeViewModel<E, V>(
    val u: VertexViewModel<V>,
    val v: VertexViewModel<V>,
    private val e: Edge<E, V>,
    private val _weightVisible: State<Boolean>,
    private val _directionVisible: State<Boolean>,
) {
    val weight
        get() = e.weight.toString()

    val weightVisible
        get() = _weightVisible.value

    val direciton
        get() = u to v

    val directionVisible
        get() = _directionVisible.value
}