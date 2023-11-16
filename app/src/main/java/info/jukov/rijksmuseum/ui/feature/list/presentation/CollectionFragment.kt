package info.jukov.rijksmuseum.ui.feature.list.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import info.jukov.rijksmuseum.ui.feature.list.presentation.model.CollectionItem
import info.jukov.rijksmuseum.ui.theme.RijksmuseumTheme

@AndroidEntryPoint
class CollectionFragment : Fragment() {

    private val viewModel: CollectionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                RijksmuseumTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val listState = rememberLazyListState()

                        val modelState = viewModel.model.observeAsState()

                        Content(listState, modelState)
                    }
                }
            }
        }
    }

    @Composable
    private fun Content(
        listState: LazyListState,
        modelState: State<List<CollectionItem>?>
    ) {
        LazyColumn(state = listState) {
            modelState.value?.let { model ->
                itemsIndexed(model) { _, item ->
                    Column(modifier = Modifier.padding(8.dp)) { // TODO Material 3
                        Text(
                            text = item.name,
                            fontSize = 18.sp
                        )
                        Text(
                            text = item.description,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        val model = remember {
            mutableStateOf((1..20).map { index ->
                CollectionItem(
                    index,
                    "Painting $index",
                    "Painting from famous author $index"
                )
            })
        }
        RijksmuseumTheme {
            Content(rememberLazyListState(), model)
        }
    }
}