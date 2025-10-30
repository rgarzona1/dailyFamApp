package com.example.dailyfamapp.data

import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.example.dailyfamapp.model.Task

class TaskRepository {
    private val ref: DatabaseReference = Firebase.database.reference.child("todos")


    fun add(task: Task) {
        val key = ref.push().key ?: return
        task.id = key
        ref.child(key).setValue(task)
    }


    fun update(task: Task) {
        val id = task.id ?: return
        ref.child(id).setValue(task)
    }


    fun setDone(task: Task, done: Boolean) {
        val id = task.id ?: return
        ref.child(id).child("done").setValue(done)
    }


    fun delete(task: Task) {
        val id = task.id ?: return
        ref.child(id).removeValue()
    }


    fun listenAll(onChange: (List<Task>) -> Unit): ValueEventListener {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Task>()
                for (child in snapshot.children) {
                    child.getValue(Task::class.java)?.let { list.add(it) }
                }
// Ordenar por createdAt ascendente
                onChange(list.sortedBy { it.createdAt })
            }
            override fun onCancelled(error: DatabaseError) { /* se puede agregar un log */ }
        }
        ref.orderByChild("createdAt").addValueEventListener(listener)
        return listener
    }


    fun removeListener(listener: ValueEventListener) {
        ref.removeEventListener(listener)
    }
}
