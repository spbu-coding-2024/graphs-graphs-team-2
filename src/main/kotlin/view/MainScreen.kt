package view

import WelcomeScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.ioNeo4j.WriteNeo4j
import kotlin.math.max
import kotlin.math.min
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.exposed.exceptions.ExposedSQLException
import view.components.CoolColors
import view.components.ErrorDialog
import view.components.InvertPurpleButton
import view.components.PurpleButton
import view.graph.GraphView
import view.io.JsonView
import view.io.Neo4jView
import view.io.SQLiteNameInputView
import viewModel.MainScreenViewModel
import viewModel.SearchScreenSQlite.SQLiteSearchScreenViewModel
import viewModel.graph.GraphViewModel
import viewModel.placement.place

@Composable
fun MainScreen(viewModel: MainScreenViewModel) {
    var dataSystem by remember { mutableStateOf<DataSystems?>(null) }
    val username: MutableState<String?> = remember { mutableStateOf(null) }
    val password: MutableState<String?> = remember { mutableStateOf(null) }
    val graphName: MutableState<String?> = remember { mutableStateOf(null) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var firstIdDijkstra by remember { mutableStateOf("") }
    var secondIdDijkstra by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope { Dispatchers.Default }
    var openNewGraph by remember { mutableStateOf(false) }
    val navigator = LocalNavigator.currentOrThrow

    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.background(CoolColors.Gray),
    ) {
        var scale by remember { mutableStateOf(calculateScale(viewModel.graphViewModel)) }
        Column(
            modifier =
                Modifier.width(370.dp)
                    .background(CoolColors.Gray)
                    .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row {
                Checkbox(
                    checked = viewModel.showVerticesLabels.value,
                    onCheckedChange = { viewModel.showVerticesLabels.value = it },
                )
                Text(
                    "Show vertices labels",
                    fontSize = 28.sp,
                    modifier = Modifier.padding(4.dp),
                    color = CoolColors.Purple,
                )
            }

            Row {
                Checkbox(
                    checked = viewModel.showEdgesLabels.value,
                    onCheckedChange = { viewModel.showEdgesLabels.value = it },
                )
                Text(
                    "Show edges labels",
                    fontSize = 28.sp,
                    modifier = Modifier.padding(4.dp),
                    color = CoolColors.Purple,
                )
            }
            Row {
                Checkbox(
                    checked = viewModel.showVerticesIds.value,
                    onCheckedChange = { viewModel.showVerticesIds.value = it },
                )
                Text(
                    "Show vertices ids",
                    fontSize = 28.sp,
                    modifier = Modifier.padding(4.dp),
                    color = CoolColors.Purple,
                )
            }
            if (viewModel.graphViewModel.isWeighted) {
                Row {
                    Checkbox(
                        checked = viewModel.showEdgesWeights.value,
                        onCheckedChange = { viewModel.showEdgesWeights.value = it },
                    )
                    Text(
                        "Show edges weights",
                        fontSize = 28.sp,
                        modifier = Modifier.padding(4.dp),
                        color = CoolColors.Purple,
                    )
                }
            }
            PurpleButton(
                modifier =
                    Modifier.clip(shape = RoundedCornerShape(15.dp))
                        .height(65.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 7.dp),
                onClick = {
                    scope.launch {
                        place(800.0, 600.0, viewModel.graphViewModel)
                        scale = calculateScale(viewModel.graphViewModel)
                    }
                },
                text = "Placement",
                fontSize = 28.sp,
                fontFamily = FontFamily.Monospace,
                textPadding = 3.dp,
            )
            PurpleButton(
                modifier =
                    Modifier.clip(shape = RoundedCornerShape(15.dp))
                        .height(65.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 7.dp),
                onClick = { scope.launch { viewModel.graphViewModel.findKeyVertices() } },
                text = "Find key vertices",
                fontSize = 28.sp,
                fontFamily = FontFamily.Monospace,
                textPadding = 3.dp,
            )
            if (!viewModel.graphViewModel.isDirected) {
                PurpleButton(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(15.dp))
                        .height(65.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 7.dp),
                    onClick = { viewModel.graphViewModel.DrawBridges() },
                    text = "Find Bridges",
                    fontSize = 28.sp,
                    fontFamily = FontFamily.Monospace,
                    textPadding = 3.dp,
                )
            }
            if (!viewModel.graphViewModel.isDirected && viewModel.graphViewModel.isWeighted) {
                PurpleButton(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(15.dp))
                        .height(65.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 7.dp),

                    onClick = {
                        scope.launch {
                            viewModel.graphViewModel.minimalSpanningTree()
                        }
                    },
                    text = "Min spanning tree",
                    fontSize = 28.sp,
                    fontFamily = FontFamily.Monospace,
                    textPadding = 3.dp
                )
            }
            PurpleButton(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(15.dp))
                    .height(65.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 7.dp),
                onClick = {
                    scope.launch {
                        viewModel.graphViewModel.highlightComponents()
                    }
                },
                text = "Find components",
                fontSize = 28.sp,
                fontFamily = FontFamily.Monospace,
                textPadding = 3.dp,
            )
            if (!viewModel.graphViewModel.isDirected) {
                PurpleButton(
                    modifier =
                        Modifier.clip(shape = RoundedCornerShape(15.dp))
                            .height(65.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 7.dp),
                    onClick = { viewModel.graphViewModel.DrawBridges() },
                    text = "Find Bridges",
                    fontSize = 28.sp,
                    fontFamily = FontFamily.Monospace,
                    textPadding = 3.dp
                )
            }

            if (viewModel.graphViewModel.isDirected) {
                Row {
                    OutlinedTextField(
                        firstIdDijkstra,
                        { firstIdDijkstra = it },
                        textStyle = TextStyle(fontSize = 28.sp, color = CoolColors.DarkPurple),
                        modifier = Modifier.width(90.dp).height(65.dp),
                    )
                    OutlinedTextField(
                        secondIdDijkstra,
                        { secondIdDijkstra = it },
                        textStyle = TextStyle(fontSize = 28.sp, color = CoolColors.DarkPurple),
                        modifier = Modifier.width(90.dp).height(65.dp),
                    )
                    PurpleButton(
                        modifier =
                            Modifier.clip(shape = RoundedCornerShape(15.dp))
                                .height(65.dp)
                                .fillMaxSize()
                                .padding(horizontal = 7.dp),
                        onClick = {
                            try {
                                viewModel.graphViewModel.Dijkstra(firstIdDijkstra, secondIdDijkstra)
                            } catch (e: Exception) {
                                errorMessage = e.message ?: "Graph is built incorrectly"
                                showErrorDialog = true
                                firstIdDijkstra = ""
                                secondIdDijkstra = ""
                            }
                        },
                        text = "Dijkstra",
                        fontSize = 28.sp,
                        fontFamily = FontFamily.Monospace,
                        textPadding = 3.dp,
                    )
                }
            }
            PurpleButton(
                modifier =
                    Modifier.clip(shape = RoundedCornerShape(15.dp))
                        .height(65.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 7.dp),
                onClick = { scope.launch { viewModel.graphViewModel.resetView() } },
                text = "Reset view",
                fontSize = 28.sp,
                fontFamily = FontFamily.Monospace,
                textPadding = 3.dp,
            )
            PurpleButton(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(15.dp))
                    .height(65.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 7.dp),
                onClick = { dataSystem = DataSystems.Neo4j },
                text = "Save to Neo4j",
                fontSize = 28.sp,
                fontFamily = FontFamily.Monospace,
                textPadding = 3.dp,
            )
            PurpleButton(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(15.dp))
                    .height(65.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 7.dp),
                onClick = { dataSystem = DataSystems.JSON },
                text = "Save to JSON",
                fontSize = 28.sp,
                fontFamily = FontFamily.Monospace,
                textPadding = 3.dp,
            )
            PurpleButton(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(15.dp))
                    .height(65.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 7.dp),
                onClick = { dataSystem = DataSystems.SQLite },
                text = "Save to SQLite",
                fontSize = 28.sp,
                fontFamily = FontFamily.Monospace,
                textPadding = 3.dp,
            )
            InvertPurpleButton(
                modifier =
                    Modifier.clip(shape = RoundedCornerShape(15.dp))
                        .height(65.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 7.dp),
                onClick = { openNewGraph = true },
                text = "Open new graph",
                fontSize = 28.sp,
                fontFamily = FontFamily.Monospace,
                textPadding = 3.dp,
            )
        }

        Surface(
            modifier =
                Modifier.weight(1f)
                    .scrollable(
                        orientation = Orientation.Vertical,
                        state =
                            rememberScrollableState { delta ->
                                scale *= 1f + delta / 500
                                scale = scale.coerceIn(0.000001f, 100f)
                                delta
                            },
                    ),
            color = CoolColors.DarkGray,
        ) {
            GraphView(viewModel.graphViewModel, scale)
        }

        if (dataSystem == DataSystems.JSON) {
            val fileChooser = JsonView()
            try {
                fileChooser.storeToJson(viewModel.graphViewModel) { dataSystem = null }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Unknown error"
                showErrorDialog = true
                dataSystem = null
            }
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
                    errorMessage = e.message ?: "Unknown error"
                    showErrorDialog = true
                    username.value = null
                    password.value = null
                    dataSystem = null
                }
            }
        }
        if (dataSystem == DataSystems.SQLite) {
            SQLiteNameInputView(graphName) { dataSystem = null }
            if (graphName.value != null) {
                try {
                    SQLiteSearchScreenViewModel()
                        .writeGraph(viewModel.graphViewModel, graphName.value ?: "")
                    dataSystem = null
                    graphName.value = null
                } catch (e: Exception) {
                    errorMessage = e.message ?: "Unknown error"
                    showErrorDialog = true
                    graphName.value = null
                    dataSystem = null
                }
            }
        }
        if (openNewGraph) {
            navigator.push(WelcomeScreen)
        }
        if (showErrorDialog) {
            ErrorDialog(errorMessage) { showErrorDialog = false }
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
    val scaleX = 800f / (maxX - minX)
    val scaleY = 600f / (maxY - minY)

    return min(scaleY, scaleX)
}
