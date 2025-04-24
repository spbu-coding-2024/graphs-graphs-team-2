package view.graph

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import viewModel.graph.GraphViewModel
import java.lang.Float.min
import kotlin.math.max


@Composable
fun GraphView(
    viewModel: GraphViewModel,

    scale: Float
) {
    var offsetX by remember{ mutableStateOf(5f) }
    var offsetY by remember{ mutableStateOf(5f) }
    var isLoading = true


    Box(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                change.consume()
                offsetX += dragAmount.x
                offsetY += dragAmount.y
            }
        }
        .offset(offsetX.dp, offsetY.dp)
        .graphicsLayer(
            scaleX = scale,
            scaleY = scale
        )
    ) {
        isLoading = false
        viewModel.vertices.forEach { v ->
            VertexView(v, Modifier)
        }
        viewModel.edges.forEach { e ->
            EdgeView(e, viewModel.isDirected)
        }
    }
    if(isLoading) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }
}