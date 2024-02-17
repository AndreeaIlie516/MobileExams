package com.andreeailie.reexam.feature.data.repository

import android.util.Log
import com.andreeailie.reexam.feature.data.data_source.remote.ApiService
import com.andreeailie.reexam.feature.domain.model.Entity
import com.andreeailie.reexam.feature.domain.model.EntityServer
import com.andreeailie.reexam.feature.domain.repository.RemoteEntityRepository

class RemoteEntityRepositoryImpl(
    private val apiService: ApiService
) : RemoteEntityRepository {
    override suspend fun getAllEntities(): List<Entity> {
        return try {
            Log.d("RemoteEntityRepositoryImpl", "getAllEntities called")
            val response = apiService.getAllEntities()
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

    override suspend fun getAllProperties(): List<String> {
        return try {
            Log.d("RemoteEntityRepositoryImpl", "getAllProperties called")
            val response = apiService.getAllProperties()
            Log.d("RemoteEntityRepositoryImpl", "response: $response")
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                Log.e("RemoteEntityRepositoryImpl", "API call failed: ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("RemoteEntityRepositoryImpl", "Exception in getAllProperties: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getEntityById(id: Int): Entity? {
        return try {
            Log.d("RemoteEntityRepositoryImpl", "getEntityById called")
            val response = apiService.getEntityById(id)
            Log.d("RemoteEntityRepositoryImpl", "response: $response")
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("RemoteEntityRepositoryImpl", "API call failed: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("RemoteEntityRepositoryImpl", "Exception in getEntitiesByProperty: ${e.message}")
            null
        }
    }

    override suspend fun getEntitiesByProperty(property: String): List<Entity> {
        return try {
            Log.d("RemoteEntityRepositoryImpl", "getEntitiesByProperty called")
            val response = apiService.getEntitiesByProperty(property)
            Log.d("RemoteEntityRepositoryImpl", "response: $response")
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                Log.e("RemoteEntityRepositoryImpl", "API call failed: ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("RemoteEntityRepositoryImpl", "Exception in getEntitiesByProperty: ${e.message}")
            emptyList()
        }
    }

    override suspend fun insertEntity(entity: Entity) {
        try {
            val entityForServer = EntityServer(
                name = entity.name,
                type = entity.type,
                calories = entity.calories,
                date = entity.date,
                notes = entity.notes
            )
            Log.e("RemoteEntityRepositoryImpl", "insertEntity called")

            apiService.addEntity(entityForServer)
        } catch (e: Exception) {
            Log.e("RemoteEntityRepositoryImpl", "Error inserting entity: ${e.message}")
            throw Exception("Server error occurred while creating the entity.")
        }
    }

    override suspend fun deleteEntity(entity: Entity) {
        Log.e("RemoteEntityRepositoryImpl", "Delete entity called")
        try {
            Log.e("RemoteEntityRepositoryImpl", "Id: ${entity.id}")
            apiService.deleteEntity(entity.id)
        } catch (e: Exception) {
            Log.e("RemoteEntityRepositoryImpl", "Error deleting entity: ${e.message}")
            throw Exception("Server error occurred while deleting the entity.")
        }
    }

    override suspend fun updateEntity(entity: Entity) {
        try {
            val entityForServer = EntityServer(
                name = entity.name,
                type = entity.type,
                calories = entity.calories,
                date = entity.date,
                notes = entity.notes
            )
            apiService.updateEntity(entity.id, entityForServer)
        } catch (e: Exception) {
            Log.e("RemoteEntityRepositoryImpl", "Error updating entity: ${e.message}")
            throw Exception("Server error occurred while updating the entity.")
        }
    }
}