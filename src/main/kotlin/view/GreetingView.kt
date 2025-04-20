package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import inpout.SQLiteEXP
import view.components.CoolColors
import view.components.PurpleButton

enum class FileSystem {
    Json,
    SQLite,
    Neo4j,
}

@Composable
fun GreetingView() {

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
        }
        if(fileSystem == FileSystem.SQLite) {
            Dialog(
                onDismissRequest = { fileSystem = null }

            ){
                Card (
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp),
                    shape = RoundedCornerShape(16.dp),
                ){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        val connection = SQLiteEXP("app.db")
                        val listOfGraphs = remember{connection.makeListFromNames()}
                        var searchQuery by remember { mutableStateOf("") }
                        val filteredNames = remember(searchQuery, listOfGraphs) {
                            if (searchQuery.isEmpty()) listOfGraphs
                            else listOfGraphs.filter { it.contains(searchQuery, ignoreCase = true) }
                        }
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.fillMaxWidth().padding(20.dp),
                            label = { Text("Graph search") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(filteredNames) { name ->
                                Button(
                                    onClick = {},
                                    modifier = Modifier.fillMaxWidth(),
                                ){
                                    Text(name)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



