package com.todolist.app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.todolist.app.R
import com.todolist.app.adapter.TaskAdapter
import com.todolist.app.data.Task
import com.todolist.app.databinding.FragmentTaskListBinding
import com.todolist.app.viewmodel.TaskViewModel

class TaskListFragment : Fragment() {

    enum class TaskFilter {
        ALL, PENDING, COMPLETED
    }

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskFilter: TaskFilter

    companion object {
        private const val ARG_FILTER = "filter"

        fun newInstance(filter: TaskFilter): TaskListFragment {
            val fragment = TaskListFragment()
            val args = Bundle()
            args.putSerializable(ARG_FILTER, filter)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskFilter = arguments?.getSerializable(ARG_FILTER) as TaskFilter
        taskViewModel = ViewModelProvider(requireActivity())[TaskViewModel::class.java]

        setupRecyclerView()
        observeTasks()
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(
            onTaskClick = { task -> showTaskDetails(task) },
            onTaskToggle = { task -> toggleTaskCompletion(task) },
            onTaskEdit = { task -> editTask(task) },
            onTaskDelete = { task -> confirmDeleteTask(task) }
        )

        binding.recyclerViewTasks.apply {
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeTasks() {
        val liveData = when (taskFilter) {
            TaskFilter.ALL -> taskViewModel.allTasks
            TaskFilter.PENDING -> taskViewModel.pendingTasks
            TaskFilter.COMPLETED -> taskViewModel.completedTasks
        }

        liveData.observe(viewLifecycleOwner) { tasks ->
            taskAdapter.submitList(tasks)
            updateEmptyState(tasks.isEmpty())
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            binding.recyclerViewTasks.visibility = View.GONE
            binding.layoutEmpty.visibility = View.VISIBLE
            
            val emptyMessage = when (taskFilter) {
                TaskFilter.ALL -> getString(R.string.no_tasks_message)
                TaskFilter.PENDING -> getString(R.string.no_pending_tasks)
                TaskFilter.COMPLETED -> getString(R.string.no_completed_tasks)
            }
            binding.textEmptyMessage.text = emptyMessage
        } else {
            binding.recyclerViewTasks.visibility = View.VISIBLE
            binding.layoutEmpty.visibility = View.GONE
        }
    }

    private fun showTaskDetails(task: Task) {
        // For now, just edit the task when clicked
        editTask(task)
    }

    private fun toggleTaskCompletion(task: Task) {
        taskViewModel.toggleTaskCompletion(task)
        
        val message = if (task.isCompleted) {
            getString(R.string.task_uncompleted)
        } else {
            getString(R.string.task_completed)
        }
        
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun editTask(task: Task) {
        AddEditTaskDialog.newInstance(task).show(parentFragmentManager, "EditTaskDialog")
    }

    private fun confirmDeleteTask(task: Task) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete))
            .setMessage(getString(R.string.confirm_delete))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteTask(task)
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    private fun deleteTask(task: Task) {
        taskViewModel.deleteTask(task)
        Snackbar.make(binding.root, getString(R.string.task_deleted), Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}