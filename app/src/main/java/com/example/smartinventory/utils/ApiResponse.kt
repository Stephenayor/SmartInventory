package com.example.smartinventory.utils

sealed class ApiResponse<out T> {
    data object Idle : ApiResponse<Nothing>()
    data object Loading: ApiResponse<Nothing>()

    data class Success<out T>(
        val data: T?
    ): ApiResponse<T>()

    data class Failure(
        val error: Throwable?,
        val message: String?,
        val errorCode: Int? = null
    ) : ApiResponse<Nothing>()

    data class ErrorResponse(
        val status: String,
        val message: String,
        val errors: Map<String, List<String>>
    )


}