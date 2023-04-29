package com.example.githubClient.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.githubClient.R
import com.example.githubClient.data.model.GithubBaseUser

class GithubUserAdapter : PagingDataAdapter<GithubBaseUser, GithubUserAdapter.ViewHolder>(USER_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate your custom view and return the ViewHolder
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.github_user_row, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        if (user != null) {
            holder.name.text = user.login
        }
    }

    class ViewHolder(view:View) : RecyclerView.ViewHolder(view) {
        internal val name:AppCompatTextView = view.findViewById(R.id.name)
    }

    companion object {
        private val USER_COMPARATOR = object : DiffUtil.ItemCallback<GithubBaseUser>() {
            override fun areItemsTheSame(oldItem: GithubBaseUser, newItem: GithubBaseUser): Boolean =
                (oldItem.id == newItem.id) && (oldItem.login == newItem.login)

            override fun areContentsTheSame(oldItem: GithubBaseUser, newItem: GithubBaseUser): Boolean =
                oldItem == newItem
        }
    }
}
