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
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.githubClient.R
import com.example.githubClient.core.glide.GlideApp
import com.example.githubClient.core.glide.InvertColorTransformation
import com.example.githubClient.data.model.GithubBaseUser
import com.example.githubClient.data.model.GithubUserWithLocalData
import com.example.githubClient.ui.utils.RoundedCornersTransformation

class GithubUsersSearchAdapter : RecyclerView.Adapter<GithubUsersSearchAdapter.ViewHolder>() {

    private var userClickListener: UserItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate your custom view and return the ViewHolder
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.github_user_search_row, parent, false))
    }

    override fun getItemCount() = differ.currentList.size

    val differ = AsyncListDiffer(this, UserSearchDiffUtil())

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = differ.currentList[position]
        // bind data to view
        // set name
        holder.name.text = user.githubUser.login
        // load image with Glide
        GlideApp
            .with(holder.itemView.context)
            .load(user.githubUser.avatar_url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .transform(RoundedCornersTransformation(120f))
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(holder.avatar)
        // set click listener
        holder.itemView.setOnClickListener {
            userClickListener?.onUserClicked(user.githubUser)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal val name: AppCompatTextView = view.findViewById(R.id.name)
        internal val avatar: AppCompatImageView = view.findViewById(R.id.user_image)
    }

    fun setUserClickListener(userClickListener: UserItemClickListener) {
        this.userClickListener = userClickListener
    }

    internal class UserSearchDiffUtil : DiffUtil.ItemCallback<GithubUserWithLocalData>() {
        override fun areItemsTheSame(oldItem: GithubUserWithLocalData, newItem: GithubUserWithLocalData): Boolean {
            return oldItem.githubUser.login == newItem.githubUser.login
        }

        override fun areContentsTheSame(oldItem: GithubUserWithLocalData, newItem: GithubUserWithLocalData): Boolean {
            return oldItem == newItem
        }
    }

}
