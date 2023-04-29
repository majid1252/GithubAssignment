package com.example.githubClient.core.architecture.viewModel

import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel

abstract class GViewModel<S : MavericksState, VA : ViewModelAction, VE : ViewEvent>(initialState: S) :
        MavericksViewModel<S>(initialState) {

    // Used to post transient events to the View
    val viewEvents = EventQueue<VE>(capacity = 64)

    abstract fun handle(action: VA)
}
