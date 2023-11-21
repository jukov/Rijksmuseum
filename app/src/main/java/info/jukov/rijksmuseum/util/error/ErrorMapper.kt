package info.jukov.rijksmuseum.util.error

import info.jukov.rijksmuseum.R
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.security.cert.CertPathValidatorException
import javax.inject.Inject

class ErrorMapper @Inject constructor() {

    fun map(throwable: Throwable): AppException =
        when (throwable) {
            is SocketTimeoutException -> AppException(R.string.error_timeout)
            is UnknownHostException -> AppException(R.string.error_cant_find_site)
            is CertPathValidatorException -> AppException(R.string.error_certificate)
            is IOException -> AppException(R.string.error_network)
            is HttpException -> AppException(R.string.error_server)
            else -> AppException(R.string.error_undocumented)
        }
}