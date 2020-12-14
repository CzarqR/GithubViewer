package com.myniprojects.githubviewer.network

sealed class ResponseState<out R>
{
    data class Success<out T>(val data: T) : ResponseState<T>()

    data class Error(val exception: Exception) : ResponseState<Nothing>()

    object Loading : ResponseState<Nothing>()

    object Sleep : ResponseState<Nothing>()
}