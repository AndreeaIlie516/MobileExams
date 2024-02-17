package com.andreeailie.reexam.feature.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entities")
data class Entity (
    @PrimaryKey(autoGenerate = true) val idLocal: Int,
    val id: Int,
    val name: String,
    val type: String,
    val calories: Int,
    val date: String,
    val notes: String,
    var action: String?
)

data class EntityServer (
    val name: String,
    val type: String,
    val calories: Int,
    val date: String,
    val notes: String
)

class InvalidEntityException(message: String) : Exception(message)