package com.andreeailie.exam.feature.domain.use_case

import android.util.Log
import com.andreeailie.exam.feature.data.network.NetworkChecker
import com.andreeailie.exam.feature.domain.model.Entity
import com.andreeailie.exam.feature.domain.repository.LocalEntityRepository
import com.andreeailie.exam.feature.domain.repository.RemoteEntityRepository

class DeleteEntityUseCase(
    private val localRepository: LocalEntityRepository,
    private val remoteRepository: RemoteEntityRepository,
    private val networkChecker: NetworkChecker
) {

    suspend operator fun invoke(entity: Entity): String {
        if (networkChecker.isNetworkAvailable()) {
            try {
                remoteRepository.deleteEntity(entity)
                localRepository.deleteEntity(entity)
                return "Success"
            } catch (e: Exception) {
                Log.d("DeleteEventUseCase", "Cannot delete")
                return "Failed"
                //throw Exception("Failed to delete the event. Please try again later.")
            }
        } else {
            Log.d("DeleteEventUseCase", "Not connected. Cannot delete")
            return "Failed"
            //throw Exception("Failed to delete the event. Please try again later.")
        }
    }
}