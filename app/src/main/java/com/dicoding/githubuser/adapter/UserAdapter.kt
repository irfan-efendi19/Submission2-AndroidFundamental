package com.dicoding.githubuser.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.data.response.ItemsItem
import com.dicoding.githubuser.ui.DetailActivity
import de.hdodenhof.circleimageview.CircleImageView


class UserAdapter(private val dataUser: List<ItemsItem>) :
    RecyclerView.Adapter<UserAdapter.MyViewHolder>() {
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgAvatar = view.findViewById<CircleImageView>(R.id.avatarIV)
        val loginUsername = view.findViewById<TextView>(R.id.TVItem)
//        val loginDesc = view.findViewById<TextView>(R.id.DescTV)

        fun bind(user: ItemsItem) {
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("username", user.login)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_holder, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = dataUser.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = dataUser[position]
        holder.loginUsername.text = dataUser[position].login
//        holder.loginDesc.text = dataUser[position].reposUrl

        Glide.with(holder.imgAvatar)
            .load(data.avatarUrl)
            .error(R.drawable.baseline_broken_image_24)
            .into(holder.imgAvatar)
        holder.bind(data)
    }
}

