package view.graph

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import view.components.CoolColors
import viewModel.graph.VertexViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VertexView(
    viewModel: VertexViewModel,
    modifier: Modifier = Modifier,
) {
    var dragBeginColor by remember { mutableStateOf(viewModel.color) }
    Box(modifier = modifier
        .size(viewModel.radius * 2, viewModel.radius * 2)
        .offset(viewModel.x, viewModel.y)
        .background(
            color = viewModel.color,
            shape = CircleShape
        )
        .pointerInput(viewModel) {
            detectDragGestures (
                onDragStart =  {
                    dragBeginColor = viewModel.color

                    viewModel.radius += 5.dp
                    viewModel.color = CoolColors.Pink
                },
                onDragEnd =  {
                    viewModel.radius -= 5.dp
                    viewModel.color = dragBeginColor
                }) { change, dragAmount ->
                change.consume()
                viewModel.onDrag(dragAmount)
            }
        }
    ) {
        if (viewModel.labelVisible) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(0.dp, -viewModel.radius - 10.dp),
                text = viewModel.label,
                color = viewModel.color,
            )
        }
        if (viewModel.idVisible) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(0.dp, viewModel.radius + 10.dp),
                text = viewModel.ID.toString(),
                color = viewModel.color,
            )
        }
    }
}