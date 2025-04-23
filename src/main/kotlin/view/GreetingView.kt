package view

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
import androidx.compose.ui.unit.sp
import view.components.CoolColors
import view.components.PurpleButton
import view.io.JsonView
import view.io.SQLiteView
import viewModel.GraphViewModel
import viewModel.SearchScreenSQlite.SQLiteSearchScreenViewModel

enum class FileSystem {
    JSON,
    SQLite,
    Neo4j,
}

@Composable
fun GreetingView() {

    var fileSystem by remember { mutableStateOf<FileSystem?>(null) }
    var model by remember { mutableStateOf<GraphViewModel?>(null) }

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
                onClick = { fileSystem = FileSystem.JSON },
                text = "JSON",
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

        if (fileSystem == FileSystem.JSON) {
            val fileChooser = JsonView()
            try {
                model = fileChooser.loadFromJson()
                if(model == null ) fileSystem = null
            } catch(e: Exception) {
                fileSystem = null
            }
        }
        if (fileSystem == FileSystem.SQLite) {
            SQLiteView(SQLiteSearchScreenViewModel(), onDismissRequest = {fileSystem = null})
        }
    }
}

