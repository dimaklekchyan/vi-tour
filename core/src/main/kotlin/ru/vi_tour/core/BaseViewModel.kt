package ru.vi_tour.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Date

abstract class BaseViewModel<ViewState, Action, Event>(initialState: ViewState): ViewModel() {

    private val _viewStates = MutableStateFlow(initialState)
    private val _viewActions = MutableStateFlow<Pair<Action?, Long>>(null to Date().time)

    protected var viewState: ViewState
        get() = _viewStates.value
        set(value) {
            _viewStates.tryEmit(value)
        }
    protected var viewAction: Action?
        get() = _viewActions.value.first
        set(value) {
            _viewActions.tryEmit(value to Date().time)
        }

    abstract fun obtainEvent(viewEvent: Event)

    @Composable
    fun collectActions(collect: (Action) -> Unit) {
        val viewAction = _viewActions.collectAsState()
        LaunchedEffect(key1 = viewAction.value) {
            viewAction.value.first?.let { action ->
                this@BaseViewModel.viewAction = null
                collect(action)
            }
        }
    }

    @Composable
    fun collectState(): State<ViewState> {
        return _viewStates.collectAsState()
    }
}