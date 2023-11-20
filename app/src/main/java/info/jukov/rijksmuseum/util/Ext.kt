package info.jukov.rijksmuseum.util

import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState

fun LazyStaggeredGridState.shouldLoadMore(): Boolean {
    return firstVisibleItemIndex > layoutInfo.totalItemsCount - 10 || !canScrollForward
}