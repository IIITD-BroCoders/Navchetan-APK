package com.spym.navchenta_welcome

object TaskDataSource {
    val list = arrayListOf<Task>()
    fun getList() = list.toList()

    fun insertTask(task: Task) {
        list.add(task)
    }

    fun findById(taskId: Int) = list.find { it.id == taskId }

    fun deleteTask(task: Task) {
        list.remove(task)
    }
    fun deleteAll(){
        list.clear()
    }
}
