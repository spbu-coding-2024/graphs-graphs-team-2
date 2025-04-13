package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import CoolColors
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.unit.sp

enum class FileSystem {
    Json,
    SQLite,
    Neo4j,
}

@Composable
fun GreetingView() {
    var fileSystem: FileSystem? = null
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
            Button(
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = CoolColors.Purple,
                    contentColor = CoolColors.Gray,
                ),
                modifier = Modifier.clip(shape = RoundedCornerShape(35.dp)).weight(0.24f),
                onClick = { fileSystem = FileSystem.Json }) {
                Text(
                    "Json",
                    style = TextStyle(textGeometricTransform = TextGeometricTransform(0.3f, 0.3f)),
                    fontSize = 75.sp,
                    modifier = Modifier.padding(10.dp),
                    fontFamily = FontFamily.Monospace,
                )
            }
            Button(
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = CoolColors.Purple,
                    contentColor = CoolColors.Gray,
                ),
                modifier = Modifier.clip(shape = RoundedCornerShape(35.dp)).weight(0.36f),
                onClick = { fileSystem = FileSystem.SQLite }) {
                Text(
                    "SQLite",
                    style = TextStyle(textGeometricTransform = TextGeometricTransform(0.3f, 0.3f)),
                    fontSize = 75.sp,
                    modifier = Modifier.padding(10.dp),
                    fontFamily = FontFamily.Monospace,
                )
            }
            Button(
                colors = ButtonDefaults.textButtonColors(
                    backgroundColor = CoolColors.Purple,
                    contentColor = CoolColors.Gray,
                ),
                modifier = Modifier.clip(shape = RoundedCornerShape(35.dp)).weight(0.3f),
                onClick = { fileSystem = FileSystem.Neo4j }) {
                Text(
                    "Neo4j",
                    style = TextStyle(textGeometricTransform = TextGeometricTransform(0.3f, 0.3f)),
                    fontSize = 75.sp,
                    modifier = Modifier.padding(10.dp),
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

