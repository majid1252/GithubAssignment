package com.example.githubClient

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.RedeliverOnStart
import com.airbnb.mvrx.viewModel
import com.example.githubClient.core.network.NetworkStatus
import com.example.githubClient.core.platform.BaseActivity
import com.example.githubClient.data.adapter.GithubUserAdapter
import com.example.githubClient.databinding.ActivityMainBinding
import com.example.githubClient.data.adapter.LoadStateFooterAdapter
import com.example.githubClient.ui.components.ConnectingDotsAnimation
import com.example.githubClient.ui.components.ConnectingDotsWithText
import com.example.githubClient.ui.utils.slideIn
import com.example.githubClient.ui.utils.slideOut
import com.example.githubClient.viewModel.GithubUsersViewModel
import com.example.githubClient.viewModel.viewAction.GithubUsersViewAction
import com.example.githubClient.viewModel.viewState.GithubUsersViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(),MavericksView {

    private val githubViewModel: GithubUsersViewModel by viewModel()
    private lateinit var githubUserAdapter: GithubUserAdapter

    override fun getBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        githubUserAdapter = GithubUserAdapter()

        val recyclerView = views.rvUserScheduler
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = githubUserAdapter.withLoadStateFooter(LoadStateFooterAdapter())

        views.networkState.setContent {
            ConnectingDotsWithText()
        }

        githubViewModel.onEach(GithubUsersViewState::networkStatus) { state ->
            when (state) {
                NetworkStatus.CONNECTED         -> {
                    views.networkState.slideOut()
                }
                NetworkStatus.DISCONNECTED -> {
                    views.networkState.slideIn()
                }
            }
        }

        lifecycleScope.launch {
            githubViewModel
                .users
                .distinctUntilChanged()
                .collectLatest(RedeliverOnStart) { pagingData ->
                    githubUserAdapter.submitData(pagingData)
                }
        }

        networkStatus.observe(this) {
            githubViewModel.handle(GithubUsersViewAction.NetworkStatusChanged(it))
        }
    }
}