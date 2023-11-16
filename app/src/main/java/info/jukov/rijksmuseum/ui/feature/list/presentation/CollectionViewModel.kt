package info.jukov.rijksmuseum.ui.feature.list.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import info.jukov.rijksmuseum.ui.feature.list.presentation.model.CollectionItem
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(): ViewModel() {

    private val mutableModel = MutableLiveData<List<CollectionItem>>()
    val model: LiveData<List<CollectionItem>> = mutableModel

    init {
        mutableModel.postValue(
            (1..20).map { index ->
                CollectionItem(
                    index,
                    "Painting $index",
                    "Painting from famous author $index"
                )
            }
        )
    }
}