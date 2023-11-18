package info.jukov.rijksmuseum.feature.art.collection.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import info.jukov.rijksmuseum.Const
import info.jukov.rijksmuseum.feature.art.collection.domain.ArtCollectionRepository
import info.jukov.rijksmuseum.feature.art.collection.presentation.model.ArtCollectionUiModel
import info.jukov.rijksmuseum.feature.art.collection.presentation.model.ArtCollectionUiModel.Content
import info.jukov.rijksmuseum.feature.art.collection.presentation.model.ArtCollectionUiModel.EmptyError
import info.jukov.rijksmuseum.feature.art.collection.presentation.model.ArtCollectionUiModel.EmptyProgress
import info.jukov.rijksmuseum.feature.art.collection.presentation.model.PageState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

@HiltViewModel
class ArtCollectionViewModel @Inject constructor(
    private val repository: ArtCollectionRepository
) : ViewModel() {

    private var disposable: Disposable? = null

    private val mutableModel = MutableLiveData<ArtCollectionUiModel>(
        EmptyProgress
    )

    val model: LiveData<ArtCollectionUiModel> = mutableModel

    init {
        loadInitial()
    }

    private fun loadInitial() {
        if (disposable?.isDisposed == false) {
            return
        }
        disposable = repository.get(1)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { items ->
                    mutableModel.postValue(
                        Content(
                            refreshing = false,
                            newPageState = PageState.None,
                            hasNext = items.size == Const.Network.PAGE_SIZE,
                            lastLoadedPage = 1,
                            items = items
                        )
                    )
                },
                onError = { throwable ->
                    //TODO error mapper
                    mutableModel.postValue(EmptyError(throwable.message))
                }
            )
    }

    fun loadMore() {
        if (disposable?.isDisposed == false) {
            return
        }
        val current = mutableModel.value
        if (current !is Content) {
            error("Unexpected ui state ${current?.javaClass?.simpleName}")
        }
        if (!current.hasNext) {
            return
        }
        val newPage = current.lastLoadedPage + 1
        mutableModel.postValue(current.copy(newPageState = PageState.Loading))

        disposable = repository.get(newPage)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { items ->
                    mutableModel.postValue(
                        Content(
                            refreshing = false,
                            newPageState = PageState.None,
                            hasNext = items.size == Const.Network.PAGE_SIZE,
                            lastLoadedPage = newPage,
                            items = current.items + items
                        )
                    )
                },
                onError = { throwable ->
                    //TODO error mapper
                    mutableModel.postValue(
                        Content(
                            refreshing = false,
                            newPageState = PageState.Error(throwable.message),
                            hasNext = true,
                            lastLoadedPage = current.lastLoadedPage,
                            items = current.items
                        )
                    )
                }
            )
    }

    fun reload() {
        mutableModel.postValue(EmptyProgress)
        loadInitial()
    }

    fun reloadPage() {
        loadMore()
    }

    fun refresh() {
        val current = mutableModel.value
        if (current !is Content) {
            error("Unexpected ui state ${current?.javaClass?.simpleName}")
            return
        }
        mutableModel.postValue(current.copy(refreshing = true))
        loadInitial()
    }

    override fun onCleared() {
        disposable?.dispose()
    }

    companion object {
        private const val TAG = "ArtCollectionViewModel"
    }
}