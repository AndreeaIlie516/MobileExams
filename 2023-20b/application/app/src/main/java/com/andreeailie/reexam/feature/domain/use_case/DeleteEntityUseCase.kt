package com.andreeailie.reexam.feature.domain.use_case

import android.util.Log
import com.andreeailie.reexam.feature.data.network.NetworkStatusTracker
import com.andreeailie.reexam.feature.domain.model.Entity
import com.andreeailie.reexam.feature.domain.repository.LocalEntityRepository
import com.andreeailie.reexam.feature.domain.repository.RemoteEntityRepository

class DeleteEntityUseCase(
    private val localRepository: LocalEntityRepository,
    private val remoteRepository: RemoteEntityRepository,
    private val networkStatusTracker: NetworkStatusTracker
) {

    suspend operator fun invoke(entity: Entity): String {
        val isNetworkAvailable = networkStatusTracker.isCurrentlyAvailable()
        if (isNetworkAvailable) {
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