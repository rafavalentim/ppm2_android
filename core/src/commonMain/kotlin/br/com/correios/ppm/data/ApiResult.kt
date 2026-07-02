package br.com.correios.ppm.data

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(
        val status: Int? = null,
        val message: String? = null,
        val payload: String? = null,
        val cause: Throwable? = null
    ) : ApiResult<Nothing>()
}
