package com.example.android.trackmysleepquality.sleeptracker

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding

class SleepNightAdapter : ListAdapter<SleepNight, SleepItemViewHolder>(SleepNightDiffCallback()) {

    override fun onBindViewHolder(holder: SleepItemViewHolder, position: Int) {
        val item = getItem(position)
        val res = holder.itemView.resources
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SleepItemViewHolder {
        return SleepItemViewHolder.from(parent)
    }
}

class SleepItemViewHolder private constructor(val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SleepNight) {
        binding.sleep = item
        binding.executePendingBindings()
        val res: Resources = itemView.resources
    }

    companion object {
        fun from(parent: ViewGroup): SleepItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)
            return SleepItemViewHolder(binding)
        }
    }
}

class SleepNightDiffCallback : DiffUtil.ItemCallback<SleepNight>() {
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean = oldItem.nightId == newItem.nightId

    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean = oldItem == newItem

}