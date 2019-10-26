package com.example.cgv.model

class Resource<T> private constructor(
    val status: Int = 0,
    val data: T? = null,
    val message: String? = null
) {
    companion object {
        const val LOADING = 1
        const val SUCCESS = 2
        const val ERROR = 3

        fun <T> success(data: T?, message: String?): Resource<T> {
            return Resource(
                SUCCESS,
                data,
                message
            )
        }

        fun <T> loading(data: T?, message: String?): Resource<T> {
            return Resource(
                LOADING,
                data,
                message
            )
        }

        fun <T> error(data: T?, message: String?): Resource<T> {
            return Resource(
                ERROR,
                data,
                message
            )
        }
    }
}
