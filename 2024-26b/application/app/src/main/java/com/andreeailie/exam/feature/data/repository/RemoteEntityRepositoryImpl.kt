package com.andreeailie.exam.feature.data.repository

import android.util.Log
import com.andreeailie.exam.feature.data.data_source.remote.ApiService
import com.andreeailie.exam.feature.data.network.WebSocketService
import com.andreeailie.exam.feature.data.network.WebSocketUpdate
import com.andreeailie.exam.feature.domain.model.Entity
import com.andreeailie.exam.feature.domain.model.EntityServer
import com.andreeailie.exam.feature.domain.repository.RemoteEntityRepository
import kotlinx.coroutines.flow.Flow

class RemoteEntityRepositoryImpl(
    private val apiService: ApiService,
    private val webSocketService: WebSocketService
) : RemoteEntityRepository {
//    override suspend fun getProperties(): List<String> {
//        return try {
//            Log.d("RemoteEntityRepositoryImpl", "call get properties")
//            val response = apiService.getDayData()
//            Log.d("RemoteEntityRepositoryImpl", "response: $response")
//            if (response.isSuccessful) {
//                response.body() ?: emptyList()
//            } else {
//                Log.e("RemoteEntityRepositoryImpl", "API call failed: ${response.errorBody()?.string()}")
//                emptyList()
//            }
//        } catch (e: Exception) {
//            Log.e("RemoteEntityRepositoryImpl", "Exception in getProperties: ${e.message}")
//            emptyList()
//        }
//    }

    override suspend fun getAllEntities(): List<Entity> {
        return try {
            Log.d("RemoteEntityRepositoryImpl", "call get all entities")
            val response = apiService.getEvents()
            Log.d("RemoteEntityRepositoryImpl", "response: $response")
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                Log.e("RemoteEntityRepositoryImpl", "API call failed: ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("RemoteEntityRepositoryImpl", "Exception in getAllEntities: ${e.message}")
            emptyList()
        }
    }

//    override suspend fun getEntitiesByProperty(property: String): List<Entity> {
//        return try {
//            Log.d("RemoteEntityRepositoryImpl", "call get entities by property")
//            val response = apiService.getActivitiesByDate(property)
//            Log.d("RemoteEntityRepositoryImpl", "response: $response")
//            if (response.isSuccessful) {
//                response.body() ?: emptyList()
//            } else {
//                Log.e("RemoteEntityRepositoryImpl", "API call failed: ${response.errorBody()?.string()}")
//                emptyList()
//            }
//        } catch (e: Exception) {
//            Log.e("RemoteEntityRepositoryImpl", "Exception in getEntitiesByProperty: ${e.message}")
//            emptyList()
//        }
//    }

    override suspend fun insertEntity(entity: Entity) {
       try {
           val entityForServer = EntityServer(
               name = entity.name,
               team = entity.team,
               details = entity.details,
               status = entity.status,
               participants = entity.participants,
               type= entity.type
           )
           Log.e("RemoteEntityRepositoryImpl", "insertEntity called")

           apiService.addEvent(entityForServer)
       } catch (e: Exception) {
           Log.e("RemoteEntityRepositoryImpl", "Error inserting entity: ${e.message}")
           throw Exception("Server error occurred while creating the entity.")
       }
    }

    override suspend fun deleteEntity(entity: Entity) {
        Log.e("RemoteEntityRepositoryImpl", "Delete entity called")
        try {
            Log.e("RemoteEntityRepositoryImpl", "Id: ${entity.id}")
            apiService.deleteEvent(entity.id)
        } catch (e: Exception) {
            Log.e("RemoteEntityRepositoryImpl", "Error deleting entity: ${e.message}")
            throw Exception("Server error occurred while deleting the entity.")
        }
    }

    override suspend fun updateEntity(entity: Entity) {
        try {
            val entityForServer = EntityServer(
                name = entity.name,
                team = entity.team,
                details = entity.details,
                status = entity.status,
                participants = entity.participants,
                type = entity.type
            )
            apiService.updateEvent(entity.id, entityForServer)
        } catch (e: Exception) {
            Log.e("RemoteEntityRepositoryImpl", "Error updating entity: ${e.message}")
            throw Exception("Server error occurred while updating the entity.")
        }
    }

    override suspend fun enrollInEvent(entity: Entity) {
        Log.e("RemoteEntityRepositoryImpl", "Enroll in event called")
        try {
            Log.e("RemoteEntityRepositoryImpl", "Id: ${entity.id}")
            apiService.enrollInEvent(entity.id)
        } catch (e: Exception) {
            Log.e("RemoteEntityRepositoryImpl", "Error deleting entity: ${e.message}")
            throw Exception("Server error occurred while deleting the entity.")
        }
    }

    override suspend fun getInProgressEvents(): List<Entity> {
        return try {
            Log.d("RemoteEntityRepositoryImpl", "call get all entities")
            val response = apiService.getEventsInProgress()
            Log.d("RemoteEntityRepositoryImpl", "response: $response")
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                Log.e("RemoteEntityRepositoryImpl", "API call failed: ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("RemoteEntityRepositoryImpl", "Exception in getAllEntities: ${e.message}")
            emptyList()
        }
    }

    override suspend fun observeWebSocketEvents(): Flow<WebSocketUpdate> =
        webSocketService.observeWebSocketUpdates()
}