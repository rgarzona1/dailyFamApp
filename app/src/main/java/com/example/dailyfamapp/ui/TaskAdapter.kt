package com.example.dailyfamapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyfamapp.R
import com.example.dailyfamapp.databinding.ItemTaskBinding
import com.example.dailyfamapp.model.Task



class TaskAdapter(
    private val onToggle: (Task, Boolean) -> Unit,
    private val onEdit: (Task) -> Unit,
    private val onDelete: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.VH>() {


    private val items = mutableListOf<Task>()


    fun submit(list: List<Task>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }


    inner class VH(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.txtTitle.text = task.title
            binding.chkDone.isChecked = task.done


            binding.chkDone.setOnCheckedChangeListener { _, isChecked ->
                onToggle(task, isChecked)
            }


            binding.btnMore.setOnClickListener { v ->
                PopupMenu(v.context, v).apply {
                    menuInflater.inflate(R.menu.menu_item_task, menu)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.action_edit -> { onEdit(task); true }
                            R.id.action_delete -> { onDelete(task); true }
                            else -> false
                        }
                    }
                }.show()
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }


    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])


    override fun getItemCount(): Int = items.size
}
