package com.example.githubClient.viewModel.viewAction

import com.example.githubClient.core.architecture.viewModel.ViewModelAction
import com.example.githubClient.core.network.NetworkStatus

sealed class GithubUsersViewAction : ViewModelAction {
    data class NetworkStatusChanged(val networkStatus: NetworkStatus) : GithubUsersViewAction()
}