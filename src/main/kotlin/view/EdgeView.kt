package view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import viewModel.EdgeViewModel
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun <E, V> EdgeView(
    viewModel: EdgeViewModel<E, V>,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        drawLine(
            start = Offset(
                viewModel.u.x.toPx(),
                viewModel.u.y.toPx(),
            ),
            end = Offset(
                viewModel.v.x.toPx(),
                viewModel.v.y.toPx(),
            ),
            color = Color.Black,
        )
        if (viewModel.directionVisible) {
            val startX = viewModel.u.x.toPx() + viewModel.u.radius.toPx()
            val startY = viewModel.u.y.toPx() + viewModel.u.radius.toPx()
            val endX = viewModel.v.x.toPx() + viewModel.v.radius.toPx()
            val endY = viewModel.v.y.toPx() + viewModel.v.radius.toPx()


            val arrowLength = viewModel.u.radius.toPx() / 2
            val arrowAngle = PI / 6


            val dx = endX - startX
            val dy = endY - startY
            val angle = atan2(dy, dx)
            val x1 = (endX - arrowLength * cos(angle - arrowAngle)).toFloat()
            val y1 = (endY - arrowLength * sin(angle - arrowAngle)).toFloat()
            val x2 = (endX - arrowLength * cos(angle + arrowAngle)).toFloat()
            val y2 = (endY - arrowLength * sin(angle + arrowAngle)).toFloat()


            drawPath(
                path = Path().apply {
                    moveTo(endX, endY)
                    lineTo(x1, y1)
                    lineTo(x2, y2)
                    close()
                },
                color = Color.Black
            )
        }
    }
    if (viewModel.weightVisible) {
        Text(
            modifier = Modifier
                .offset(
                    viewModel.u.x + (viewModel.v.x - viewModel.u.x) / 2,
                    viewModel.u.y + (viewModel.v.y - viewModel.u.y) / 2
                ),
            text = viewModel.weight,
        )
    }
}