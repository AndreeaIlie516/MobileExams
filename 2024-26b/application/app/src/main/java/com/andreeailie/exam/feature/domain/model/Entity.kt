package com.andreeailie.exam.feature.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entities")
data class Entity (
    @PrimaryKey(autoGenerate = true) val idLocal: Int,
    val id: Int,
    val name: String,
    val team: String,
    val details: String,
    val status: String,
    val participants: String,
    val type: String,
    var action: String?
)

data class EntityServer (
    val name: String,
    val team: String,
    val details: String,
    val status: String,
    val participants: String,
    val type: String
)

class InvalidEntityException(message: String) : Exception(message)