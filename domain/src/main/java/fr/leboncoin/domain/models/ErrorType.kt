package fr.leboncoin.domain.models

sealed class ErrorType {
    data object ConnectivityError : ErrorType()
    data object UnknownError : ErrorType()
    data object ServerError : ErrorType()
}