package info.jukov.rijksmuseum.feature.art.details.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.pluralStringResource
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
import info.jukov.rijksmuseum.feature.art.details.domain.model.ArtDetails
import info.jukov.rijksmuseum.feature.art.details.presentation.model.ArtDetailsUiModel
import info.jukov.rijksmuseum.util.shimmerLoadingAnimation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtDetailsScreen(
    onBackClick: () -> Unit,
    itemName: String,
    viewModel: ArtDetailsViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val modelState = viewModel.model.observeAsState()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Text(
                        text = itemName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.art_details_back_content_description),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        AnimatedVisibility(
            visible = modelState.value is ArtDetailsUiModel.Content,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Content(
                outerPadding = innerPadding,
                item = (modelState.value as ArtDetailsUiModel.Content).data
            )
        }

        AnimatedVisibility(
            visible = modelState.value is ArtDetailsUiModel.Progress,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Progress(
                outerPadding = innerPadding
            )
        }

        var lastErrorMessage by remember {
            mutableStateOf((modelState.value as? ArtDetailsUiModel.Error)?.message)
        }

        (modelState.value as? ArtDetailsUiModel.Error)?.message?.let { message ->
            lastErrorMessage = message
        }

        AnimatedVisibility(
            visible = modelState.value is ArtDetailsUiModel.Error,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Error(
                outerPadding = innerPadding,
                message = lastErrorMessage,
                onReloadClick = {
                    viewModel.reload()
                }
            )
        }
    }
}

@Composable
private fun Content(
    outerPadding: PaddingValues,
    item: ArtDetails
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .padding(outerPadding)
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            val groupTitleStyle = MaterialTheme.typography.headlineSmall

            if (item.imageUrl != null && item.imageAspectRatio != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.art_collection_object_image_content_description),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .fillMaxWidth()
                        .aspectRatio(item.imageAspectRatio)
                )
            } else {
                Card(
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.art_collection_object_image_fallback),
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 24.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
            Text(
                text = item.title,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
            if (item.description != null) {
                Text(
                    text = stringResource(id = R.string.art_details_description),
                    style = groupTitleStyle,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = stringResource(id = R.string.art_details_description_group_creation),
                style = groupTitleStyle,
                modifier = Modifier.padding(top = 16.dp)
            )
            if (item.authors.isNotEmpty()) {
                DescriptionRow(
                    title = pluralStringResource(id = R.plurals.author, count = item.authors.size),
                    items = item.authors
                )
            }
            if (item.placesOfCreation?.isNotEmpty() == true) {
                DescriptionRow(
                    title = pluralStringResource(
                        id = R.plurals.place,
                        count = item.placesOfCreation.size
                    ),
                    items = item.placesOfCreation
                )
            }
            if (item.dateOfCreation != null) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(id = R.string.art_details_description_date),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .alignByBaseline()
                            .weight(1f)
                    )
                    Text(
                        text = item.dateOfCreation,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.alignByBaseline()
                    )
                }
            }
            Text(
                text = stringResource(id = R.string.art_details_description_group_material_and_technique),
                style = groupTitleStyle,
                modifier = Modifier.padding(top = 16.dp)
            )
            if (item.techniques.isNotEmpty()) {
                DescriptionRow(
                    title = pluralStringResource(
                        id = R.plurals.technique,
                        count = item.techniques.size
                    ),
                    items = item.techniques
                )
            }
            if (item.dimensions.isNotEmpty()) {
                DescriptionRow(
                    title = pluralStringResource(
                        id = R.plurals.measurement,
                        count = item.dimensions.size
                    ),
                    items = item.dimensions
                )
            }
        }
    }
}

@Composable
private fun DescriptionRow(
    title: String,
    items: List<String>
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(end = 8.dp)
                .alignByBaseline()
        )
        Column(
            modifier = Modifier
                .alignByBaseline()
                .fillMaxWidth()
        ) {
            items.forEach { item ->
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Suppress("NAME_SHADOWING")
@Composable
private fun Progress(
    outerPadding: PaddingValues
) {
    Box(modifier = Modifier.padding(outerPadding)) {
        Column(
            modifier = Modifier.padding(top = 24.dp, start = 24.dp, end = 24.dp)
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerLoadingAnimation(true)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(top = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerLoadingAnimation()
            )
            for (i in 1..5) {
                Spacer(
                    modifier = Modifier
                        .width(90.dp)
                        .height(38.dp)
                        .padding(top = 16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .shimmerLoadingAnimation()
                )
                for (i in 1..3) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(top = 8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .shimmerLoadingAnimation()
                    )
                }
            }
        }
    }
}

@Composable
private fun Error(
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
    Content(
        PaddingValues(),
        ArtDetails(
            "1",
            "Rijksmuseum app",
            listOf("Me", "My dog"),
            listOf("Amsterdam"),
            "20 Now, 2023",
            "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.",
            listOf("Kotlin", "Jetpack Compose"),
            listOf("KISS", "SOLID"),
            listOf("640KB"),
            null,
            null
        )
    )
}

@Preview(showBackground = true, widthDp = 300, heightDp = 300)
@Composable
fun ProgressPreview() {
    Progress(PaddingValues())
}

@Preview(showBackground = true, widthDp = 300, heightDp = 300)
@Composable
fun ErrorPreview() {
    Error(PaddingValues(), "Can't connect to server", {})
}