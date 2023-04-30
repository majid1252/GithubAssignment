package com.example.githubClient

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.DeliveryMode
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.RedeliverOnStart
import com.airbnb.mvrx.viewModel
import com.example.githubClient.core.platform.BaseActivity
import com.example.githubClient.ui.adapter.GithubUserAdapter
import com.example.githubClient.databinding.ActivityMainBinding
import com.example.githubClient.ui.adapter.LoadStateFooterAdapter
import com.example.githubClient.viewModel.GithubUsersGViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(),MavericksView {

    private val githubViewModel: GithubUsersGViewModel by viewModel()
    private lateinit var githubUserAdapter: GithubUserAdapter

    override fun getBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        githubUserAdapter = GithubUserAdapter()

        val recyclerView = views.rvUserScheduler
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = githubUserAdapter.withLoadStateFooter(LoadStateFooterAdapter())

        lifecycleScope.launch {
            githubViewModel
                .users
                .distinctUntilChanged()
                .collectLatest(RedeliverOnStart) { pagingData ->
                    githubUserAdapter.submitData(pagingData)
                }
        }
    }
}