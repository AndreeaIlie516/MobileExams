package com.andreeailie.reexam.feature.domain.repository

import com.andreeailie.reexam.feature.domain.model.Entity
import com.andreeailie.reexam.feature.domain.model.Property
import kotlinx.coroutines.flow.Flow

interface LocalEntityRepository {

    fun getAllEntities(): Flow<List<Entity>>

    fun getAllProperties(): Flow<List<Property>>

    suspend fun getEntityById(id: Int): Entity?

    suspend fun findEntityByAttributes(title: String, author: String, isbn: String): Entity?

    suspend fun getEntitiesByProperty(property: Property): Flow<List<Entity>>

    suspend fun insertEntity(entity: Entity)

    suspend fun deleteEntity(entity: Entity)

    suspend fun deleteAllEntities()

    suspend fun deleteAllProperties()

    suspend fun getEventsWithPendingActions(): List<Entity>

    suspend fun clearAndCacheEntities(entitiesFlow: Flow<List<Entity>>)

    suspend fun clearAndCacheProperties(propertiesFlow: Flow<List<Property>>)
}