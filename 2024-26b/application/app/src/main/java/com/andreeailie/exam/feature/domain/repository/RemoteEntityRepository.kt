package com.andreeailie.exam.feature.domain.repository

import com.andreeailie.exam.feature.data.network.WebSocketUpdate
import com.andreeailie.exam.feature.domain.model.Entity
import kotlinx.coroutines.flow.Flow

interface RemoteEntityRepository {

    //suspend fun getProperties(): List<String>

    suspend fun getAllEntities(): List<Entity>

    suspend fun getInProgressEvents(): List<Entity>

    //suspend fun getEntitiesByProperty(property: String): List<Entity>

    suspend fun insertEntity(entity: Entity)

    suspend fun deleteEntity(entity: Entity)

    suspend fun updateEntity(entity: Entity)

    suspend fun enrollInEvent(entity: Entity)

    suspend fun observeWebSocketEvents(): Flow<WebSocketUpdate>

}