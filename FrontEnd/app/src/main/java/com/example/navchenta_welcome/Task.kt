package com.example.navchenta_welcome

data class Task(
    val firstemail: String,
    val hour: String,
    val date: String,
    val id: Int = 0,
    val secondemail : String,
    val state : String,
    val district: String,
    val address: String,
    val batch: Int,
    val batchn : String ="Batch",
    val staten : String = "State",
    val districtn : String = "District"

) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Task

        if (id != other.id) return false

        return true

    }

    override fun hashCode(): Int {
        return id
    }
}
