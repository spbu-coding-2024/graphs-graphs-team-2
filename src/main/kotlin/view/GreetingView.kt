package view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.unit.sp


enum class FileSystem {
    Json,
    SQLite,
    neo4j
}


@Composable
fun GreetingView() {
    var fileSystem: FileSystem? = null
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Choose your file system", fontSize = 100.sp,
            style = TextStyle(textGeometricTransform = TextGeometricTransform(0.3f, 0.3f)),
            modifier = Modifier.padding(20.dp)
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.width(1000.dp)
        ) {
            Button(
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = Color.Black,
                    contentColor = Color.White
                ),
                modifier = Modifier.clip(shape = RoundedCornerShape(35.dp)).weight(1f),
                onClick = { fileSystem = FileSystem.Json }) {
                Text(
                    "Json",
                    style = TextStyle(textGeometricTransform = TextGeometricTransform(0.3f, 0.3f)),
                    fontSize = 90.sp,
                    modifier = Modifier.padding(10.dp)
                )
            }
            Button(
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = Color.Black,
                    contentColor = Color.White
                ),
                modifier = Modifier.clip(shape = RoundedCornerShape(35.dp)).weight(1f),
                onClick = { fileSystem = FileSystem.SQLite }) {
                Text(
                    "SQLite",
                    style = TextStyle(textGeometricTransform = TextGeometricTransform(0.3f, 0.3f)),
                    fontSize = 90.sp,
                    modifier = Modifier.padding(10.dp)
                )
            }
            Button(
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = Color.Black,
                    contentColor = Color.White
                ),
                modifier = Modifier.clip(shape = RoundedCornerShape(35.dp)).weight(1f),
                onClick = { fileSystem = FileSystem.neo4j }) {
                Text(
                    "neo4j",
                    style = TextStyle(textGeometricTransform = TextGeometricTransform(0.3f, 0.3f)),
                    fontSize = 90.sp,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}

