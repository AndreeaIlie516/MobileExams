package com.andreeailie.exam.feature.domain.use_case

import android.util.Log
import com.andreeailie.exam.feature.data.network.NetworkChecker
import com.andreeailie.exam.feature.domain.model.Entity
import com.andreeailie.exam.feature.domain.model.InvalidEntityException
import com.andreeailie.exam.feature.domain.repository.LocalEntityRepository
import com.andreeailie.exam.feature.domain.repository.RemoteEntityRepository

class UpdateEntityUseCase(
    private val localRepository: LocalEntityRepository,
    private val remoteRepository: RemoteEntityRepository,
    private val networkChecker: NetworkChecker
) {

    suspend operator fun invoke(entity: Entity): String {
        if (entity.name.isBlank()) {
            return "Failed"
            //throw InvalidEntityException("The date of the event can't be empty.")
        }
        if (entity.team.isBlank()) {
            return "Failed"
            //throw InvalidEntityException("The type of the event can't be empty.")
        }
        if (entity.status.isBlank()) {
            return "Failed"
            //throw InvalidEntityException("The category of the event can't be empty.")
        }
        if (networkChecker.isNetworkAvailable()) {
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