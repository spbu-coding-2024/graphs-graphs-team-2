package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import io.ioNeo4j.ReadNeo4j
import view.components.CoolColors
import view.components.PurpleButton
import viewModel.GraphViewModel
import java.io.File

enum class FileSystem {
    Json,
    SQLite,
    Neo4j,
}

@Composable
fun GreetingView() {
    var graphViewModel: GraphViewModel

    var fileSystem by remember { mutableStateOf<FileSystem?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = CoolColors.DarkGray),
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
                onClick = { fileSystem = FileSystem.Json },
                text = "Json",
                fontSize = 75.sp,
                fontFamily = FontFamily.Monospace,
                textPadding = 10.dp
            )
            PurpleButton(
                modifier = Modifier.clip(shape = RoundedCornerShape(35.dp)).weight(0.36f),
                onClick = { fileSystem = FileSystem.SQLite },
                text = "SQLite",
                fontSize = 75.sp,
                fontFamily = FontFamily.Monospace,
                textPadding = 10.dp
            )
            PurpleButton(
                modifier = Modifier.clip(shape = RoundedCornerShape(35.dp)).weight(0.3f),
                onClick = { fileSystem = FileSystem.Neo4j },
                text = "Neo4j",
                fontSize = 75.sp,
                fontFamily = FontFamily.Monospace,
                textPadding = 10.dp
            )
            if (fileSystem == FileSystem.Neo4j) {
                val state = remember { TextFieldState() }
                var showPassword by remember { mutableStateOf(false) }
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
                                    onClick = { fileSystem = null },
                                    modifier = Modifier.padding(30.dp),
                                ) {
                                    Text("Back", fontSize = 32.sp, color = CoolColors.DarkPurple)
                                }
                                TextButton(
                                    onClick = {
                                        graphViewModel = ReadNeo4j(username.value,password.value)
                                        fileSystem = null
                                    },
                                    modifier = Modifier.padding(30.dp),
                                ) {
                                    Text("Confirm", fontSize = 32.sp, color = CoolColors.DarkPurple)
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}

