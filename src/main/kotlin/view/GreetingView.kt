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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import model.Graph
import model.abstractGraph.AbstractVertex
import view.components.CoolColors
import view.components.ErrorDialog
import view.components.PurpleButton
import view.io.Neo4jView


enum class DataSystems {
    JSON,
    SQLite,
    Neo4j,
}

@Composable
fun GreetingView() {

    var dataSystem = remember { mutableStateOf<DataSystems?>(null) }
    var model = remember { mutableStateOf<Pair<Graph, Map<AbstractVertex, Pair<Dp?, Dp?>?>>?>(null) }
    var showErrorDialog = remember { mutableStateOf(false) }
    var errorMessage = remember { mutableStateOf("") }
    val navigator = LocalNavigator.currentOrThrow

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = CoolColors.Gray),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            "Choose your file system",
            fontSize = 95.sp,
            style = TextStyle(textGeometricTransform = TextGeometricTransform(0.3f, 0.3f)),
            modifier = Modifier.padding(20.dp),
            color = CoolColors.DarkPurple,
            fontFamily = FontFamily.Monospace,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.width(1000.dp)
        ) {
            PurpleButton(
                modifier = Modifier.clip(shape = RoundedCornerShape(35.dp)).weight(0.24f),
                onClick = { dataSystem.value = DataSystems.JSON },
                text = "JSON",
                fontSize = 75.sp,
                fontFamily = FontFamily.Monospace,
                textPadding = 10.dp
            )
            PurpleButton(
                modifier = Modifier.clip(shape = RoundedCornerShape(35.dp)).weight(0.36f),
                onClick = { dataSystem.value = DataSystems.SQLite },
                text = "SQLite",
                fontSize = 75.sp,
                fontFamily = FontFamily.Monospace,
                textPadding = 10.dp
            )
            PurpleButton(
                modifier = Modifier.clip(shape = RoundedCornerShape(35.dp)).weight(0.3f),
                onClick = { dataSystem.value = DataSystems.Neo4j },
                text = "Neo4j",
                fontSize = 75.sp,
                fontFamily = FontFamily.Monospace,
                textPadding = 10.dp
            )
        }

        if (dataSystem.value == DataSystems.Neo4j) {
            Neo4jView(dataSystem, errorMessage, model, showErrorDialog, navigator)
        }

        if (showErrorDialog.value) {
            ErrorDialog(errorMessage.value) { showErrorDialog.value = false }
        }
    }
}

