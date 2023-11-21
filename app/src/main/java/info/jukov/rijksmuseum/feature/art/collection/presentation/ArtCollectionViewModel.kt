package info.jukov.rijksmuseum.feature.art.collection.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import info.jukov.rijksmuseum.Const
import info.jukov.rijksmuseum.feature.art.collection.domain.ArtCollectionRepository
import info.jukov.rijksmuseum.feature.art.collection.domain.model.ArtCollectionItem
import info.jukov.rijksmuseum.feature.art.collection.presentation.model.ArtCollectionUiModel
import info.jukov.rijksmuseum.feature.art.collection.presentation.model.ArtCollectionUiModel.Header
import info.jukov.rijksmuseum.feature.art.collection.presentation.model.ArtCollectionUiModel.Item
import info.jukov.rijksmuseum.feature.art.collection.presentation.model.ArtCollectionUiState
import info.jukov.rijksmuseum.feature.art.collection.presentation.model.ArtCollectionUiState.Content
import info.jukov.rijksmuseum.feature.art.collection.presentation.model.ArtCollectionUiState.EmptyError
import info.jukov.rijksmuseum.feature.art.collection.presentation.model.ArtCollectionUiState.EmptyProgress
import info.jukov.rijksmuseum.feature.art.collection.presentation.model.PageState
import info.jukov.rijksmuseum.util.error.AppException
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

@HiltViewModel
class ArtCollectionViewModel @Inject constructor(
    private val repository: ArtCollectionRepository
) : ViewModel() {

    private var disposable: Disposable? = null

    private val mutableModel = MutableLiveData<ArtCollectionUiState>(EmptyProgress)
    val model: LiveData<ArtCollectionUiState> = mutableModel

    private val mutableError = MutableLiveData<String?>()
    val error: LiveData<String?> = mutableError

    init {
        loadInitial()
    }

    private fun loadInitial() {
        if (disposable?.isDisposed == false) {
            return
        }
        val current = mutableModel.value

        disposable = repository.get(1)
            .map { items ->
                mapToUiModel(items)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { (items, size) ->
                    mutableModel.postValue(
                        Content(
                            refreshing = false,
                            newPageState = PageState.None,
                            hasNext = size == Const.Network.PAGE_SIZE,
                            lastLoadedPage = 1,
                            items = items
                        )
                    )
                },
                onError = { throwable ->
                    if (current is Content) {
                        mutableError.postValue(throwable.message)
                        mutableModel.postValue(current.copy(refreshing = false,))
                    } else {
                        mutableModel.postValue(EmptyError((throwable as? AppException)?.messageRes))
                    }
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
            .map { items ->
                mapToUiModel(items, current.items)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { (items, size) ->
                    mutableModel.postValue(
                        Content(
                            refreshing = false,
                            newPageState = PageState.None,
                            hasNext = size == Const.Network.PAGE_SIZE,
                            lastLoadedPage = newPage,
                            items = items
                        )
                    )
                },
                onError = { throwable ->
                    mutableModel.postValue(
                        Content(
                            refreshing = false,
                            newPageState = PageState.Error((throwable as? AppException)?.messageRes),
                            hasNext = true,
                            lastLoadedPage = current.lastLoadedPage,
                            items = current.items
                        )
                    )
                }
            )
    }

    private fun mapToUiModel(
        newItems: List<ArtCollectionItem>,
        currentItems: List<ArtCollectionUiModel> = emptyList()
    ): Pair<ArrayList<ArtCollectionUiModel>, Int> {
        val output = ArrayList<ArtCollectionUiModel>(currentItems)
        var lastAuthor: String? = (output.lastOrNull() as? Item)?.item?.author
        newItems.forEach { item ->
            if (item.author != null && lastAuthor != item.author) {
                lastAuthor = item.author
                output += Header(item.author)
            }
            output += Item(item)
        }
        return output to newItems.size
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
        }
        mutableModel.postValue(current.copy(refreshing = true))
        loadInitial()
    }

    fun consumeError() {
        mutableError.postValue(null)
    }

    override fun onCleared() {
        disposable?.dispose()
    }
}