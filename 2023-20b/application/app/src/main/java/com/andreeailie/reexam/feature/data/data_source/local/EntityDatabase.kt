package com.andreeailie.reexam.feature.data.data_source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.andreeailie.reexam.feature.domain.model.Entity
import com.andreeailie.reexam.feature.domain.model.Property

@Database(
    entities = [Entity::class, Property::class],
    version = 1
)
abstract class EntityDatabase : RoomDatabase() {

    abstract val entityDao: EntityDao

    companion object {
        const val DATABASE_NAME = "entity_db"
    }
}