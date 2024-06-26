package com.chhorvorn.material

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

open class TaskAdapter(private val item: List<TASK_ITEM>, val listener: TaskInterface): RecyclerView.Adapter<TaskAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.textItem)
        val desc: TextView = view.findViewById(R.id.desc)
        val doneButton: TextView = view.findViewById(R.id.doneButton)
        val deleteButton: ImageView = view.findViewById(R.id.deleteButton)
        val completedButton: TextView = view.findViewById(R.id.completed)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_mail_fragment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = item[position].title
        holder.title.paintFlags = if (item[position].status) Paint.STRIKE_THRU_TEXT_FLAG else 0

        holder.desc.text = item[position].desc
        holder.desc.isVisible = item[position].desc.toString().isEmpty() != true

        //Delete button
        holder.deleteButton.isVisible = false
        holder.deleteButton.setOnClickListener {
            listener.onItemDelete(position)
        }

        //complete button
        holder.completedButton.isVisible = item[position].status == true

        // Done button
        holder.doneButton.isVisible = item[position].status != true
        holder.doneButton.setOnClickListener {
            listener.onItemDone(position)
        }
    }

    override fun getItemCount(): Int = item.size

    interface TaskInterface {
        fun onItemClick(position: Int)
        fun onItemDelete(position: Int)
        fun onItemDone(position: Int)
    }
}
