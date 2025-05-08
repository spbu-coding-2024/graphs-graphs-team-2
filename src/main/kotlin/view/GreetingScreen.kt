package view

import GraphScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.ioNeo4j.ReadNeo4j
import model.Graph
import model.abstractGraph.AbstractVertex
import view.components.CoolColors
import view.components.ErrorDialog
import view.components.PurpleButton
import view.io.Neo4jView
import view.io.SQLiteSearchView
import view.io.loadFromJson
import viewModel.GreetingScreenViewModel
import viewModel.io.JSONViewModel
import viewModel.io.SQLiteSearchScreenViewModel

enum class DataSystems {
    JSON,
    SQLite,
    Neo4j,
}

@Composable
fun GreetingView(viewModel: GreetingScreenViewModel) {
    var model by remember {
        mutableStateOf<Pair<Graph, Map<AbstractVertex, Pair<Dp?, Dp?>?>>?>(null)
    }
    val username = remember { mutableStateOf(viewModel.username) }
    val password = remember { mutableStateOf(viewModel.password) }
    val navigator = LocalNavigator.currentOrThrow

    Column(
        modifier = Modifier.fillMaxSize().background(color = CoolColors.Gray),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            "Choose your data system",
            fontSize = 95.sp,
            style = TextStyle(textGeometricTransform = TextGeometricTransform(0.3f, 0.3f)),
            modifier = Modifier.padding(20.dp),
            color = CoolColors.DarkPurple,
            fontFamily = FontFamily.Monospace,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.width(1000.dp),
        ) {
            PurpleButton(
                modifier = Modifier.clip(shape = RoundedCornerShape(35.dp)).weight(0.24f),
                onClick = { viewModel.dataSystem = DataSystems.JSON },
                text = "JSON",
                fontSize = 75.sp,
                fontFamily = FontFamily.Monospace,
                textPadding = 10.dp,
            )
            PurpleButton(
                modifier = Modifier.clip(shape = RoundedCornerShape(35.dp)).weight(0.36f),
                onClick = { viewModel.dataSystem = DataSystems.SQLite },
                text = "SQLite",
                fontSize = 75.sp,
                fontFamily = FontFamily.Monospace,
                textPadding = 10.dp,
            )
            PurpleButton(
                modifier = Modifier.clip(shape = RoundedCornerShape(35.dp)).weight(0.3f),
                onClick = { viewModel.dataSystem = DataSystems.Neo4j },
                text = "Neo4j",
                fontSize = 75.sp,
                fontFamily = FontFamily.Monospace,
                textPadding = 10.dp,
            )
        }

        if (viewModel.dataSystem == DataSystems.Neo4j) {
            Neo4jView(username, password) { viewModel.dataSystem = null }
            if (username.value != null && password.value != null) {
                try {
                    model = ReadNeo4j(username.value ?: "", password.value ?: "")
                } catch (e: Exception) {
                    viewModel.apply {
                        errorMessage = e.message ?: "Error"
                        showErrorDialog = true
                        dataSystem = null
                    }
                    username.value = null
                    password.value = null
                }
                if (model != null) {
                    navigator.push(GraphScreen(model!!.first, model!!.second))
                }
            }
        }

        if (viewModel.dataSystem == DataSystems.JSON) {
            loadFromJson(JSONViewModel(), navigator) { viewModel.dataSystem = null }
        }
        if (viewModel.dataSystem == DataSystems.SQLite) {
            SQLiteSearchView(
                SQLiteSearchScreenViewModel(),
                onDismissRequest = { viewModel.dataSystem = null },
                navigator,
                onErrorRequest = { viewModel.apply {
                    errorMessage = "Graph not found"
                    showErrorDialog = true
                    dataSystem = null
                }}
            )
        }

        if (viewModel.showErrorDialog) {
            ErrorDialog(viewModel.errorMessage) { viewModel.showErrorDialog = false }
        }
    }
}
