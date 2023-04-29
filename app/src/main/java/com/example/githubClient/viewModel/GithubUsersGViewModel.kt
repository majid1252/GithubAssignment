package com.example.githubClient.viewModel

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.githubClient.core.GithubApp
import com.example.githubClient.core.architecture.viewModel.MavericksAssistedViewModelFactory
import com.example.githubClient.core.architecture.viewModel.GViewModel
import com.example.githubClient.data.api.GithubEndpoint
import com.example.githubClient.data.db.GithubDatabase
import com.example.githubClient.data.model.GithubBaseUser
import com.example.githubClient.data.repo.GithubUsersRepository
import com.example.githubClient.viewModel.viewAction.GithubUsersViewAction
import com.example.githubClient.viewModel.viewEvent.GithubUsersViewEvent
import com.example.githubClient.viewModel.viewState.GithubUsersViewState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow

class GithubUsersGViewModel @AssistedInject constructor(
    @Assisted private val initialState: GithubUsersViewState,
) : GViewModel<GithubUsersViewState, GithubUsersViewAction, GithubUsersViewEvent>(initialState) {
    private val githubUsersRepository = GithubUsersRepository(GithubEndpoint.githubApi , GithubDatabase.getInstance(GithubApp.getContext()))
    override fun handle(action: GithubUsersViewAction) {

    }

    val users: Flow<PagingData<GithubBaseUser>> = githubUsersRepository.getUsers().cachedIn(viewModelScope)

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<GithubUsersGViewModel, GithubUsersViewState> {
        override fun create(initialState: GithubUsersViewState): GithubUsersGViewModel
    }
}