package com.andreeailie.exam.feature.domain.repository

import com.andreeailie.exam.feature.domain.model.Entity
import com.andreeailie.exam.feature.domain.model.Property
import kotlinx.coroutines.flow.Flow

interface LocalEntityRepository {

    fun getAllEntities(): Flow<List<Entity>>

    suspend fun getEntityById(id: Int): Entity?

    suspend fun insertEntity(entity: Entity)

    suspend fun deleteEntity(entity: Entity)

    suspend fun deleteAllEntities()
    suspend fun clearAndCacheEntities(entitiesFlow: Flow<List<Entity>>)

    suspend fun getEventsWithPendingActions(): List<Entity>
}