package com.andreeailie.exam.feature.domain.use_case

import android.util.Log
import com.andreeailie.exam.feature.data.network.NetworkChecker
import com.andreeailie.exam.feature.domain.model.Entity
import com.andreeailie.exam.feature.domain.repository.LocalEntityRepository
import com.andreeailie.exam.feature.domain.repository.RemoteEntityRepository

class EnrollInEventUseCase(
    private val localRepository: LocalEntityRepository,
    private val remoteRepository: RemoteEntityRepository,
    private val networkChecker: NetworkChecker
) {
    suspend operator fun invoke(entity: Entity): String {
        if (networkChecker.isNetworkAvailable()) {
            try {
                val newEntity = remoteRepository.enrollInEvent(entity)
                Log.d("EnrollInEventUseCase", "newEntity: $newEntity")
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