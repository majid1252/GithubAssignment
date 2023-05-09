package com.example.githubClient.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.githubClient.R
import com.example.githubClient.core.glide.GlideApp
import com.example.githubClient.core.glide.InvertColorTransformation
import com.example.githubClient.data.model.GithubUserWithLocalData
import com.example.githubClient.ui.utils.RoundedCornersTransformation

class GithubUserAdapter : PagingDataAdapter<GithubUserWithLocalData, GithubUserAdapter.ViewHolder>(USER_COMPARATOR) {

    private var userClickListener: UserItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate your custom view and return the ViewHolder
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.github_user_row, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        if (user != null) {
            // set name
            holder.name.text = "@${user.githubUser.login}"
            // set note icon visibility
            holder.note.visibility = if (user.localData?.note != null) View.VISIBLE else View.GONE
            // add necessary transformations to image here
            val transformations = mutableListOf<BitmapTransformation>(RoundedCornersTransformation(120f))
            if ((holder.absoluteAdapterPosition + 1) % 4 == 0)
                transformations.add(InvertColorTransformation())
            // load image with Glide
            GlideApp
                .with(holder.itemView.context)
                .load(user.githubUser.avatar_url)
                .transform(
                    MultiTransformation(transformations)
                ).transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(holder.avatar)
            // set click listener for user row here (to navigate to user details)
            holder.itemView.setOnClickListener {
                userClickListener?.onUserClicked(user.githubUser , holder.avatar)
            }
        }
    }

    fun setUserClickListener(userClickListener: UserItemClickListener) {
        this.userClickListener = userClickListener
    }

    class ViewHolder(view:View) : RecyclerView.ViewHolder(view) {
        internal val name:AppCompatTextView = view.findViewById(R.id.name)
        internal val avatar:AppCompatImageView = view.findViewById(R.id.user_image)
        internal val note:AppCompatImageView = view.findViewById(R.id.data_availability_indicator)
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
