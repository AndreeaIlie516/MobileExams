package com.andreeailie.exam.feature.domain.use_case

import android.util.Log
import com.andreeailie.exam.feature.data.network.NetworkChecker
import com.andreeailie.exam.feature.domain.model.Entity
import com.andreeailie.exam.feature.domain.repository.LocalEntityRepository
import com.andreeailie.exam.feature.domain.repository.RemoteEntityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn

class GetInProgressEventsUseCase(
    private val localRepository: LocalEntityRepository,
    private val remoteRepository: RemoteEntityRepository,
    private val networkChecker: NetworkChecker
) {
    operator fun invoke(): Flow<List<Entity>> = flow {

        if (networkChecker.isNetworkAvailable()) {
            try {
                val entities = remoteRepository.getInProgressEvents()
                emit(entities)
            } catch (e: Exception) {
                Log.d("DeleteEventUseCase", "Cannot delete")
            }
        }
    }.flowOn(Dispatchers.IO)
}