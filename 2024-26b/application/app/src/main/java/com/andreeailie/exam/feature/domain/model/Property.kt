package com.andreeailie.exam.feature.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "properties")
data class Property (
    @PrimaryKey(autoGenerate = true) val idLocal: Int,
    val name: String
)