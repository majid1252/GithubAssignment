package com.example.githubClient.core.architecture.viewModel

import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.MavericksViewModel

interface MavericksAssistedViewModelFactory<VM : MavericksViewModel<S>, S : MavericksState> {
    fun create(initialState: S): VM
}
