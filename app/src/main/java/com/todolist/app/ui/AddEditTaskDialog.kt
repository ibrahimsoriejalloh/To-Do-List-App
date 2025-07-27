package com.todolist.app.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.todolist.app.R
import com.todolist.app.data.Priority
import com.todolist.app.data.Task
import com.todolist.app.databinding.DialogAddEditTaskBinding
import com.todolist.app.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddEditTaskDialog : DialogFragment() {

    private var _binding: DialogAddEditTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskViewModel: TaskViewModel
    private var editingTask: Task? = null
    private var selectedDate: Date? = null

    companion object {
        private const val ARG_TASK = "task"

        fun newInstance(task: Task? = null): AddEditTaskDialog {
            val dialog = AddEditTaskDialog()
            if (task != null) {
                val args = Bundle()
                args.putSerializable(ARG_TASK, task)
                dialog.arguments = args
            }
            return dialog
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskViewModel = ViewModelProvider(requireActivity())[TaskViewModel::class.java]
        editingTask = arguments?.getSerializable(ARG_TASK) as Task?

        setupDialog()
        setupClickListeners()
        populateFields()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupDialog() {
        val isEditing = editingTask != null
        binding.textDialogTitle.text = if (isEditing) {
            getString(R.string.edit_task)
        } else {
            getString(R.string.add_task)
        }
        
        binding.buttonSave.text = if (isEditing) {
            getString(R.string.save)
        } else {
            getString(R.string.save)
        }
    }

    private fun setupClickListeners() {
        binding.buttonCancel.setOnClickListener {
            dismiss()
        }

        binding.buttonSave.setOnClickListener {
            saveTask()
        }

        binding.buttonSelectDate.setOnClickListener {
            showDatePicker()
        }

        binding.buttonClearDate.setOnClickListener {
            clearDate()
        }
    }

    private fun populateFields() {
        editingTask?.let { task ->
            binding.editTextTitle.setText(task.title)
            binding.editTextDescription.setText(task.description)
            
            when (task.priority) {
                Priority.LOW -> binding.radioLow.isChecked = true
                Priority.MEDIUM -> binding.radioMedium.isChecked = true
                Priority.HIGH -> binding.radioHigh.isChecked = true
            }
            
            task.dueDate?.let { date ->
                selectedDate = date
                updateDateDisplay()
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        selectedDate?.let { calendar.time = it }

        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                selectedDate = calendar.time
                updateDateDisplay()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun clearDate() {
        selectedDate = null
        updateDateDisplay()
    }

    private fun updateDateDisplay() {
        val dateText = selectedDate?.let { date ->
            val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            formatter.format(date)
        } ?: ""
        
        binding.textSelectedDate.text = dateText
    }

    private fun getSelectedPriority(): Priority {
        return when {
            binding.radioLow.isChecked -> Priority.LOW
            binding.radioHigh.isChecked -> Priority.HIGH
            else -> Priority.MEDIUM
        }
    }

    private fun saveTask() {
        val title = binding.editTextTitle.text.toString().trim()
        if (title.isEmpty()) {
            Snackbar.make(binding.root, getString(R.string.task_title_required), Snackbar.LENGTH_LONG).show()
            return
        }

        val description = binding.editTextDescription.text.toString().trim()
        val priority = getSelectedPriority()

        if (editingTask != null) {
            // Update existing task
            val updatedTask = editingTask!!.copy(
                title = title,
                description = description,
                priority = priority,
                dueDate = selectedDate,
                updatedAt = Date()
            )
            taskViewModel.updateTask(updatedTask)
        } else {
            // Create new task
            val newTask = Task(
                title = title,
                description = description,
                priority = priority,
                dueDate = selectedDate,
                createdAt = Date(),
                updatedAt = Date()
            )
            taskViewModel.insertTask(newTask)
        }

        Snackbar.make(binding.root, getString(R.string.task_saved), Snackbar.LENGTH_SHORT).show()
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}