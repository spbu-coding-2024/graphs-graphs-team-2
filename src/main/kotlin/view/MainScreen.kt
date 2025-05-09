package view

import WelcomeScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.CircularProgressIndicator
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.skiko.Cursor
import view.components.CoolColors
import view.components.ErrorDialog
import view.components.InvertPurpleButton
import view.components.PurpleButton
import view.graph.GraphView
import view.io.Neo4jView
import view.io.SQLiteNameInputView
import view.io.storeToJson
import viewModel.MainScreenViewModel
import viewModel.io.Neo4jViewModel
import viewModel.io.SQLiteSearchScreenViewModel

@Composable
fun MainScreen(viewModel: MainScreenViewModel) {

    val scope = rememberCoroutineScope { Dispatchers.Default }
    val navigator = LocalNavigator.currentOrThrow
    if (!viewModel.isLoading) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.background(CoolColors.Gray),
        ) {
            var scale by remember {
                mutableStateOf(viewModel.graphViewModel.calculateScaleAndOffset())
            }

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
                    onClick = { viewModel.showMenuState = true },
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
                        style =
                            TextStyle(textGeometricTransform = TextGeometricTransform(0.3f, 0.3f)),
                    )
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Icon(
                            Icons.Filled.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.rotate(if (viewModel.showMenuState) 180f else 0f),
                        )
                    }
                }
                DropdownMenu(
                    modifier = Modifier.background(CoolColors.Gray),
                    expanded = viewModel.showMenuState,
                    onDismissRequest = { viewModel.showMenuState = false },
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
                            viewModel.graphViewModel.placementAlgorithm()
                            scale = viewModel.graphViewModel.calculateScaleAndOffset()
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
                    onClick = {
                        scope.launch {
                            viewModel.isLoading = true
                            viewModel.graphViewModel.Louvain()
                            viewModel.isLoading = false
                        }
                    },
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
                        onClick = {
                            scope.launch {
                                try {
                                    viewModel.graphViewModel.minimalSpanningTree()
                                } catch (e: Exception) {
                                    viewModel.apply {
                                        errorMessage = e.message ?: ("Graph is not connected")
                                        showErrorDialog = true
                                    }
                                }
                            }
                        },
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
                        onClick = {
                            scope.launch { viewModel.graphViewModel.highlightComponents() }
                        },
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
                        onClick = {
                            scope.launch {
                                viewModel.isLoading = true
                                viewModel.graphViewModel.DrawBridges()
                                viewModel.isLoading = false
                            }
                        },
                        text = "Find Bridges",
                        fontSize = 28.sp,
                        fontFamily = FontFamily.Monospace,
                        textPadding = 3.dp,
                    )
                }

                Row {
                    OutlinedTextField(
                        viewModel.graphViewModel.firstIdDijkstra,
                        { viewModel.graphViewModel.firstIdDijkstra = it },
                        textStyle = TextStyle(fontSize = 28.sp, color = CoolColors.DarkPurple),
                        modifier = Modifier.width(90.dp).height(65.dp),
                    )
                    OutlinedTextField(
                        viewModel.graphViewModel.secondIdDijkstra,
                        { viewModel.graphViewModel.secondIdDijkstra = it },
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
                                    viewModel.isLoading = true
                                    viewModel.graphViewModel.Dijkstra()
                                    viewModel.isLoading = false
                                }
                            } catch (e: Exception) {
                                viewModel.apply {
                                    errorMessage = e.message ?: "Graph is built incorrectly"
                                    showErrorDialog = true
                                    graphViewModel.firstIdDijkstra = ""
                                    graphViewModel.secondIdDijkstra = ""
                                }
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
                        viewModel.graphViewModel.firstIDFB,
                        { viewModel.graphViewModel.firstIDFB = it },
                        textStyle = TextStyle(fontSize = 28.sp, color = CoolColors.DarkPurple),
                        modifier = Modifier.width(90.dp).height(65.dp),
                    )
                    OutlinedTextField(
                        viewModel.graphViewModel.secondIDFB,
                        { viewModel.graphViewModel.secondIDFB = it },
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
                                scope.launch { viewModel.graphViewModel.findPathByFordBellman() }
                            } catch (e: Exception) {
                                viewModel.apply {
                                    viewModel.apply {
                                        errorMessage = e.message ?: "Graph is built incorrectly"
                                        showErrorDialog = true
                                        graphViewModel.firstIDFB = ""
                                        graphViewModel.secondIDFB = ""
                                    }
                                }
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
                        viewModel.graphViewModel.idForLoop,
                        { viewModel.graphViewModel.idForLoop = it },
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
                                scope.launch { viewModel.graphViewModel.findLoopForVertex() }
                            } catch (e: Exception) {
                                viewModel.apply {
                                    errorMessage = e.message ?: "Graph is built incorrectly"
                                    showErrorDialog = true
                                    graphViewModel.firstIDFB = ""
                                    graphViewModel.secondIDFB = ""
                                }
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
                    onClick = { viewModel.saveMenuState = true },
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
                        style =
                            TextStyle(textGeometricTransform = TextGeometricTransform(0.3f, 0.3f)),
                    )
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Icon(
                            Icons.Filled.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.rotate(if (!viewModel.saveMenuState) -90f else 0f),
                        )
                    }
                }
                DropdownMenu(
                    modifier = Modifier.background(CoolColors.Gray),
                    expanded = viewModel.saveMenuState,
                    onDismissRequest = { viewModel.saveMenuState = false },
                    offset = DpOffset(375.dp, (-150).dp),
                ) {
                    DropdownMenuItem(
                        onClick = { viewModel.dataSystem = DataSystems.Neo4j },
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
                        onClick = { viewModel.dataSystem = DataSystems.JSON },
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
                        onClick = { viewModel.dataSystem = DataSystems.SQLite },
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
                    onClick = { viewModel.openNewGraph = true },
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

            if (viewModel.dataSystem == DataSystems.JSON) {
                storeToJson(viewModel, viewModel.graphViewModel) { viewModel.dataSystem = null }
            }

            if (viewModel.dataSystem == DataSystems.Neo4j) {
                val neo4jViewModel = Neo4jViewModel()
                Neo4jView(
                    true,
                    viewModel.graphViewModel,
                    neo4jViewModel,
                    navigator,
                    { viewModel.dataSystem = null },
                    { viewModel.showErrorDialog = neo4jViewModel.showErrorDialog.value },
                    { viewModel.errorMessage = neo4jViewModel.errorMessage.value },
                    { viewModel.isLoading = true },
                    { viewModel.isLoading = false },
                )
            }
            if (viewModel.dataSystem == DataSystems.SQLite) {
                SQLiteNameInputView(mutableStateOf(viewModel.graphName)) {
                    viewModel.dataSystem = null
                }
                if (viewModel.graphName != null) {
                    try {
                        SQLiteSearchScreenViewModel()
                            .writeGraph(viewModel.graphViewModel, viewModel.graphName ?: "")
                        viewModel.apply {
                            dataSystem = null
                            graphName = null
                        }
                    } catch (e: Exception) {
                        viewModel.apply {
                            errorMessage = e.message ?: "Unknown error"
                            showErrorDialog = true
                            graphName = null
                            dataSystem = null
                        }
                    }
                }
            }
            if (viewModel.openNewGraph) {
                navigator.push(WelcomeScreen)
            }
        }
    } else {
        Box(Modifier.fillMaxSize().background(color = CoolColors.Gray)) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }
    if (viewModel.showErrorDialog) {
        ErrorDialog(viewModel.errorMessage) { viewModel.showErrorDialog = false }
    }
}
