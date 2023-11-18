package info.jukov.rijksmuseum.feature.artcollection.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import info.jukov.rijksmuseum.R
import info.jukov.rijksmuseum.feature.art.collection.domain.model.ArtCollectionItem
import info.jukov.rijksmuseum.feature.art.collection.presentation.ArtCollectionViewModel
import info.jukov.rijksmuseum.feature.art.collection.presentation.model.ArtCollectionUiModel
import info.jukov.rijksmuseum.feature.art.collection.presentation.model.PageState
import info.jukov.rijksmuseum.ui.theme.RijksmuseumTheme

@Composable
fun ArtCollectionScreen(
    onItemClick: (itemId: String) -> Unit,
    viewModel: ArtCollectionViewModel = hiltViewModel()
) {
    val modelState = viewModel.model.observeAsState()

    modelState.value?.let { model ->
        when (model) {
            is ArtCollectionUiModel.Content ->
                Content(
                    model = model,
                    onItemClick = onItemClick,
                    onRefresh = { viewModel.refresh() },
                    onLoadMore = { viewModel.loadMore() },
                    onPageReload = { viewModel.reloadPage() }
                )

            ArtCollectionUiModel.EmptyProgress ->
                EmptyProgress()

            is ArtCollectionUiModel.EmptyError ->
                EmptyError(
                    message = model.message,
                    onReloadClick = {
                        viewModel.reload()
                    }
                )
        }
    }
}

fun LazyListState.shouldLoadMore(): Boolean {
    return firstVisibleItemIndex > layoutInfo.totalItemsCount - 10
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Content(
    model: ArtCollectionUiModel.Content,
    onItemClick: (itemId: String) -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onPageReload: () -> Unit
) {
    val listState = rememberLazyListState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = model.refreshing,
        onRefresh = onRefresh
    )

    val shouldLoadMore = remember {
        derivedStateOf { listState.shouldLoadMore() }
    }

    Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
        LazyColumn(state = listState) {
            itemsIndexed(model.items) { _, item ->
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

            when (model.newPageState) {
                is PageState.Error -> {
                    item {
                        PageError(
                            message = model.newPageState.message,
                            onReloadClick = {
                                onPageReload()
                            }
                        )
                    }
                }

                PageState.Loading -> {
                    item {
                        PageProgress()
                    }
                }

                PageState.None -> Unit
            }
        }

        PullRefreshIndicator(
            model.refreshing,
            pullRefreshState,
            Modifier.align(Alignment.TopCenter),
            contentColor = MaterialTheme.colors.secondary
        )

        LaunchedEffect(shouldLoadMore.value) {
            if (shouldLoadMore.value) {
                onLoadMore()
            }
        }
    }
}

@Composable
private fun EmptyProgress() {
    //TODO shimmer
    //TODO animate change
    Box(contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun PageProgress() {
    Box(
        modifier = Modifier
            .height(96.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun PageError(
    message: String?,
    onReloadClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(96.dp)
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(R.drawable.baseline_palette_24),
            contentDescription = stringResource(R.string.art_collection_empty_error_icon_content_description),
            colorFilter = ColorFilter.tint(Color.Black),
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
        )

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.weight(1f)
                .padding(horizontal = 8.dp)
        ) {

            Text(
                text = stringResource(R.string.art_collection_empty_error),
                fontSize = 16.sp,
            )

            Text(
                text = message ?: stringResource(R.string.art_collection_empty_error_undocumented),
                fontSize = 14.sp,
            )
        }

        OutlinedButton(
            onClick = onReloadClick
        ) {
            Text(text = stringResource(R.string.art_collection_empty_error_reload))
        }
    }
}

@Composable
private fun EmptyError(
    message: String?,
    onReloadClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.baseline_palette_24),
            contentDescription = stringResource(R.string.art_collection_empty_error_icon_content_description),
            colorFilter = ColorFilter.tint(Color.Black),
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
        )

        Text(
            text = stringResource(R.string.art_collection_empty_error),
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = message ?: stringResource(R.string.art_collection_empty_error_undocumented),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        OutlinedButton(
            onClick = onReloadClick,
            modifier = Modifier.padding(top = 16.dp),

            ) {
            Text(text = stringResource(R.string.art_collection_empty_error_reload))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContentPreview() {
    val model = ArtCollectionUiModel.Content(
        refreshing = false,
        newPageState = PageState.None,
        hasNext = true,
        0,
        (1..5).map { index ->
            ArtCollectionItem(
                index.toString(),
                "Painting $index",
                "Painting from famous author $index",
                "Author $index"
            )
        })
    RijksmuseumTheme {
        Content(model, { }, { }, { }, { })
    }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 300)
@Composable
fun EmptyProgressPreview() {
    EmptyProgress()
}

@Preview(showBackground = true)
@Composable
fun PageProgressPreview() {
    PageProgress()
}

@Preview(showBackground = true, widthDp = 300, heightDp = 300)
@Composable
fun EmptyErrorPreview() {
    EmptyError("Can't connect to server", {})
}

@Preview(showBackground = true)
@Composable
fun PageErrorPreview() {
    PageError("Can't connect to server", {})
}