package com.example.githubClient.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.githubClient.R
import com.example.githubClient.core.glide.GlideApp
import com.example.githubClient.data.model.GithubBaseUser
import com.example.githubClient.data.model.GithubUserWithLocalData

class GithubUserAdapter : PagingDataAdapter<GithubUserWithLocalData, GithubUserAdapter.ViewHolder>(USER_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate your custom view and return the ViewHolder
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.github_user_row, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        if (user != null) {
            holder.name.text = user.githubUser.login
            GlideApp
                .with(holder.itemView.context)
                .load(user.githubUser.avatar_url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(holder.avatar)
        }
    }



    class ViewHolder(view:View) : RecyclerView.ViewHolder(view) {
        internal val name:AppCompatTextView = view.findViewById(R.id.name)
        internal val avatar:AppCompatImageView = view.findViewById(R.id.user_image)
    }


    companion object {


        private val USER_COMPARATOR = object : DiffUtil.ItemCallback<GithubUserWithLocalData>() {
            override fun areItemsTheSame(oldItem: GithubUserWithLocalData, newItem: GithubUserWithLocalData): Boolean =
                (oldItem.githubUser.id == newItem.githubUser.id) && (oldItem.githubUser.login == newItem.githubUser.login)

            override fun areContentsTheSame(oldItem: GithubUserWithLocalData, newItem: GithubUserWithLocalData): Boolean =
                oldItem == newItem
        }
    }
}
