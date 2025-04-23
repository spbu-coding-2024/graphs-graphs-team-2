package view

import GraphScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ioNeo4j.ReadNeo4j
import io.ioNeo4j.WriteNeo4j
import view.algo.FindBridgesView
import view.components.CoolColors
import view.components.ErrorDialog
import view.components.PurpleButton
import view.graph.GraphView
import view.io.Neo4jView
import viewModel.MainScreenViewModel

@Composable
fun MainScreen(viewModel: MainScreenViewModel) {
    var dataSystem by remember { mutableStateOf<DataSystems?>(null) }
    val username: MutableState<String?> = remember { mutableStateOf(null) }
    val password: MutableState<String?> = remember { mutableStateOf(null) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.background(CoolColors.Gray)
    ) {
        Column(
            modifier = Modifier
                .width(370.dp)
                .background(CoolColors.Gray)
        ) {
            Row {
                Checkbox(
                    checked = viewModel.showVerticesLabels.value,
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
            PurpleButton(
                modifier = Modifier.clip(shape = RoundedCornerShape(35.dp)).weight(0.3f),
                onClick = { FindBridgesView(viewModel.graph, viewModel.graphViewModel).DrawBridges() },
                text = "FindBridges",
                fontSize = 75.sp,
                fontFamily = FontFamily.Monospace,
                textPadding = 10.dp
            )
            PurpleButton(
                modifier = Modifier.clip(shape = RoundedCornerShape(35.dp)).weight(0.3f),
                onClick = { dataSystem = DataSystems.Neo4j},
                text = "WriteNeo4j",
                fontSize = 75.sp,
                fontFamily = FontFamily.Monospace,
                textPadding = 10.dp
            )
        }

        Surface(
            modifier = Modifier
                .weight(1f),
            color = CoolColors.DarkGray,
        ) {
            GraphView(viewModel.graphViewModel)
        }
        if (dataSystem == DataSystems.Neo4j) {
            Neo4jView(username, password) { dataSystem = null }
            if (username.value != null && password.value != null) {
                try {
                    WriteNeo4j(username.value ?: "", password.value ?: "", viewModel.graphViewModel)
                    dataSystem = null
                    username.value = null
                    password.value = null
                } catch (e: Exception) {
                    errorMessage = e.message ?: "Error"
                    showErrorDialog = true
                    username.value = null
                    password.value = null
                    dataSystem = null
                }
            }
        }
        if (showErrorDialog) {
            ErrorDialog(errorMessage) { showErrorDialog = false }
        }
    }
}