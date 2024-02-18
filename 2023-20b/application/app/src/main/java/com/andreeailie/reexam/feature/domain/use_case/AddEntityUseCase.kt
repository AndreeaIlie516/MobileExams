package com.andreeailie.reexam.feature.domain.use_case

import android.util.Log
import com.andreeailie.reexam.feature.data.network.NetworkStatusTracker
import com.andreeailie.reexam.feature.domain.model.Entity
import com.andreeailie.reexam.feature.domain.model.InvalidEntityException
import com.andreeailie.reexam.feature.domain.repository.LocalEntityRepository
import com.andreeailie.reexam.feature.domain.repository.RemoteEntityRepository

class AddEntityUseCase(
    private val localRepository: LocalEntityRepository,
    private val remoteRepository: RemoteEntityRepository,
    private val networkStatusTracker: NetworkStatusTracker
) {

    suspend operator fun invoke(entity: Entity) {
        val isNetworkAvailable = networkStatusTracker.isCurrentlyAvailable()
        try {
            val existingEntity = localRepository.findEntityByAttributes(
                name = entity.name,
                type = entity.type,
                date = entity.date,
                calories = entity.calories
            )
            Log.i("AddEntityUseCase", "Entity already exists: $existingEntity")

            if (existingEntity != null) {
                // If the entity exists, log that we're updating it instead of adding a new one.
                Log.d("AddEntityUseCase", "Entity already exists.")
            } else {
                if (isNetworkAvailable) {
                    Log.d("AddEntityUseCase", "Add to the server")
                    val newEvent = remoteRepository.insertEntity(entity)
                    Log.d("AddEntityUseCase", "newEvent: $newEvent")
                    localRepository.insertEntity(entity.copy(action = null))

                } else {
                    Log.d("AddEntityUseCase", "Add on the local database, no internet")
                    Log.d("AddEntityUseCase", "entity: $entity")
                    val entityCopy = entity.copy(action = "add")
                    Log.d("AddEntityUseCase", "entityCopy: $entityCopy")
                    localRepository.insertEntity(entityCopy)
                }
            }
        } catch (e: Exception) {
            Log.d("AddEntityUseCase", "on catch")
            throw Exception("Failed to add the event. Please try again later.")
        }
    }

}