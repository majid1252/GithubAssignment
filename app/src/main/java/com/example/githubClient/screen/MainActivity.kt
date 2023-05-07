package com.example.githubClient.screen

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.RedeliverOnStart
import com.airbnb.mvrx.viewModel
import com.example.githubClient.core.network.NetworkStatus
import com.example.githubClient.core.platform.BaseActivity
import com.example.githubClient.data.adapter.GithubUserAdapter
import com.example.githubClient.data.adapter.GithubUsersSearchAdapter
import com.example.githubClient.databinding.ActivityMainBinding
import com.example.githubClient.data.adapter.LoadStateFooterAdapter
import com.example.githubClient.data.adapter.UserItemClickListener
import com.example.githubClient.data.model.GithubBaseUser
import com.example.githubClient.screen.userDetails.GithubUserDetailsFragment
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
class MainActivity : BaseActivity<ActivityMainBinding>(), MavericksView {

    // ViewModel and adapters
    private val githubUsersViewModel: GithubUsersViewModel by viewModel()
    private lateinit var githubUserAdapter: GithubUserAdapter
    private lateinit var githubUserSearchAdapter: GithubUsersSearchAdapter
    private lateinit var mSupportFragmentManager: FragmentManager

    // Inflating the view binding
    override fun getBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initializing the FragmentManager
        this.mSupportFragmentManager = supportFragmentManager

        // Initializing the adapters
        githubUserAdapter = GithubUserAdapter()
        githubUserSearchAdapter = GithubUsersSearchAdapter()

        // Setting the click listener for adapters
        githubUserAdapter.setUserClickListener(userClickListener)
        githubUserSearchAdapter.setUserClickListener(userClickListener)

        // Setting up the main RecyclerView
        val recyclerView = views.rvUserScheduler
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = githubUserAdapter.withLoadStateFooter(LoadStateFooterAdapter())

        // Setting up the search result RecyclerView
        val rvSearchResult = views.rvUserSearchResult
        rvSearchResult.layoutManager = LinearLayoutManager(this)
        rvSearchResult.adapter = githubUserSearchAdapter

        // Setting up the network state view
        views.networkState.setContent {
            ConnectingDotsWithText()
        }

        // Adding a text change listener for the search view
        views.usersSearchView.editText.addTextChangedListener {
            githubUsersViewModel.handle(GithubUsersViewAction.QueryUsers(it.toString()))
        }

        // Handling the back press for search view
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (views.usersSearchView.isShowing) {
                    views.usersSearchView.hide()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })

        // Observing the search query changes
        githubUsersViewModel.onEach(GithubUsersViewState::searchQuery) { query ->
            if (query.isEmpty())
                rvSearchResult.scrollToPosition(0)
        }

        // Observing the network status
        githubUsersViewModel.onEach(GithubUsersViewState::networkStatus) { state ->
            when (state) {
                NetworkStatus.CONNECTED    -> {
                    views.networkState.slideOut()
                }

                NetworkStatus.DISCONNECTED -> {
                    views.networkState.slideIn()
                }
            }
        }

        // Observing the queried users
        githubUsersViewModel.queriedUsers.observe(this) {
            githubUserSearchAdapter.differ.submitList(it)
        }

        // Collecting the user data and submitting it to the adapter
        lifecycleScope.launch {
            githubUsersViewModel
                .getUsers()
                .distinctUntilChanged()
                .collectLatest(RedeliverOnStart) { pagingData ->
                    githubUserAdapter.submitData(pagingData)
                }
        }

        // Observing the network status changes
        networkStatus.observe(this) {
            githubUsersViewModel.handle(GithubUsersViewAction.NetworkStatusChanged(it))
        }

        // Observing the user click events for back press
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (views.usersSearchView.isShowing)
                    views.usersSearchView.hide()
                else {
                    if (mSupportFragmentManager.backStackEntryCount > 0)
                        this@MainActivity.supportFragmentManager.popBackStack()
                    else
                        finish()
                }
            }
        })
    }

    // Click listener for user items
    private val userClickListener = object : UserItemClickListener {
        override fun onUserClicked(user: GithubBaseUser, avatar: AppCompatImageView) {
            // close search bar
            if (views.usersSearchView.isShowing)
                views.usersSearchView.hide()
            // Start user details fragment
            val fragment = GithubUserDetailsFragment.newInstance(user.id, user.login)
            val transaction = mSupportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            transaction.add(views.userDetailFragmentContainer.id, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}