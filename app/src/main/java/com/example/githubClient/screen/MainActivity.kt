package com.example.githubClient.screen

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.RedeliverOnStart
import com.airbnb.mvrx.viewModel
import com.example.githubClient.R
import com.example.githubClient.core.network.NetworkStatus
import com.example.githubClient.core.platform.BaseActivity
import com.example.githubClient.data.adapter.GithubUserAdapter
import com.example.githubClient.data.adapter.GithubUsersSearchAdapter
import com.example.githubClient.databinding.ActivityMainBinding
import com.example.githubClient.data.adapter.LoadStateFooterAdapter
import com.example.githubClient.data.adapter.UserItemClickListener
import com.example.githubClient.data.model.GithubBaseUser
import com.example.githubClient.ui.components.ConnectingDotsWithText
import com.example.githubClient.ui.motion.AppMotions
import com.example.githubClient.ui.utils.slideIn
import com.example.githubClient.ui.utils.slideOut
import com.example.githubClient.viewModel.GithubUsersViewModel
import com.example.githubClient.viewModel.viewAction.GithubUsersViewAction
import com.example.githubClient.viewModel.viewState.GithubUsersViewState
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(),MavericksView {

    private val githubUsersViewModel: GithubUsersViewModel by viewModel()
    private lateinit var githubUserAdapter: GithubUserAdapter
    private lateinit var githubUserSearchAdapter: GithubUsersSearchAdapter
    private lateinit var mSupportFragmentManager: FragmentManager
    override fun getBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.mSupportFragmentManager = supportFragmentManager

        githubUserAdapter = GithubUserAdapter()
        githubUserSearchAdapter = GithubUsersSearchAdapter()

        githubUserAdapter.setUserClickListener(userClickListener)
        githubUserSearchAdapter.setUserClickListener(userClickListener)

        val recyclerView = views.rvUserScheduler
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = githubUserAdapter.withLoadStateFooter(LoadStateFooterAdapter())

        val rvSearchResult = views.rvUserSearchResult
        rvSearchResult.layoutManager = LinearLayoutManager(this)
        rvSearchResult.adapter = githubUserSearchAdapter

        views.networkState.setContent {
            ConnectingDotsWithText()
        }

        views.usersSearchView.editText.addTextChangedListener {
            githubUsersViewModel.handle(GithubUsersViewAction.QueryUsers(it.toString()))
        }

        onBackPressedDispatcher.addCallback(this , object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // close search view on back pressed
                if (views.usersSearchView.isShowing) {
                    views.usersSearchView.hide()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })

        githubUsersViewModel.onEach(GithubUsersViewState::searchQuery) { query ->
            if(query.isEmpty())
                rvSearchResult.scrollToPosition(0)
        }

        githubUsersViewModel.onEach(GithubUsersViewState::networkStatus) { state ->
            when (state) {
                NetworkStatus.CONNECTED         -> {
                    views.networkState.slideOut()
                }
                NetworkStatus.DISCONNECTED -> {
                    views.networkState.slideIn()
                }
            }
        }

        githubUsersViewModel.queriedUsers.observe(this) {
            githubUserSearchAdapter.differ.submitList(it)
        }

        lifecycleScope.launch {
            githubUsersViewModel
                .users
                .distinctUntilChanged()
                .collectLatest(RedeliverOnStart) { pagingData ->
                    githubUserAdapter.submitData(pagingData)
                }
        }

        networkStatus.observe(this) {
            githubUsersViewModel.handle(GithubUsersViewAction.NetworkStatusChanged(it))
        }
    }

    private val userClickListener = object : UserItemClickListener {
        override fun onUserClicked(user: GithubBaseUser, avatar: AppCompatImageView) {
            // Transition to the next activity with shared element transition animation and add to back stack
            val intent = Intent(this@MainActivity, GithubUserDetailsActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(this@MainActivity, avatar, "user_image_avatar")
            startActivity(intent, options.toBundle())
        }
    }

}