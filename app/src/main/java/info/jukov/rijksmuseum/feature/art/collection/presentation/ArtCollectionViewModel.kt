package info.jukov.rijksmuseum.feature.art.collection.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import info.jukov.rijksmuseum.feature.art.collection.domain.ArtCollectionRepository
import info.jukov.rijksmuseum.feature.art.collection.domain.model.ArtCollectionItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

@HiltViewModel
class ArtCollectionViewModel @Inject constructor(
    repository: ArtCollectionRepository
): ViewModel() {

    private var disposable: Disposable? = null

    private val mutableModel = MutableLiveData<List<ArtCollectionItem>>()
    val model: LiveData<List<ArtCollectionItem>> = mutableModel

    init {
        disposable = repository.get()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { model ->
                    mutableModel.postValue(model)
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