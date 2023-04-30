package com.example.githubClient.viewModel.viewState

import androidx.constraintlayout.motion.utils.ViewState
import com.airbnb.mvrx.MavericksState
import com.example.githubClient.core.network.NetworkStatus

data class GithubUsersViewState(val networkStatus: NetworkStatus) : MavericksState{
    constructor() : this(NetworkStatus.CONNECTED)
}
