package view.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import org.jetbrains.skiko.Cursor

@Composable
fun PurpleButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    fontSize: TextUnit,
    fontFamily: FontFamily = FontFamily.Default,
    textPadding: Dp,
) {
    Button(
        colors =
            ButtonDefaults.textButtonColors(
                backgroundColor = CoolColors.Purple,
                contentColor = CoolColors.DarkGray,
            ),
        modifier =
            modifier.pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR))),
        onClick = onClick,
    ) {
        Text(
            text,
            style = TextStyle(textGeometricTransform = TextGeometricTransform(0.3f, 0.3f)),
            fontSize = fontSize,
            modifier = Modifier.padding(textPadding),
            fontFamily = fontFamily,
        )
    }
}

@Composable
fun InvertPurpleButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    fontSize: TextUnit,
    fontFamily: FontFamily = FontFamily.Default,
    textPadding: Dp,
) {
    Button(
        colors =
            ButtonDefaults.textButtonColors(
                backgroundColor = CoolColors.DarkGray,
                contentColor = CoolColors.Purple,
            ),
        modifier =
            modifier.pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR))),
        onClick = onClick,
    ) {
        Text(
            text,
            style = TextStyle(textGeometricTransform = TextGeometricTransform(0.3f, 0.3f)),
            fontSize = fontSize,
            modifier = Modifier.padding(textPadding),
            fontFamily = fontFamily,
        )
    }
}
