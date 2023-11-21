package info.jukov.rijksmuseum.feature.art.details.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import info.jukov.rijksmuseum.Const
import info.jukov.rijksmuseum.feature.art.details.domain.ArtDetailsRepository
import info.jukov.rijksmuseum.feature.art.details.presentation.model.ArtDetailsUiModel
import info.jukov.rijksmuseum.feature.art.details.presentation.model.ArtDetailsUiModel.Content
import info.jukov.rijksmuseum.feature.art.details.presentation.model.ArtDetailsUiModel.Error
import info.jukov.rijksmuseum.feature.art.details.presentation.model.ArtDetailsUiModel.Progress
import info.jukov.rijksmuseum.util.error.AppException
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

@HiltViewModel
class ArtDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: ArtDetailsRepository
): ViewModel() {

    private val id: String = requireNotNull(savedStateHandle[Const.Keys.ITEM_ID])

    private var disposable: Disposable? = null

    private val mutableModel = MutableLiveData<ArtDetailsUiModel>(Progress)
    val model: LiveData<ArtDetailsUiModel> = mutableModel

    init {
        load()
    }

    private fun load() {
        if (disposable?.isDisposed == false) {
            return
        }
        disposable = repository.get(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { model ->
                    mutableModel.postValue(Content(model))
                },
                onError = { throwable ->
                    mutableModel.postValue(Error((throwable as? AppException)?.messageRes))
                }
            )
    }

    override fun onCleared() {
        disposable?.dispose()
    }

    fun reload() {
        mutableModel.postValue(Progress)
        load()
    }
}