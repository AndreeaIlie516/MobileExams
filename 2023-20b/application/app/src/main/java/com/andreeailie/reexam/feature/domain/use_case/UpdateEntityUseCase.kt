package com.andreeailie.reexam.feature.domain.use_case

import android.util.Log
import com.andreeailie.reexam.feature.data.network.NetworkStatusTracker
import com.andreeailie.reexam.feature.domain.model.Entity
import com.andreeailie.reexam.feature.domain.repository.LocalEntityRepository
import com.andreeailie.reexam.feature.domain.repository.RemoteEntityRepository

class UpdateEntityUseCase(
    private val localRepository: LocalEntityRepository,
    private val remoteRepository: RemoteEntityRepository,
    private val networkStatusTracker: NetworkStatusTracker
) {

    suspend operator fun invoke(entity: Entity): String {
        val isNetworkAvailable = networkStatusTracker.isCurrentlyAvailable()
        if (entity.name.isBlank()) {
            return "Failed"
            //throw InvalidEntityException("The date of the event can't be empty.")
        }
        if (entity.type.isBlank()) {
            return "Failed"
            //throw InvalidEntityException("The type of the event can't be empty.")
        }
        if (entity.date.isBlank()) {
            return "Failed"
            //throw InvalidEntityException("The category of the event can't be empty.")
        }
        if (isNetworkAvailable) {
            try {
                val newEntity = remoteRepository.updateEntity(entity)
                Log.d("AddEntityUseCase", "newEntity: $newEntity")
                localRepository.insertEntity(entity)
                return "Success"
            } catch (e:Exception) {
                return "Failed"
            }
        }
        else {
            return "Failed"
        }
    }
}