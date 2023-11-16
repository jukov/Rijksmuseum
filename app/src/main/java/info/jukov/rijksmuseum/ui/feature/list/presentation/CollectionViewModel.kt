package info.jukov.rijksmuseum.ui.feature.list.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import info.jukov.rijksmuseum.ui.feature.list.presentation.model.CollectionItem

class CollectionViewModel: ViewModel() {

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

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory { // TODO DI
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                return CollectionViewModel() as T
            }
        }
    }
}