package info.jukov.rijksmuseum.feature.art.details.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
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
import info.jukov.rijksmuseum.feature.art.details.domain.model.ArtDetails
import info.jukov.rijksmuseum.feature.art.details.presentation.model.ArtDetailsUiModel

@Composable
fun ArtDetailsScreen(
    viewModel: ArtDetailsViewModel = hiltViewModel()
) {
    val modelState = viewModel.model.observeAsState()

    modelState.value?.let { model ->
        when (model) {
            is ArtDetailsUiModel.Content ->
                Content(
                    model = model.data
                )

            ArtDetailsUiModel.Progress ->
                Progress()

            is ArtDetailsUiModel.Error ->
                Error(
                    message = model.message,
                    onReloadClick = {
                        viewModel.reload()
                    }
                )
        }
    }
}

@Composable
private fun Content(
    model: ArtDetails
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = model.title,
            fontSize = 20.sp
        )
        if (model.author != null) {
            Text(
                text = model.author,
                fontSize = 18.sp
            )
        }
        if (model.description != null) {
            Text(
                text = model.description,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        if (model.longTitle != null) {
            Text(
                text = model.longTitle,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        if (model.scLabelLine != null) {
            Text(
                text = model.scLabelLine,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        if (model.subTitle != null) {
            Text(
                text = model.subTitle,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun Progress() {
    //TODO shimmer
    //TODO animate change
    Box(contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun Error(
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

@Preview(showBackground = true, widthDp = 300, heightDp = 300)
@Composable
fun ProgressPreview() {
    Progress()
}

@Preview(showBackground = true, widthDp = 300, heightDp = 300)
@Composable
fun ErrorPreview() {
    Error("Can't connect to server", {})
}