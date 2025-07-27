package com.todolist.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.todolist.app.databinding.ActivityMainBinding
import com.todolist.app.ui.AddEditTaskDialog
import com.todolist.app.ui.TaskListFragment
import com.todolist.app.viewmodel.TaskViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskViewModel = ViewModelProvider(this)[TaskViewModel::class.java]

        setupToolbar()
        setupViewPager()
        setupFab()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun setupViewPager() {
        binding.viewPager.adapter = TaskPagerAdapter(this)
        
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_all)
                1 -> getString(R.string.tab_pending)
                2 -> getString(R.string.tab_completed)
                else -> ""
            }
        }.attach()
    }

    private fun setupFab() {
        binding.fabAddTask.setOnClickListener {
            showAddTaskDialog()
        }
    }

    private fun showAddTaskDialog() {
        AddEditTaskDialog.newInstance().show(supportFragmentManager, "AddTaskDialog")
    }

    inner class TaskPagerAdapter(fragmentActivity: FragmentActivity) : 
        FragmentStateAdapter(fragmentActivity) {

        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> TaskListFragment.newInstance(TaskListFragment.TaskFilter.ALL)
                1 -> TaskListFragment.newInstance(TaskListFragment.TaskFilter.PENDING)
                2 -> TaskListFragment.newInstance(TaskListFragment.TaskFilter.COMPLETED)
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }
    }
}