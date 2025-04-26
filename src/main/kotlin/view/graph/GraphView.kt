package view.graph

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import viewModel.graph.GraphViewModel

@Composable
fun GraphView(viewModel: GraphViewModel, scale: Float) {

  var offsetX by remember { mutableStateOf(5f) }
  var offsetY by remember { mutableStateOf(5f) }

  Box(
      modifier =
          Modifier.fillMaxSize()
              .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                  change.consume()
                  offsetX += dragAmount.x
                  offsetY += dragAmount.y
                }
              }
              .offset(offsetX.dp, offsetY.dp)
              .graphicsLayer(scaleX = scale, scaleY = scale)) {
        viewModel.vertices.forEach { v -> VertexView(v, Modifier) }
        viewModel.edges.forEach { e -> EdgeView(e, viewModel.isDirected) }
      }
}
