package info.jukov.rijksmuseum.util.error

import androidx.annotation.StringRes

data class AppException(@StringRes val messageRes: Int): Exception()