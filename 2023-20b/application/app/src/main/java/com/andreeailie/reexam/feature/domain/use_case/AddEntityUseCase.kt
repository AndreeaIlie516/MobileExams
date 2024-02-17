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
        } catch (e: Exception) {
            Log.d("AddEntityUseCase", "on catch")
            throw Exception("Failed to add the event. Please try again later.")
        }
    }

}