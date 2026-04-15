package com.codework.dailyenglish.core.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

interface Dispatcher {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val unconfined: CoroutineDispatcher
}