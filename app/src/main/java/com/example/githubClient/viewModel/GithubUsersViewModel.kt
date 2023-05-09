package com.example.githubClient.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.githubClient.core.GithubApp
import com.example.githubClient.core.architecture.viewModel.MavericksAssistedViewModelFactory
import com.example.githubClient.core.architecture.viewModel.GViewModel
import com.example.githubClient.core.network.NetworkStatus
import com.example.githubClient.core.network.NetworkStatusLiveData
import com.example.githubClient.data.api.GithubEndpoint
import com.example.githubClient.data.db.GithubDatabase
import com.example.githubClient.data.model.GithubBaseUser
import com.example.githubClient.data.model.GithubUserWithLocalData
import com.example.githubClient.data.repo.GithubUsersRepository
import com.example.githubClient.viewModel.viewAction.GithubUsersViewAction
import com.example.githubClient.viewModel.viewEvent.GithubUsersViewEvent
import com.example.githubClient.viewModel.viewState.GithubUsersViewState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch

class GithubUsersViewModel @AssistedInject constructor(
    @Assisted private val initialState: GithubUsersViewState,
) : GViewModel<GithubUsersViewState, GithubUsersViewAction, GithubUsersViewEvent>(initialState) {

    val queriedUsers: MutableLiveData<List<GithubUserWithLocalData>> = MediatorLiveData()

    override fun handle(action: GithubUsersViewAction) {
        when (action) {
            is GithubUsersViewAction.NetworkStatusChanged -> {
                setState { copy(networkStatus = action.networkStatus) }
            }
            is GithubUsersViewAction.QueryUsers           -> {
                viewModelScope.launch {
                    if(action.query.isEmpty())
                        queriedUsers.value = listOf()
                    else {
                        val sqlQuery = "*${action.query}*"
                        queriedUsers.value = GithubUsersRepository.searchForUsers(sqlQuery)
                    }
                    setState { copy(searchQuery = action.query) }
                }
            }
        }
    }

    suspend fun getUsers() = GithubUsersRepository.getUsers().cachedIn(viewModelScope)

    @AssistedFactory
    interface Factory : MavericksAssistedViewModelFactory<GithubUsersViewModel, GithubUsersViewState> {
        override fun create(initialState: GithubUsersViewState): GithubUsersViewModel
    }

    companion object {
        const val CONSTANTS_DEFAULT_PAGE_SIZE: Int = 30
    }
}