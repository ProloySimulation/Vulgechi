package com.xossapp.vulegechi.wish.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xossapp.vulegechi.databinding.ItemWishBinding
import com.xossapp.vulegechi.databinding.ReminderviewRowBinding
import com.xossapp.vulegechi.home.ReminderAdapter
import com.xossapp.vulegechi.newReminder.ActivityReminder
import com.xossapp.vulegechi.newReminder.data.ReminderItem
import com.xossapp.vulegechi.utils.Constants
import com.xossapp.vulegechi.wish.data.Wish

class WishAdapter(): RecyclerView.Adapter<WishAdapter.MyViewHolder>() {

    var items = ArrayList<Wish>()
    private lateinit var context: Context

    fun setDataList(data: ArrayList<Wish>)
    {
        this.items = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishAdapter.MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        val binding = com.xossapp.vulegechi.databinding.ItemWishBinding.inflate(layoutInflater,parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount() = items.size


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class MyViewHolder(val binding: ItemWishBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Wish) {
            binding.wishData = data
            binding.executePendingBindings()

        }
    }
}