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
import java.lang.Float.min
import kotlin.math.max

@Composable
fun GraphView(
    viewModel: GraphViewModel,
) {
    var offsetX by remember{ mutableStateOf(5f) }
    var offsetY by remember{ mutableStateOf(5f) }
    var scale by remember { mutableStateOf(calculateScale(viewModel)) }

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
        .pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    val scroll = event.changes.firstOrNull()?.scrollDelta?.y ?: 0f
                    if(scroll != 0f) {
                        val zoomF = if(scroll > 0) 0.9f else 1.1f
                        scale *= zoomF
                        scale=scale.coerceIn(0.000001f, 100f)
                    }
                }
            }
        }
        .graphicsLayer(
            scaleX = scale,
            scaleY = scale
        )
    ) {
        viewModel.vertices.forEach { v ->
            VertexView(v, Modifier)
        }
        viewModel.edges.forEach { e ->
            EdgeView(e, viewModel.isDirected)
        }
    }
}


fun calculateScale(graph: GraphViewModel): Float {
    var minX = 0f
    var minY = 0f
    var maxX = 0f
    var maxY = 0f

    for (v in graph.vertices) {
        minX = min(v.x.value, minX)
        minY = min(v.y.value, minY)
        maxX = max(v.x.value, maxX)
        maxY = max(v.y.value, maxY)
    }
    val scaleX = 800f/(maxX - minX)
    val scaleY = 600f/(maxY - minY)

    return 5 * min(scaleY, scaleX)
}