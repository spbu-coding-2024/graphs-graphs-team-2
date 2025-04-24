package view.components

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

object CoolColors {
    @Stable
    val DarkGray = Color(29, 29, 38)

    @Stable
    val Gray = Color(44, 44, 59)

    @Stable
    val Purple = Color(140, 110, 185)

    @Stable
    val DarkPurple = Color(130, 100, 175)

    @Stable
    val Bardo = Color(200, 12, 12)

    @Stable
    val Pink = Color(233, 184, 234)

    @Stable
    val White = Color(200, 200, 200)

    @Stable
    val Blue = Color(139, 170, 183)

    val RandomColor
        get()= Color(
        Random.nextInt(80, 255),
        Random.nextInt(80, 255),
        Random.nextInt(80, 255),
    )
}