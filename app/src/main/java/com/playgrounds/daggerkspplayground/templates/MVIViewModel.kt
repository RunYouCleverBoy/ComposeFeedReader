package com.playgrounds.daggerkspplayground.templates

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class MVIViewModel<STATE, EVENT, ACTION>(initialState: STATE): ViewModel() {
    protected val mutableStateFlow: MutableStateFlow<STATE> = MutableStateFlow(initialState)
    val stateFlow: StateFlow<STATE> = mutableStateFlow

    abstract fun dispatchEvent(event: EVENT)

    private val _actionFlow: MutableSharedFlow<ACTION> = MutableSharedFlow(extraBufferCapacity = 10)
    val actionFlow: SharedFlow<ACTION> = _actionFlow

    protected fun emitAction(action: ACTION) {
        _actionFlow.tryEmit(action)
    }
}