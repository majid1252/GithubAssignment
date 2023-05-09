package com.example.githubClient.viewModel.viewState

sealed class DataFetchState<out T> {
    object Loading : DataFetchState<Nothing>()
    data class Success<T>(val data: T) : DataFetchState<T>()
    data class Error(val message: String) : DataFetchState<Nothing>()
}
