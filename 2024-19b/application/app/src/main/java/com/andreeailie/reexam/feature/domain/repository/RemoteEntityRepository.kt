package com.andreeailie.reexam.feature.domain.repository

import com.andreeailie.reexam.feature.domain.model.Entity
import com.andreeailie.reexam.feature.domain.model.Property
import kotlinx.coroutines.flow.Flow

interface RemoteEntityRepository {

    suspend fun getAllEntities(): List<Entity>

    suspend fun getAllProperties(): List<String>

    suspend fun getEntityById(id: Int): Entity?

    suspend fun getEntitiesByProperty(property: String): List<Entity>

    suspend fun insertEntity(entity: Entity)

    suspend fun deleteEntity(entity: Entity)

    suspend fun updateEntity(entity: Entity)

//    suspend fun getInProgressEvents(): List<Entity>

//    suspend fun enrollInEvent(entity: Entity)

//    suspend fun observeWebSocketEvents(): Flow<WebSocketUpdate>
}