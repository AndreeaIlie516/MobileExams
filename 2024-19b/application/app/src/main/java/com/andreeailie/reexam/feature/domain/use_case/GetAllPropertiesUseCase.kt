package com.andreeailie.reexam.feature.domain.use_case

import android.util.Log
import com.andreeailie.reexam.feature.data.network.NetworkStatusTracker
import com.andreeailie.reexam.feature.domain.model.Property
import com.andreeailie.reexam.feature.domain.repository.LocalEntityRepository
import com.andreeailie.reexam.feature.domain.repository.RemoteEntityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn

class GetAllPropertiesUseCase(
    private val localRepository: LocalEntityRepository,
    private val remoteRepository: RemoteEntityRepository,
    private val networkStatusTracker: NetworkStatusTracker
) {
    operator fun invoke(): Flow<List<Property>> = flow {
        val localProperties = localRepository.getAllProperties().first()
        val isNetworkAvailable = networkStatusTracker.isCurrentlyAvailable()

        if (localProperties.isEmpty()) {
            if (isNetworkAvailable) {
                try {
                    val remotePropertiesStrings = remoteRepository.getAllProperties()
                    Log.d("GetPropertiesUseCase", "remoteProperties: $remotePropertiesStrings")

                    val remoteProperties = remotePropertiesStrings.map { name ->
                        Property(idLocal = 0, name = name)
                    }

                    localRepository.clearAndCacheProperties(flowOf(remoteProperties))

                    emitAll(flowOf(remoteProperties))
                } catch (e: Exception) {
                    emitAll(flowOf(localProperties))
                }
            } else {
                emitAll(flowOf(localProperties))
            }
        } else {
            emitAll(flowOf(localProperties))
        }
    }.flowOn(Dispatchers.IO)
}