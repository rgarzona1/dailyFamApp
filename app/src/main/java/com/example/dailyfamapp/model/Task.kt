package com.example.dailyfamapp.model

data class Task(
    var id: String? = null,
    var title: String = "",
    var done: Boolean = false,
    var createdAt: Long = System.currentTimeMillis()
)

