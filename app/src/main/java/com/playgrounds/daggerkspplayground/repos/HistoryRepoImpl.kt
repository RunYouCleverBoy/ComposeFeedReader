package com.playgrounds.daggerkspplayground.repos

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryRepoImpl @Inject constructor() : HistoryRepo {
    private val _historyItems = MutableStateFlow(HistoryRepo.HistoryItem(""))
    override val historyItems: StateFlow<HistoryRepo.HistoryItem> = _historyItems


    override fun setLastSurfedName(name: String) {
        _historyItems.value = HistoryRepo.HistoryItem(name)
    }
}