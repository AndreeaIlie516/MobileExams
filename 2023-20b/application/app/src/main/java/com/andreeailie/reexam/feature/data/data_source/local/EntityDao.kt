package com.andreeailie.reexam.feature.data.data_source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andreeailie.reexam.feature.domain.model.Entity
import com.andreeailie.reexam.feature.domain.model.Property
import kotlinx.coroutines.flow.Flow

@Dao
interface EntityDao {
    @Query("SELECT * FROM entities")
    fun getAllEntities(): Flow<List<Entity>>

    @Query("SELECT * FROM entities WHERE idLocal = :id")
    suspend fun getEntityById(id: Int): Entity?

    @Query("SELECT * FROM properties")
    fun getAllProperties(): Flow<List<Property>>

    @Query("SELECT * FROM entities WHERE type = :property")
    fun getEntitiesByProperty(property: String): Flow<List<Entity>>

    @Query("SELECT * FROM entities WHERE name = :name AND type = :type AND date = :date AND calories = :calories LIMIT 1")
    suspend fun findEntityByAttributes(name: String, type: String, date: String, calories: Int): Entity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntity(entity: Entity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProperty(property: Property)

    @Query("Delete FROM entities")
    suspend fun deleteAllEntities()

    @Delete
    suspend fun deleteEntity(activity: Entity)

    @Query("SELECT * FROM entities WHERE action IS NOT NULL")
    fun getEntitiesWithPendingActions(): List<Entity>

    @Query("Delete FROM properties")
    suspend fun deleteAllProperties()
}