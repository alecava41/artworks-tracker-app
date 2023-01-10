package it.afm.artworkstracker.featureArtwork.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import it.afm.artworkstracker.R

@Composable
fun Description(desc: String) {
    Column(modifier = Modifier.semantics(mergeDescendants = true) { }) {
        Text(
            text = stringResource(id = R.string.description_label),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(25.dp, 10.dp, 25.dp, 10.dp)
        )

        Text(
            text = desc,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Justify,
            modifier = Modifier
                .padding(25.dp, 0.dp, 25.dp, 15.dp)
        )
    }
}