package com.example.githubClient.screen.userDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.example.githubClient.viewModel.GithubUserDetailsViewModel

class GithubUserDetailsFragment : Fragment(){

    private val githubUsersViewModel by viewModels<GithubUserDetailsViewModel>()

    companion object {

        private const val USER_ID_PARAM = "user_id_param"
        private const val USERNAME_PARAM = "username_param"

        fun newInstance(userId : Int , username: String) = GithubUserDetailsFragment().apply {
            val args = Bundle()
            args.putInt(USER_ID_PARAM, userId)
            args.putString(USERNAME_PARAM, username)
            this.arguments = args
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                val userId = arguments?.getInt(USER_ID_PARAM) ?: 0
                val username = arguments?.getString(USERNAME_PARAM) ?: ""
                GithubUserDetailsScreen(githubUsersViewModel , userId , username)
            }
        }
    }

}