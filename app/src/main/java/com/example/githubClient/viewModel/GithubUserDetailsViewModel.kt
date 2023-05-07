package com.example.githubClient.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubClient.data.model.GithubDetailedUser
import com.example.githubClient.data.model.GithubUserLocalData
import com.example.githubClient.data.repo.GithubUsersRepository
import com.example.githubClient.viewModel.viewState.DataFetchState
import kotlinx.coroutines.launch

class GithubUserDetailsViewModel : ViewModel() {

    private val githubUserRepository = GithubUsersRepository
    private val _userDetails = MutableLiveData<DataFetchState<GithubDetailedUser>>()
    private val _userNote = MutableLiveData<String>()
    val userDetails: LiveData<DataFetchState<GithubDetailedUser>> = _userDetails
    val userNote: LiveData<String> = _userNote

    fun getUserDetails(id : Int , username: String) {
        viewModelScope.launch {
            githubUserRepository.getLocalUserData(id)?.let {
                _userNote.value = it.note ?: ""
            }
            githubUserRepository.getUserDetails(username)?.let { userDetails ->
                _userDetails.value = DataFetchState.Success(userDetails)
            }
            githubUserRepository.fetchUserDetails(username)?.let {
                viewModelScope.launch {
                    try {
                        _userDetails.value = DataFetchState.Loading
                        it.body()?.let { user ->
                            _userDetails.value = DataFetchState.Success(user)
                            githubUserRepository.updateUserDetails(user)
                        }
                    } catch (e: Exception) {
                        _userDetails.value = DataFetchState.Error("Failed to fetch user details")
                    }
                }
            }
        }
    }

    fun updateUserNote(githubUserLocalData: GithubUserLocalData) {
        viewModelScope.launch {
            if (githubUserLocalData.note.isNullOrEmpty())
                githubUserRepository.deleteUserNote(githubUserLocalData)
            else
                githubUserRepository.updateUserNote(githubUserLocalData)
        }
    }
}