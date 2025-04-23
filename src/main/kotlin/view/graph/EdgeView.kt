package view.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import viewModel.graph.EdgeViewModel
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun EdgeView(
    viewModel: EdgeViewModel,
    isDirect: Boolean,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val x1 = mutableStateOf(viewModel.u.x.toPx())
        val x2 = mutableStateOf(viewModel.v.x.toPx())
        val y1 = mutableStateOf(viewModel.u.y.toPx())
        val y2 = mutableStateOf(viewModel.v.y.toPx())
        val r1 = mutableStateOf(viewModel.u.radius.toPx())
        val r2 = mutableStateOf(viewModel.v.radius.toPx())

        val startX = x1.value + r1.value
        val startY = y1.value + r1.value
        val endX = x2.value + r2.value
        val endY = y2.value + r2.value

        val dx = endX - startX
        val dy = endY - startY
        val angle = atan2(dy, dx)
        val firstX = startX + r1.value * cos(angle)
        val firstY = startY + r1.value * sin(angle)
        val indentX = endX - r2.value * cos(angle)
        val indentY = endY - r2.value * sin(angle)

        drawLine(
            start = Offset(
                firstX,
                firstY,
            ),
            end = Offset(
                indentX,
                indentY,
            ),
            color = viewModel.color,
        )
        if(isDirect) {
            val arrowLength = r1.value / 2
            val arrowAngle = PI / 6

            val arrowBeginX = (indentX - arrowLength * cos(angle - arrowAngle)).toFloat()
            val arrowBeginY = (indentY - arrowLength * sin(angle - arrowAngle)).toFloat()
            val arrowEndX = (indentX - arrowLength * cos(angle + arrowAngle)).toFloat()
            val arrowEndY = (indentY - arrowLength * sin(angle + arrowAngle)).toFloat()
            drawPath(
                path = Path().apply {
                    moveTo(indentX, indentY)
                    lineTo(arrowBeginX, arrowBeginY)
                    lineTo(arrowEndX, arrowEndY)
                    close()
                },
                color = viewModel.color,
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
            color = viewModel.color,
        )
    }
    if (viewModel.labelVisibile) {
        Text(
            modifier = Modifier
                .offset(
                    viewModel.u.x + (viewModel.v.x - viewModel.u.x) / 2,
                    viewModel.u.y + (viewModel.v.y - viewModel.u.y) / 2
                ),
            text = viewModel.label,
            color = viewModel.color
        )
    }
}