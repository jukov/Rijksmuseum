package info.jukov.rijksmuseum.feature.artcollection.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import info.jukov.rijksmuseum.feature.art.collection.domain.model.ArtCollectionItem
import info.jukov.rijksmuseum.feature.art.collection.presentation.ArtCollectionViewModel
import info.jukov.rijksmuseum.ui.theme.RijksmuseumTheme

@Composable
fun ArtCollectionScreen(
    onItemClick: (itemId: String) -> Unit,
    viewModel: ArtCollectionViewModel = hiltViewModel()
) {
    val listState = rememberLazyListState()
    val modelState = viewModel.model.observeAsState()

    ArtCollectionContent(onItemClick, listState, modelState)
}

@Composable
private fun ArtCollectionContent(
    onItemClick: (itemId: String) -> Unit,
    listState: LazyListState,
    modelState: State<List<ArtCollectionItem>?>
) {
    LazyColumn(state = listState) {
        modelState.value?.let { model ->
            itemsIndexed(model) { _, item ->
                Column(modifier = Modifier
                    .padding(8.dp)
                    .clickable { onItemClick(item.id) }
                ) { // TODO Material 3
                    Text(
                        text = item.name,
                        fontSize = 18.sp
                    )
                    Text(
                        text = item.author?.let { "by $it" } ?: "",//TODO i18n
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = item.description ?: "",
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArtCollectionConrentPreview() {
    val model = remember {
        mutableStateOf((1..20).map { index ->
            ArtCollectionItem(
                index.toString(),
                "Painting $index",
                "Painting from famous author $index",
                "Author $index"
            )
        })
    }
    RijksmuseumTheme {
        ArtCollectionContent({ }, rememberLazyListState(), model)
    }
}