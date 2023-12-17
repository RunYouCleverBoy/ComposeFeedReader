package com.playgrounds.daggerkspplayground.screens.primaryscreen

import androidx.lifecycle.viewModelScope
import com.playgrounds.daggerkspplayground.repos.HistoryRepo
import com.playgrounds.daggerkspplayground.templates.MVIViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrimaryViewModel @Inject constructor(
    private val historyRepo: HistoryRepo
): MVIViewModel<PrimaryState, PrimaryEvent, PrimaryAction>(PrimaryState()) {
    init {
        viewModelScope.launch {
            historyRepo.historyItems.collect { historyItem ->
                mutableStateFlow.update {
                    it.copy(lastSurfData = historyItem.lastSurfedName)
                }
            }
        }
    }

    override fun dispatchEvent(event: PrimaryEvent) {
        when (event) {
            is PrimaryEvent.OnSurfDataClicked -> emitAction(PrimaryAction.NavigateTo("tabs"))
        }
    }
}

data class PrimaryState(val lastSurfData: String = "")
sealed class PrimaryEvent {
    data object OnSurfDataClicked: PrimaryEvent()
}

sealed class PrimaryAction {
    data class NavigateTo(val path: String): PrimaryAction()
}
