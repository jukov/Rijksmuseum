package info.jukov.rijksmuseum.feature.art.details.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import info.jukov.rijksmuseum.Const
import info.jukov.rijksmuseum.feature.art.details.domain.ArtDetailsRepository
import info.jukov.rijksmuseum.feature.art.details.presentation.model.ArtDetailsUiState
import info.jukov.rijksmuseum.feature.art.details.presentation.model.ArtDetailsUiState.Content
import info.jukov.rijksmuseum.feature.art.details.presentation.model.ArtDetailsUiState.Error
import info.jukov.rijksmuseum.feature.art.details.presentation.model.ArtDetailsUiState.Progress
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

    private val mutableModel = MutableLiveData<ArtDetailsUiState>(Progress)
    val model: LiveData<ArtDetailsUiState> = mutableModel

    fun init() {
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

    fun reload() {
        mutableModel.postValue(Progress)
        load()
    }

    override fun onCleared() {
        disposable?.dispose()
    }
}