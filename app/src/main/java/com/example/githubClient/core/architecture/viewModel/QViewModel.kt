package com.example.githubClient.core.architecture.viewModel

import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel

abstract class QViewModel<S : MavericksState, VA : QViewModelAction, VE : QViewEvent>(initialState: S) :
        MavericksViewModel<S>(initialState) {

    // Used to post transient events to the View
    val viewEvents = EventQueue<VE>(capacity = 64)

    abstract fun handle(action: VA)
}
