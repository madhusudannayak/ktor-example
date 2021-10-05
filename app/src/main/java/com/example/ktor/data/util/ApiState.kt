package com.example.ktor.data.util

import com.example.ktor.data.Post

sealed class ApiState {
    object Empty : ApiState()
    class Failure(val msg: Throwable) : ApiState()
    class Success(val data: List<Post>) : ApiState()
    object Loading : ApiState()

}