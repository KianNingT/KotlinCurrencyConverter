package com.example.kotlincurrencyconverter.util

sealed class Resource<T>(val data: T?, val errorMessage: String?) {

    class Success<T>(data: T): Resource<T> (data, null)

    class Error<T>(errMessage: String): Resource<T> (null, errMessage)
}