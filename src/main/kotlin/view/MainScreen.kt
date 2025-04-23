package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Checkbox
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import view.components.CoolColors
import view.graph.GraphView
import viewModel.MainScreenViewModel

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

        Surface(
            modifier = Modifier
                .weight(1f),
            color = CoolColors.DarkGray,
        ) {
            GraphView(viewModel.graphViewModel)
        }
    }
}