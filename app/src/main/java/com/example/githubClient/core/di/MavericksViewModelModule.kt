package com.example.githubClient.core.di

import com.example.githubClient.core.architecture.viewModel.MavericksAssistedViewModelFactory
import com.example.githubClient.viewModel.GithubUsersViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.multibindings.IntoMap

@InstallIn(MavericksViewModelComponent::class)
@Module
interface MavericksViewModelModule {

    @Binds
    @IntoMap
    @MavericksViewModelKey(GithubUsersViewModel::class)
    fun copyTaskViewModelFactory(factory: GithubUsersViewModel.Factory): MavericksAssistedViewModelFactory<*, *>

}
