package view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import viewModel.VertexViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VertexView(
    viewModel: VertexViewModel,
    modifier: Modifier = Modifier,
) {
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
                    viewModel.radius += 2.dp
                    viewModel.color = Color.Red
                },
                onDragEnd =  {
                    viewModel.radius -= 2.dp
                    viewModel.color = Color.Gray
                }) { change, dragAmount,  ->
                change.consume()
                viewModel.onDrag(dragAmount)

            }
        }
        .onClick(true) {
            if(viewModel.color != Color.Red) {
                viewModel.radius += 2.dp
                viewModel.color = Color.Red
            }
            else {
                viewModel.radius -= 5.dp
                viewModel.color = Color.Gray
            }
        }
    ) {
        if (viewModel.labelVisible) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(0.dp, -viewModel.radius - 10.dp),
                text = viewModel.label,
            )
        }
    }
}