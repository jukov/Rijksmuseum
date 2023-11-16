package info.jukov.rijksmuseum.feature.list.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import info.jukov.rijksmuseum.feature.list.domain.CollectionRepository
import info.jukov.rijksmuseum.feature.list.domain.model.CollectionItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    repository: CollectionRepository
): ViewModel() {

    private var disposable: Disposable? = null

    private val mutableModel = MutableLiveData<List<CollectionItem>>()
    val model: LiveData<List<CollectionItem>> = mutableModel

    init {
        disposable = repository.get()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    mutableModel.postValue(
                        (1..20).map { index ->
                            CollectionItem(
                                index,
                                "Painting $index",
                                "Painting from famous author $index"
                            )
                        }
                    )
                },
                onError = {
                    TODO("Handle errors")
                }
            )
    }

    override fun onCleared() {
        disposable?.dispose()
    }
}