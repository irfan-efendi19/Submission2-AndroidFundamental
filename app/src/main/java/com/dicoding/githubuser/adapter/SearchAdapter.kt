package com.dicoding.githubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.data.response.SearchItem
import de.hdodenhof.circleimageview.CircleImageView

class SearchAdapter(val dataUser: List<SearchItem>) :
    RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgAvatar = view.findViewById<CircleImageView>(R.id.avatarIV)
        val loginUsername = view.findViewById<TextView>(R.id.TVItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_holder, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataUser.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.loginUsername.text = dataUser.get(position).login

        Glide.with(holder.imgAvatar)
            .load(dataUser[position].avatarUrl)
            .error(R.drawable.baseline_broken_image_24)
            .into(holder.imgAvatar)

        holder.itemView.setOnClickListener {
            onClick?.invoke(dataUser[position])
        }
    }

    var onClick: ((SearchItem) -> Unit)? = null


}
