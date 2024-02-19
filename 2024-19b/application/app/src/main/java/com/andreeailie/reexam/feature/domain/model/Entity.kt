package com.andreeailie.reexam.feature.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entities")
data class Entity (
    @PrimaryKey(autoGenerate = true) val idLocal: Int,
    val id: Int,
    val title: String,
    val author: String,
    val genre: String,
    val year: Int,
    val ISBN: String,
    val availability: String,
    var action: String?
)

data class EntityServer (
    val title: String,
    val author: String,
    val genre: String,
    val year: Int,
    val ISBN: String,
    val availability: String,
)

class InvalidEntityException(message: String) : Exception(message)