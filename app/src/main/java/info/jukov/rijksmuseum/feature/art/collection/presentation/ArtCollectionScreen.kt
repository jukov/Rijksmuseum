package info.jukov.rijksmuseum.feature.art.collection.presentation

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import info.jukov.rijksmuseum.R
import info.jukov.rijksmuseum.feature.art.collection.domain.model.ArtCollectionItem
import info.jukov.rijksmuseum.feature.art.collection.presentation.model.ArtCollectionUiModel
import info.jukov.rijksmuseum.feature.art.collection.presentation.model.ArtCollectionUiModel.Item
import info.jukov.rijksmuseum.feature.art.collection.presentation.model.ArtCollectionUiState
import info.jukov.rijksmuseum.feature.art.collection.presentation.model.PageState
import info.jukov.rijksmuseum.ui.theme.RijksmuseumTheme
import info.jukov.rijksmuseum.util.UiState
import info.jukov.rijksmuseum.util.shimmerLoadingAnimation
import info.jukov.rijksmuseum.util.shouldLoadMore

@Suppress("NAME_SHADOWING")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtCollectionScreen(
    onItemClick: (itemId: String, itemName: String) -> Unit,
    viewModel: ArtCollectionViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val modelState = viewModel.model.observeAsState()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        stringResource(id = R.string.app_name),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { innerPadding ->
        var uiState by remember { mutableStateOf(UiState.Progress) }

        modelState.value?.let { model ->
            uiState = model.uiState
        }

        Crossfade(uiState, label = "ArtCollectionCrossfade") { uiState ->
            when (uiState) {
                UiState.Content ->
                    Content(
                        onItemClick = onItemClick,
                        onRefresh = { viewModel.refresh() },
                        onLoadMore = { viewModel.loadMore() },
                        onPageReload = { viewModel.reloadPage() },
                        outerPadding = innerPadding,
                        model = (modelState.value as ArtCollectionUiState.Content)
                    )

                UiState.Progress ->
                    EmptyProgress(
                        outerPadding = innerPadding
                    )

                UiState.Error ->
                    EmptyError(
                        outerPadding = innerPadding,
                        message = (modelState.value as ArtCollectionUiState.EmptyError).message,
                        onReloadClick = {
                            viewModel.reload()
                        }
                    )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Content(
    onItemClick: (itemId: String, itemName: String) -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onPageReload: () -> Unit,
    outerPadding: PaddingValues,
    model: ArtCollectionUiState.Content
) {
    val listState = rememberLazyStaggeredGridState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = model.refreshing,
        onRefresh = onRefresh
    )

    val shouldLoadMore = remember {
        derivedStateOf { listState.shouldLoadMore() }
    }

    Box(
        modifier = Modifier
            .pullRefresh(pullRefreshState)
            .padding(outerPadding)
    ) {
        LazyVerticalStaggeredGrid(
            state = listState,
            columns = StaggeredGridCells.Adaptive(150.dp),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            for (item in model.items) {
                when (item) {
                    is ArtCollectionUiModel.Header -> {
                        item(span = StaggeredGridItemSpan.FullLine) {
                            ArtHeader(item.title)
                        }
                    }

                    is Item -> {
                        item {
                            ArtItem(onItemClick, item.item)
                        }
                    }
                }
            }

            when (model.newPageState) {
                is PageState.Error -> {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        PageError(
                            message = model.newPageState.message,
                            onReloadClick = {
                                onPageReload()
                            }
                        )
                    }
                }

                PageState.Loading -> {
                    item(span = StaggeredGridItemSpan.FullLine) {
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
            contentColor = MaterialTheme.colorScheme.secondary
        )

        LaunchedEffect(shouldLoadMore.value) {
            if (shouldLoadMore.value) {
                onLoadMore()
            }
        }
    }
}

@Composable
private fun ArtHeader(
    title: String
) {
    Box {
        Text(
            text = title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArtItem(
    onItemClick: (itemId: String, itemName: String) -> Unit,
    item: ArtCollectionItem
) {
    Card(
        onClick = { onItemClick(item.id, item.title) },
        modifier = Modifier
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            if (item.imageUrl != null && item.imageAspectRatio != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.art_collection_object_image_content_description),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.clip(RoundedCornerShape(8.dp))
                        .fillMaxWidth()
                        .aspectRatio(item.imageAspectRatio)
                )
            } else {
                Card(border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant)) {
                    Text(
                        text = stringResource(R.string.art_collection_object_image_fallback),
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 24.dp)
                    )
                }
            }
            Text(
                text = item.longTitle,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Suppress("NAME_SHADOWING")
@Composable
private fun EmptyProgress(outerPadding: PaddingValues) {
    Box(modifier = Modifier.padding(outerPadding)) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(150.dp),
            modifier = Modifier.padding(horizontal = 8.dp),
            userScrollEnabled = false
        ) {
            for (i in 1..4) {
                item(span = StaggeredGridItemSpan.FullLine) {
                    Box {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 16.dp)
                                .height(24.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .shimmerLoadingAnimation()
                        )
                    }
                }
                for (i in 1..4) {
                    item {
                        Card(
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(140.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .shimmerLoadingAnimation(true)
                                )
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(24.dp)
                                        .padding(top = 8.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .shimmerLoadingAnimation()
                                )
                            }
                        }
                    }
                }
            }
        }
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
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {

            Text(
                text = stringResource(R.string.art_collection_empty_error),
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = message ?: stringResource(R.string.art_collection_empty_error_undocumented),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
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
    outerPadding: PaddingValues,
    message: String?,
    onReloadClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(outerPadding)
    ) {
        Image(
            painter = painterResource(R.drawable.baseline_palette_24),
            contentDescription = stringResource(R.string.art_collection_empty_error_icon_content_description),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
                .padding(horizontal = 32.dp)
        )
        Text(
            text = stringResource(R.string.art_collection_empty_error),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp, start = 32.dp, end = 32.dp),
            maxLines = 5,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = message ?: stringResource(R.string.art_collection_empty_error_undocumented),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, start = 32.dp, end = 32.dp),
            maxLines = 5,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium
        )

        OutlinedButton(
            onClick = onReloadClick,
            modifier = Modifier.padding(top = 16.dp, start = 32.dp, end = 32.dp)
        ) {
            Text(text = stringResource(R.string.art_collection_empty_error_reload))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContentPreview() {
    val model = ArtCollectionUiState.Content(
        refreshing = false,
        newPageState = PageState.None,
        hasNext = true,
        0,
        (1..5).map { index ->
            Item(
                ArtCollectionItem(
                    index.toString(),
                    "Painting $index",
                    "Painting from famous author $index",
                    "Author $index",
                    "https://www.google.com/favicon.ico",
                    1f
                )
            )
        })
    RijksmuseumTheme {
        Content({ _, _ -> }, { }, { }, { }, PaddingValues(), model)
    }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 300)
@Composable
fun EmptyProgressPreview() {
    EmptyProgress(PaddingValues())
}

@Preview(showBackground = true)
@Composable
fun PageProgressPreview() {
    PageProgress()
}

@Preview(showBackground = true, widthDp = 300, heightDp = 300)
@Composable
fun EmptyErrorPreview() {
    EmptyError(PaddingValues(), "Can't connect to server") { }
}

@Preview(showBackground = true)
@Composable
fun PageErrorPreview() {
    PageError("Can't connect to server") { }
}

@Preview(showBackground = true, widthDp = 150)
@Composable
fun ArtCollectionItemPreview() {
    ArtItem(
        onItemClick = { _, _ -> },
        item = ArtCollectionItem(
            "1",
            "Painting",
            "Painting from The Famous Author",
            "The Famous Author",
            null,
            null
        )
    )
}

@Preview(showBackground = true)
@Composable
fun ArtCollectionHeaderPreview() {
    ArtHeader(title = "The Famous Author")
}