package com.andreeailie.exam.feature.data.data_source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andreeailie.exam.feature.domain.model.Entity
import com.andreeailie.exam.feature.domain.model.Property
import kotlinx.coroutines.flow.Flow

@Dao
interface EntityDao {
    @Query("SELECT * FROM entities")
    fun getAllEvents(): Flow<List<Entity>>

    @Query("SELECT * FROM entities WHERE idLocal = :id")
    suspend fun getEntityById(id: Int): Entity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(activity: Entity)


    @Query("Delete FROM entities")
    suspend fun deleteAllEntities()

    @Delete
    suspend fun deleteEvent(activity: Entity)

    @Query("SELECT * FROM entities WHERE action IS NOT NULL")
    fun getEventsWithPendingActions(): List<Entity>
}