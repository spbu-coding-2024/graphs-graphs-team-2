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
import androidx.compose.ui.window.Dialog
import io.ioNeo4j.ReadNeo4j

enum class DataSystems {
    JSON,
    SQLite,
    Neo4j,
}

@Composable
fun GreetingView() {

    var dataSystem by remember { mutableStateOf<DataSystems?>(null) }
    var model by remember { mutableStateOf<Pair<Graph, Map<AbstractVertex, Pair<Dp?, Dp?>?>>?>(null) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
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
                onClick = { dataSystem = DataSystems.JSON },
                text = "JSON",
                fontSize = 75.sp,
                fontFamily = FontFamily.Monospace,
                textPadding = 10.dp
            )
            PurpleButton(
                modifier = Modifier.clip(shape = RoundedCornerShape(35.dp)).weight(0.36f),
                onClick = { dataSystem = DataSystems.SQLite },
                text = "SQLite",
                fontSize = 75.sp,
                fontFamily = FontFamily.Monospace,
                textPadding = 10.dp
            )
            PurpleButton(
                modifier = Modifier.clip(shape = RoundedCornerShape(35.dp)).weight(0.3f),
                onClick = { dataSystem = DataSystems.Neo4j },
                text = "Neo4j",
                fontSize = 75.sp,
                fontFamily = FontFamily.Monospace,
                textPadding = 10.dp
            )
        }

        if (dataSystem == DataSystems.Neo4j) {
            Dialog(onDismissRequest = {}) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize().background(CoolColors.DarkGray),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Enter username and password Neo4j",
                            modifier = Modifier.padding(10.dp),
                            fontSize = 30.sp,
                            style = TextStyle(textGeometricTransform = TextGeometricTransform(0.3f, 0.3f)),
                            color = CoolColors.DarkPurple
                        )
                        val username = remember { mutableStateOf("username") }
                        val password = remember { mutableStateOf("password") }

                        OutlinedTextField(
                            username.value,
                            { username.value = it },
                            textStyle = TextStyle(fontSize = 30.sp, color = CoolColors.DarkPurple),
                            modifier = Modifier.padding(15.dp).width(400.dp)
                        )
                        OutlinedTextField(
                            password.value,
                            { password.value = it },
                            textStyle = TextStyle(fontSize = 30.sp, color = CoolColors.DarkPurple),
                            modifier = Modifier.padding(15.dp).width(400.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            TextButton(
                                onClick = { dataSystem = null },
                                modifier = Modifier.padding(30.dp),
                            ) {
                                Text("Back", fontSize = 32.sp, color = CoolColors.DarkPurple)
                            }
                            TextButton(
                                onClick = {
                                    try {
                                        model = ReadNeo4j(username.value, password.value)
                                    } catch (e: Exception) {
                                        errorMessage = e.message ?: "Error"
                                        showErrorDialog = true
                                    }
                                    if(model != null) {
                                        navigator.push(GraphScreen(model!!.first, model!!.second))
                                    }
                                },
                                modifier = Modifier.padding(30.dp),
                            ) {
                                Text("Confirm", fontSize = 32.sp, color = CoolColors.DarkPurple)
                            }
                        }
                    }
                }
            }

            if (showErrorDialog) {
                ErrorDialog(errorMessage) { showErrorDialog = false }
            }
        }
    }
}

