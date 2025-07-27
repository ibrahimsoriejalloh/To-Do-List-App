package com.todolist.app.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.todolist.app.R
import com.todolist.app.data.Priority
import com.todolist.app.data.Task
import com.todolist.app.databinding.ItemTaskBinding
import java.text.SimpleDateFormat
import java.util.*

class TaskAdapter(
    private val onTaskClick: (Task) -> Unit,
    private val onTaskToggle: (Task) -> Unit,
    private val onTaskEdit: (Task) -> Unit,
    private val onTaskDelete: (Task) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : 
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.apply {
                textTitle.text = task.title
                textDescription.text = task.description
                checkboxCompleted.isChecked = task.isCompleted

                // Set priority indicator color
                val priorityColor = when (task.priority) {
                    Priority.HIGH -> ContextCompat.getColor(root.context, R.color.priority_high)
                    Priority.MEDIUM -> ContextCompat.getColor(root.context, R.color.priority_medium)
                    Priority.LOW -> ContextCompat.getColor(root.context, R.color.priority_low)
                }
                priorityIndicator.setCardBackgroundColor(priorityColor)

                // Format and display due date
                task.dueDate?.let { date ->
                    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                    textDueDate.text = "Due: ${formatter.format(date)}"
                    
                    // Check if overdue
                    val today = Calendar.getInstance()
                    today.set(Calendar.HOUR_OF_DAY, 0)
                    today.set(Calendar.MINUTE, 0)
                    today.set(Calendar.SECOND, 0)
                    today.set(Calendar.MILLISECOND, 0)
                    
                    if (date.before(today.time) && !task.isCompleted) {
                        textDueDate.setTextColor(ContextCompat.getColor(root.context, R.color.overdue))
                    } else {
                        textDueDate.setTextColor(ContextCompat.getColor(root.context, R.color.text_secondary))
                    }
                } ?: run {
                    textDueDate.text = ""
                }

                // Style completed tasks
                if (task.isCompleted) {
                    textTitle.paintFlags = textTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    textDescription.paintFlags = textDescription.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    root.alpha = 0.6f
                } else {
                    textTitle.paintFlags = textTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    textDescription.paintFlags = textDescription.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    root.alpha = 1.0f
                }

                // Set click listeners
                root.setOnClickListener { onTaskClick(task) }
                checkboxCompleted.setOnClickListener { onTaskToggle(task) }
                buttonEdit.setOnClickListener { onTaskEdit(task) }
                buttonDelete.setOnClickListener { onTaskDelete(task) }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}