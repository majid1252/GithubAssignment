package com.example.githubClient.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import com.example.githubClient.R


class GithubUserDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.github_user_details)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment.
         *
         * @return A new instance of fragment GithubUserDetailsFragment.
         */
        @JvmStatic
        fun newInstance() =
            GithubUserDetailsActivity()
    }
}