package view

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Checkbox
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import view.components.CoolColors
import view.graph.GraphView
import viewModel.MainScreenViewModel
import viewModel.graph.GraphViewModel
import java.lang.Float.min
import kotlin.math.max

@Composable
fun MainScreen(viewModel: MainScreenViewModel) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.background(CoolColors.Gray)
    ) {
        Column(modifier = Modifier
            .width(370.dp)
            .background(CoolColors.Gray)
        ) {
            Row {
                Checkbox(checked = viewModel.showVerticesLabels.value,
                    onCheckedChange = { viewModel.showVerticesLabels.value = it })
                Text(
                    "Show vertices labels",
                    fontSize = 28.sp,
                    modifier = Modifier.padding(4.dp),
                    color = CoolColors.Purple
                )
            }
            Row {
                Checkbox(checked = viewModel.showEdgesLabels.value, onCheckedChange = {
                    viewModel.showEdgesLabels.value = it
                })
                Text(
                    "Show edges labels",
                    fontSize = 28.sp,
                    modifier = Modifier.padding(4.dp),
                    color = CoolColors.Purple
                )
            }
        }

        var scale by remember { mutableStateOf(calculateScale(viewModel.graphViewModel)) }
        Surface(
            modifier = Modifier
                .weight(1f)
                .scrollable(orientation = Orientation.Vertical, state = rememberScrollableState { delta ->
                    scale *= 1f + delta / 400
                    scale = scale.coerceIn(0.000001f, 100f)
                    delta
                }),
            color = CoolColors.DarkGray,
        ) {
            GraphView(viewModel.graphViewModel, scale)
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

    return min(scaleY, scaleX)
}