package org.example.alittlebetter.core.animation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import kotlinx.coroutines.delay

/** Reveals [text] one character at a time - used for the Daily Thought screen. */
@Composable
fun LetterRevealText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign? = null,
    millisPerChar: Long = 35L,
) {
    var visibleChars by remember(text) { mutableStateOf(0) }

    LaunchedEffect(text) {
        visibleChars = 0
        while (visibleChars < text.length) {
            delay(millisPerChar)
            visibleChars++
        }
    }

    Text(
        text = text.take(visibleChars),
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        textAlign = textAlign,
    )
}
