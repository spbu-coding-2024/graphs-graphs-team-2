package view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun InformationDialog(message: String, onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier.fillMaxWidth().height(400.dp),
            shape = RoundedCornerShape(25.dp),
            backgroundColor = CoolColors.Gray,
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier.padding(top = 25.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(text = "Information", fontSize = 30.sp, color = CoolColors.White)
                }
                Row(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = message,
                        modifier = Modifier.padding(10.dp),
                        fontSize = 30.sp,
                        style =
                            TextStyle(textGeometricTransform = TextGeometricTransform(0.3f, 0.3f)),
                        color = CoolColors.White,
                    )
                }
            }
            Row(
                modifier = Modifier.padding(25.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End,
            ) {
                PurpleButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.clip(shape = RoundedCornerShape(10.dp)),
                    text = "OK",
                    fontSize = 32.sp,
                    textPadding = 7.dp,
                )
            }
        }
    }
}
