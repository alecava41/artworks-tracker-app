package it.afm.artworkstracker.featureArtwork.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Description(desc: String) {

    Text(
        text = "Description:",
/*        fontSize = 15.sp,
        fontStyle = FontStyle.Italic,
        fontFamily = FontFamily.SansSerif,*/
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .padding(25.dp, 10.dp, 25.dp, 10.dp)
    )

    Text(
        text = desc,
/*        fontSize = 14.sp,
        fontFamily = FontFamily.SansSerif,*/
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Justify,
        modifier = Modifier
            .padding(25.dp, 0.dp, 25.dp, 15.dp)
    )
}