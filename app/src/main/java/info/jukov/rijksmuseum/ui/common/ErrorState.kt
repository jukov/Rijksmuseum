package info.jukov.rijksmuseum.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.jukov.rijksmuseum.R

@Composable
fun ErrorState(
    outerPadding: PaddingValues,
    message: Int?,
    onReloadClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(outerPadding)
            .fillMaxSize()
            .testTag("ErrorState")
    ) {
        Text(
            text = stringResource(R.string.art_collection_empty_error),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp, start = 32.dp, end = 32.dp),
            maxLines = 5,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = stringResource(message ?: R.string.error_undocumented),
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

@Preview(showBackground = true, widthDp = 300, heightDp = 600)
@Composable
fun ErrorStatePreview() {
    ErrorState(PaddingValues(), R.string.error_network, {})
}