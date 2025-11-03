package com.example.dailyfamapp
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailyfamapp.data.TaskRepository
import com.example.dailyfamapp.databinding.ActivityTaskListBinding
import com.example.dailyfamapp.databinding.DialogTaskBinding
import com.example.dailyfamapp.model.Task
import com.example.dailyfamapp.ui.TaskAdapter
import com.google.firebase.database.ValueEventListener
import android.widget.ImageButton


class TaskListActivity : AppCompatActivity() {


    private lateinit var binding: ActivityTaskListBinding
    private val repository = TaskRepository()
    private lateinit var adapter: TaskAdapter
    private var dbListener: ValueEventListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnBack.setOnClickListener {
            finish()
        }
        setupRecycler()
        setupFab()
    }


    private fun setupRecycler() {
        adapter = TaskAdapter(
            onToggle = { task, done -> repository.setDone(task, done) },
            onEdit = { task -> showTaskDialog(task) },
            onDelete = { task -> repository.delete(task) }
        )
        binding.rvTasks.layoutManager = LinearLayoutManager(this)
        binding.rvTasks.adapter = adapter
    }


    private fun setupFab() {
        binding.fabAdd.setOnClickListener { showTaskDialog(null) }
    }


    private fun showTaskDialog(taskToEdit: Task?) {
        val dialogBinding = DialogTaskBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setTitle(if (taskToEdit == null) "Nueva tarea" else "Editar tarea")
            .setView(dialogBinding.root)
            .setPositiveButton("Guardar") { d, _ ->
                val title = dialogBinding.edtTitle.text?.toString()?.trim().orEmpty()
                if (title.isNotEmpty()) {
                    if (taskToEdit == null) {
                        repository.add(Task(title = title))
                    } else {
                        taskToEdit.title = title
                        repository.update(taskToEdit)
                    }
                }
                d.dismiss()
            }
            .setNegativeButton("Cancelar") { d, _ -> d.dismiss() }
            .create()


// Precarga si es edición
        dialogBinding.edtTitle.setText(taskToEdit?.title ?: "")
        dialog.show()
    }


    override fun onStart() {
        super.onStart()
        dbListener = repository.listenAll { list ->
            adapter.submit(list)
        }
    }


    override fun onStop() {
        super.onStop()
        dbListener?.let { repository.removeListener(it) }
    }


}
