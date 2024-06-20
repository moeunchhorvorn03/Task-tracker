package com.chhorvorn.material

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

interface OnClickListener {
    fun onItemDelete(position: Int)
}
open class CompletedTaskAdapter(
    private val item: List<TASK_ITEM>,
    private val listener: OnClickListener
): RecyclerView.Adapter<CompletedTaskAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.textItem)
        val desc: TextView = view.findViewById(R.id.desc)
        val deleteButton: ImageView = view.findViewById(R.id.deleteButton)
        val doneButton: TextView = view.findViewById(R.id.doneButton)
        val completedButton: TextView = view.findViewById(R.id.completed)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_mail_fragment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = item[position].title
        holder.desc.text = item[position].desc
        holder.desc.isVisible = item[position].desc?.isEmpty() != true
        holder.deleteButton.isVisible = item[position].status == true
        holder.deleteButton.setOnClickListener {
            listener.onItemDelete(position)
        }

        holder.doneButton.isVisible = false
        holder.completedButton.isVisible = false
    }

    override fun getItemCount(): Int = item.size
}
