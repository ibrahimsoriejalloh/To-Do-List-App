# To-Do List App

A modern, feature-rich Android productivity app for creating and managing personal tasks with persistent storage using Room database.

## Features

✅ **Add New Tasks** - Create tasks with title, description, due date, and priority levels
✅ **Task Management** - Mark tasks as completed or pending with visual feedback
✅ **Edit & Delete** - Modify existing tasks or remove them entirely
✅ **Priority System** - Organize tasks with High, Medium, and Low priority levels
✅ **Due Date Tracking** - Set and track due dates with overdue indicators
✅ **Persistent Storage** - All tasks are stored locally using Room database
✅ **Modern UI** - Clean Material Design 3 interface with intuitive navigation
✅ **Task Filtering** - View All, Pending, or Completed tasks in separate tabs
✅ **Visual Indicators** - Color-coded priority indicators and completion status

## Architecture

The app follows modern Android development best practices:

- **MVVM Architecture** - Clear separation of concerns with ViewModel and Repository pattern
- **Room Database** - Local persistence with type converters for Date and enum types
- **LiveData** - Reactive UI updates when data changes
- **View Binding** - Type-safe view references
- **Material Design 3** - Modern UI components and theming
- **Kotlin** - 100% Kotlin codebase with coroutines for async operations

## Project Structure

```
app/src/main/java/com/todolist/app/
├── data/
│   ├── Task.kt              # Task entity and Priority enum
│   ├── TaskDao.kt           # Database access object
│   ├── TaskDatabase.kt      # Room database configuration
│   └── Converters.kt        # Type converters for Room
├── repository/
│   └── TaskRepository.kt    # Data repository layer
├── viewmodel/
│   └── TaskViewModel.kt     # ViewModel for UI data management
├── adapter/
│   └── TaskAdapter.kt       # RecyclerView adapter for task list
├── ui/
│   ├── TaskListFragment.kt  # Fragment for displaying task lists
│   └── AddEditTaskDialog.kt # Dialog for adding/editing tasks
└── MainActivity.kt          # Main activity with tabs and navigation
```

## Technologies Used

- **Kotlin** - Primary programming language
- **Room Database** - Local data persistence
- **AndroidX Lifecycle** - ViewModel and LiveData components
- **Material Design 3** - UI components and theming
- **ViewPager2** - Tab-based navigation
- **RecyclerView** - Efficient list display
- **Coroutines** - Asynchronous programming

## Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 24 (API level 24) or higher
- Kotlin 1.8.20 or later

### Installation
1. Clone the repository
```bash
git clone https://github.com/yourusername/To-Do-List-App.git
```

2. Open the project in Android Studio

3. Build and run the app on an emulator or physical device

## Usage

### Adding a New Task
1. Tap the floating action button (+)
2. Fill in the task title (required)
3. Optionally add a description
4. Select priority level (Low, Medium, High)
5. Choose a due date if needed
6. Tap "Save"

### Managing Tasks
- **Complete a task**: Tap the checkbox next to the task
- **Edit a task**: Tap the edit button (pencil icon)
- **Delete a task**: Tap the delete button (trash icon) and confirm
- **View different categories**: Use the tabs (All, Pending, Completed)

### Visual Indicators
- **Priority Colors**: 
  - Red: High priority
  - Orange: Medium priority  
  - Green: Low priority
- **Overdue Tasks**: Due dates shown in red for overdue tasks
- **Completed Tasks**: Strike-through text and reduced opacity

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Future Enhancements

- [ ] Task categories and tags
- [ ] Search and filter functionality
- [ ] Data export/import
- [ ] Reminders and notifications
- [ ] Cloud synchronization
- [ ] Task sharing capabilities
- [ ] Dark mode support
- [ ] Widget for home screen

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Screenshots

*Screenshots will be added once the app is running*

---

**Built with ❤️ using modern Android development practices**
