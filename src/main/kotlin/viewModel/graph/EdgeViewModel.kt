package viewModel.graph

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import model.abstractGraph.AbstractEdge

class EdgeViewModel (
    val u: VertexViewModel,
    val v: VertexViewModel,
    color: Color,
    width: Float = 2f,
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


    val labelVisible
        get() = _labelVisible.value

    internal val ID
        get() = e.id

    private var _width = mutableStateOf(width)
    var width: Float
        get() = _width.value
        set(value) {
            _width.value = value
        }

    private var _color = mutableStateOf(color)
    var color: Color
        get() = _color.value
        set(value) {
            _color.value = value
        }

}