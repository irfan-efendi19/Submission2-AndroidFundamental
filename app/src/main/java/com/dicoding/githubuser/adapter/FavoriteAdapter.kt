package com.dicoding.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuser.data.entity.UserFavoriteEntity
import com.dicoding.githubuser.databinding.ItemHolderBinding

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteHolder>() {
    class FavoriteHolder(val binding: ItemHolderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: UserFavoriteEntity) {
            binding.TVItem.text = data.login
            Glide.with(itemView.context).load(data.avatar_url).into(binding.avatarIV)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteHolder {
        return FavoriteHolder(
            ItemHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    private val diffUtil = object : DiffUtil.ItemCallback<UserFavoriteEntity>() {
        override fun areItemsTheSame(
            oldItem: UserFavoriteEntity, newItem: UserFavoriteEntity
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: UserFavoriteEntity, newItem: UserFavoriteEntity
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: FavoriteHolder, position: Int) {
        val item = differ.currentList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onClick?.invoke(item)
        }
    }

    var onClick: ((UserFavoriteEntity) -> Unit)? = null
}