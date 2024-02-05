package com.andreeailie.exam.feature.domain.use_case

import android.util.Log
import com.andreeailie.exam.feature.data.network.NetworkChecker
import com.andreeailie.exam.feature.domain.model.Entity
import com.andreeailie.exam.feature.domain.model.InvalidEntityException
import com.andreeailie.exam.feature.domain.repository.LocalEntityRepository
import com.andreeailie.exam.feature.domain.repository.RemoteEntityRepository

class AddEntityUseCase(
    private val localRepository: LocalEntityRepository,
    private val remoteRepository: RemoteEntityRepository,
    private val networkChecker: NetworkChecker
) {

    suspend operator fun invoke(entity: Entity) {
        if (entity.name.isBlank()) {
            throw InvalidEntityException("The name of the event can't be empty.")
        }
        if (entity.team.isBlank()) {
            throw InvalidEntityException("The team of the event can't be empty.")
        }
        if (entity.details.isBlank()) {
            throw InvalidEntityException("The details of the event can't be empty.")
        }
        try {
            if (networkChecker.isNetworkAvailable()) {
                val newEvent = remoteRepository.insertEntity(entity)
                Log.d("AddEventUseCase", "newEvent: $newEvent")
                localRepository.insertEntity(entity.copy(action = null))
            } else {
                localRepository.insertEntity(entity.copy(action = "add"))
            }
        } catch (e: Exception) {
            throw Exception("Failed to add the event. Please try again later.")
        }
    }

}