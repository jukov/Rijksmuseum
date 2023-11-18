package info.jukov.rijksmuseum.feature.art.item.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ArtItemScreen(
    itemId: String
) {
    Text(text = itemId)
}