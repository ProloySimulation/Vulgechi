package com.xossapp.vulegechi.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xossapp.vulegechi.newReminder.data.ReminderItem
import com.xossapp.vulegechi.databinding.ReminderviewRowBinding
import com.xossapp.vulegechi.newReminder.ActivityReminder
import com.xossapp.vulegechi.utils.Constants

class ReminderAdapter(): RecyclerView.Adapter<ReminderAdapter.MyViewHolder>() {
    var items = ArrayList<ReminderItem>()
    var onItemClickListener: ItemClickListener? = null
    private lateinit var context:Context

    fun setDataList(data: ArrayList<ReminderItem>)
    {
        this.items = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderAdapter.MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        val binding = com.xossapp.vulegechi.databinding.ReminderviewRowBinding.inflate(layoutInflater,parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount() = items.size


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener{
            onItemClickListener?.onReminderClick(items[position])
        }
        holder.binding.btnReminderDelete.setOnClickListener {

            var intent = Intent(context,ActivityReminder::class.java)
            intent.putExtra(Constants.KEY_OCASSION_TITLE,holder.binding.reminderData?.ocassionName)
            intent.putExtra(Constants.KEY_OCASSION_PERSON_TYPE,holder.binding.reminderData?.personType)
            intent.putExtra(Constants.KEY_OCASSION_PERSON_NAME,holder.binding.reminderData?.ocassionPerson)
            intent.putExtra(Constants.KEY_PERSON_MOBILE,holder.binding.reminderData?.personNumber)
            intent.putExtra(Constants.KEY_OCASSION_DATE,holder.binding.reminderData?.ocassionDate)
            intent.putExtra(Constants.KEY_OCASSION_TIME,holder.binding.reminderData?.ocassionTime)
            intent.putExtra(Constants.KEY_REMINDER_ID,holder.binding.reminderData?.reminderId)
            intent.putExtra(Constants.KEY_REMINDER_DELETE,Constants.KEY_REMINDER_DELETE)
            context.startActivity(intent)
        }
    }

    class MyViewHolder(val binding: ReminderviewRowBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(data: ReminderItem) {
            binding.reminderData = data
            binding.executePendingBindings()

        }
    }

    interface ItemClickListener {
        fun onReminderClick(reminderItem: ReminderItem)
    }
}