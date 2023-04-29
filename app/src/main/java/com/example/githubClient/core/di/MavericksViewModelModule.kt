package com.example.githubClient.core.di

import com.example.githubClient.core.architecture.viewModel.MavericksAssistedViewModelFactory
import com.example.githubClient.viewModel.GithubUsersGViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.multibindings.IntoMap

@InstallIn(MavericksViewModelComponent::class)
@Module
interface MavericksViewModelModule {

    @Binds
    @IntoMap
    @MavericksViewModelKey(GithubUsersGViewModel::class)
    fun copyTaskViewModelFactory(factory: GithubUsersGViewModel.Factory): MavericksAssistedViewModelFactory<*, *>

}
