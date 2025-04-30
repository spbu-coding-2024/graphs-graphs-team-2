package view

import WelcomeScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.ioNeo4j.WriteNeo4j
import kotlin.math.max
import kotlin.math.min
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.skiko.Cursor
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
    var showMenuState by remember { mutableStateOf(false) }
    var saveMenuState by remember { mutableStateOf(false) }

    var firstIdDijkstra by remember { mutableStateOf("") }
    var secondIdDijkstra by remember { mutableStateOf("") }
    var firstIDFB by remember { mutableStateOf("") }
    var secondIDFB by remember { mutableStateOf("") }
    var idforLooop by remember { mutableStateOf("") }
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
            Button(
                modifier =
                    Modifier.clip(shape = RoundedCornerShape(15.dp))
                        .height(52.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 7.dp)
                        .pointerHoverIcon(
                            PointerIcon(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR))
                        ),
                onClick = { showMenuState = true },
                colors =
                    ButtonDefaults.textButtonColors(
                        backgroundColor = CoolColors.Purple,
                        contentColor = CoolColors.DarkGray,
                    ),
            ) {
                Text(
                    modifier = Modifier.padding(start = 70.dp),
                    text = "Show all...",
                    fontSize = 24.sp,
                    fontFamily = FontFamily.Monospace,
                    color = CoolColors.DarkGray,
                    style = TextStyle(textGeometricTransform = TextGeometricTransform(0.3f, 0.3f)),
                )
                Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.rotate(if (showMenuState) 180f else 0f),
                    )
                }
            }
            DropdownMenu(
                modifier = Modifier.background(CoolColors.Gray),
                expanded = showMenuState,
                onDismissRequest = { showMenuState = false },
                offset = DpOffset(137.dp, (-1800).dp),
            ) {
                DropdownMenuItem(
                    onClick = {
                        viewModel.showVerticesLabels.value = !viewModel.showVerticesLabels.value
                    },
                    Modifier.height(45.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 3.dp)
                        .background(
                            if (viewModel.showVerticesLabels.value) CoolColors.DarkPurple
                            else CoolColors.Purple
                        )
                        .border(width = 1.dp, color = CoolColors.Gray),
                ) {
                    Text(
                        "Vertices labels",
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Monospace,
                        color = CoolColors.DarkGray,
                    )
                    if (viewModel.showVerticesLabels.value) {
                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.width(15.dp),
                            )
                        }
                    }
                }
                DropdownMenuItem(
                    onClick = {
                        viewModel.showEdgesLabels.value = !viewModel.showEdgesLabels.value
                    },
                    Modifier.height(45.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 3.dp)
                        .background(
                            if (viewModel.showEdgesLabels.value) CoolColors.DarkPurple
                            else CoolColors.Purple
                        )
                        .border(width = 1.dp, color = CoolColors.Gray),
                ) {
                    Text(
                        "Edges labels",
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Monospace,
                        color = CoolColors.DarkGray,
                    )
                    if (viewModel.showEdgesLabels.value) {
                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.width(15.dp),
                            )
                        }
                    }
                }
                DropdownMenuItem(
                    onClick = {
                        viewModel.showVerticesIds.value = !viewModel.showVerticesIds.value
                    },
                    Modifier.height(45.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 3.dp)
                        .background(
                            if (viewModel.showVerticesIds.value) CoolColors.DarkPurple
                            else CoolColors.Purple
                        )
                        .border(width = 1.dp, color = CoolColors.Gray),
                ) {
                    Text(
                        "Vertices IDs",
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Monospace,
                        color = CoolColors.DarkGray,
                    )
                    if (viewModel.showVerticesIds.value) {
                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Icon(
                                Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.width(15.dp),
                            )
                        }
                    }
                }
                if (viewModel.graphViewModel.isWeighted) {
                    DropdownMenuItem(
                        onClick = {
                            viewModel.showEdgesWeights.value = !viewModel.showEdgesWeights.value
                        },
                        Modifier.height(45.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 3.dp)
                            .background(
                                if (viewModel.showEdgesWeights.value) CoolColors.DarkPurple
                                else CoolColors.Purple
                            )
                            .border(width = 1.dp, color = CoolColors.Gray),
                    ) {
                        Text(
                            "Edges weights",
                            fontSize = 20.sp,
                            fontFamily = FontFamily.Monospace,
                            color = CoolColors.DarkGray,
                        )
                        if (viewModel.showEdgesWeights.value) {
                            Column(
                                horizontalAlignment = Alignment.End,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Icon(
                                    Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier.width(15.dp),
                                )
                            }
                        }
                    }
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
            PurpleButton(
                modifier =
                    Modifier.clip(shape = RoundedCornerShape(15.dp))
                        .height(65.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 7.dp),
                onClick = { viewModel.graphViewModel.Louvain() },
                text = "Find Communities",
                fontSize = 28.sp,
                fontFamily = FontFamily.Monospace,
                textPadding = 3.dp,
            )
            if (!viewModel.graphViewModel.isDirected && viewModel.graphViewModel.isWeighted) {
                PurpleButton(
                    modifier =
                        Modifier.clip(shape = RoundedCornerShape(15.dp))
                            .height(65.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 7.dp),
                    onClick = { scope.launch { viewModel.graphViewModel.minimalSpanningTree() } },
                    text = "Min spanning tree",
                    fontSize = 28.sp,
                    fontFamily = FontFamily.Monospace,
                    textPadding = 3.dp,
                )
            }
            if (viewModel.graphViewModel.isDirected) {
                PurpleButton(
                    modifier =
                        Modifier.clip(shape = RoundedCornerShape(15.dp))
                            .height(65.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 7.dp),
                    onClick = { scope.launch { viewModel.graphViewModel.highlightComponents() } },
                    text = "Find components",
                    fontSize = 28.sp,
                    fontFamily = FontFamily.Monospace,
                    textPadding = 3.dp,
                )
            }
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
                    textPadding = 3.dp,
                )
            }

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
            Row {
                OutlinedTextField(
                    firstIDFB,
                    { firstIDFB = it },
                    textStyle = TextStyle(fontSize = 28.sp, color = CoolColors.DarkPurple),
                    modifier = Modifier.width(90.dp).height(65.dp),
                )
                OutlinedTextField(
                    secondIDFB,
                    { secondIDFB = it },
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
                            scope.launch {
                                viewModel.graphViewModel.findPathByFordBellman(
                                    firstIDFB,
                                    secondIDFB,
                                )
                            }
                        } catch (e: Exception) {
                            errorMessage = e.message ?: "Graph is built incorrectly"
                            showErrorDialog = true
                            firstIDFB = ""
                            secondIDFB = ""
                        }
                    },
                    text = "Ford Bellman",
                    fontSize = 28.sp,
                    fontFamily = FontFamily.Monospace,
                    textPadding = 3.dp,
                )
            }
            Row {
                OutlinedTextField(
                    idforLooop,
                    { idforLooop = it },
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
                            scope.launch { viewModel.graphViewModel.findLoopForVertex(idforLooop) }
                        } catch (e: Exception) {
                            errorMessage = e.message ?: "Graph is built incorrectly"
                            showErrorDialog = true
                            firstIDFB = ""
                            secondIDFB = ""
                        }
                    },
                    text = "Find Loop",
                    fontSize = 28.sp,
                    fontFamily = FontFamily.Monospace,
                    textPadding = 3.dp,
                )
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

            Button(
                modifier =
                    Modifier.clip(shape = RoundedCornerShape(15.dp))
                        .height(65.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 7.dp)
                        .pointerHoverIcon(
                            PointerIcon(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR))
                        ),
                onClick = { saveMenuState = true },
                colors =
                    ButtonDefaults.textButtonColors(
                        backgroundColor = CoolColors.Purple,
                        contentColor = CoolColors.DarkGray,
                    ),
            ) {
                Text(
                    modifier = Modifier.padding(start = 70.dp),
                    text = "Save to...",
                    fontSize = 28.sp,
                    fontFamily = FontFamily.Monospace,
                    color = CoolColors.DarkGray,
                    style = TextStyle(textGeometricTransform = TextGeometricTransform(0.3f, 0.3f)),
                )
                Column(horizontalAlignment = Alignment.End, modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.rotate(if (!saveMenuState) -90f else 0f),
                    )
                }
            }
            DropdownMenu(
                modifier = Modifier.background(CoolColors.Gray),
                expanded = saveMenuState,
                onDismissRequest = { saveMenuState = false },
                offset = DpOffset(375.dp, (-150).dp),
            ) {
                DropdownMenuItem(
                    onClick = { dataSystem = DataSystems.Neo4j },
                    Modifier.height(60.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 7.dp)
                        .background(CoolColors.Purple)
                        .border(width = 1.dp, color = CoolColors.Gray),
                    content = {
                        Text(
                            modifier = Modifier.padding(3.dp),
                            text = "Neo4j",
                            fontSize = 28.sp,
                            fontFamily = FontFamily.Monospace,
                            color = CoolColors.DarkGray,
                        )
                    },
                )
                DropdownMenuItem(
                    onClick = { dataSystem = DataSystems.JSON },
                    Modifier.height(60.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 7.dp)
                        .background(CoolColors.Purple)
                        .border(width = 1.dp, color = CoolColors.Gray),
                    content = {
                        Text(
                            modifier = Modifier.padding(3.dp),
                            text = "JSON",
                            fontSize = 28.sp,
                            fontFamily = FontFamily.Monospace,
                            color = CoolColors.DarkGray,
                        )
                    },
                )
                DropdownMenuItem(
                    onClick = { dataSystem = DataSystems.SQLite },
                    Modifier.height(60.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 7.dp)
                        .background(CoolColors.Purple)
                        .border(width = 1.dp, color = CoolColors.Gray),
                    content = {
                        Text(
                            modifier = Modifier.padding(3.dp),
                            text = "SQLite",
                            fontSize = 28.sp,
                            fontFamily = FontFamily.Monospace,
                            color = CoolColors.DarkGray,
                        )
                    },
                )
            }
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
            scope.launch {
                val fileChooser = JsonView()
                try {
                    fileChooser.storeToJson(viewModel.graphViewModel) { dataSystem = null }
                } catch (e: Exception) {
                    errorMessage = e.message ?: "Unknown error"
                    showErrorDialog = true
                    dataSystem = null
                }
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
