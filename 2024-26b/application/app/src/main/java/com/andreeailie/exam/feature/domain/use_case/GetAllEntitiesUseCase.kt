package com.andreeailie.exam.feature.domain.use_case

import android.util.Log
import com.andreeailie.exam.feature.data.network.NetworkChecker
import com.andreeailie.exam.feature.domain.model.Entity
import com.andreeailie.exam.feature.domain.model.Property
import com.andreeailie.exam.feature.domain.repository.LocalEntityRepository
import com.andreeailie.exam.feature.domain.repository.RemoteEntityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn

class GetAllEntitiesUseCase (
    private val localRepository: LocalEntityRepository,
    private val remoteRepository: RemoteEntityRepository,
    private val networkChecker: NetworkChecker
    ) {
        operator fun invoke(): Flow<List<Entity>> = flow {
            val localEntities = localRepository.getAllEntities()

            if (networkChecker.isNetworkAvailable()) {
                try {
                    val remoteEntities = remoteRepository.getAllEntities()
                    Log.d("GetEntitiesUseCase", "remoteEntities: $remoteEntities")

                    val mergedEntities = localEntities.combine(flowOf(remoteEntities)) { local, remote ->
                        Log.d("GetPropertiesUseCase", "Merging: Local - $local, Remote - $remote")
                        val allEntities = (local + remote)
                        allEntities.distinctBy { Triple(it.name, it.team, it.status) }
                    }

                    localRepository.clearAndCacheEntities(mergedEntities)

                    emitAll(mergedEntities)
                } catch (e: Exception) {
                    emitAll(localRepository.getAllEntities())
                }
            } else {
                emitAll(localRepository.getAllEntities())
            }
        }.flowOn(Dispatchers.IO)
    }