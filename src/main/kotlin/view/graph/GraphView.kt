package view.graph

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import viewModel.graph.GraphViewModel

@Composable
fun GraphView(viewModel: GraphViewModel, scale: Float) {
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    if (viewModel.isOffsetCalculated) {
        offsetX = viewModel.calculatedOffsetX.value
        offsetY = viewModel.calculatedOffsetY.value
        viewModel.isOffsetCalculated = false
    }
    Box(
        modifier =
            Modifier.fillMaxSize()
                .pointerInput(scale) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount.x/scale
                        offsetY += dragAmount.y/scale
                    }
                }
                .graphicsLayer(scaleX = scale, scaleY = scale)
                .offset(offsetX.dp, offsetY.dp)
    ) {
        viewModel.vertices.forEach { v -> VertexView(v, Modifier) }
        viewModel.edges.forEach { e -> EdgeView(e, viewModel.isDirected) }
    }
}
