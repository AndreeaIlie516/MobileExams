package com.andreeailie.reexam.feature.domain.use_case

import android.util.Log
import com.andreeailie.reexam.feature.data.network.NetworkStatusTracker
import com.andreeailie.reexam.feature.domain.model.Entity
import com.andreeailie.reexam.feature.domain.repository.LocalEntityRepository
import com.andreeailie.reexam.feature.domain.repository.RemoteEntityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
//
//class GetAllEntitiesUseCase (
//    private val localRepository: LocalEntityRepository,
//    private val remoteRepository: RemoteEntityRepository,
//    private val networkChecker: NetworkChecker
//) {
//    operator fun invoke(): Flow<List<Entity>> = flow {
//        val localEntities = localRepository.getAllEntities()
//
//        if (networkChecker.isNetworkAvailable()) {
//            try {
//                val remoteEntities = remoteRepository.getAllEntities()
//                Log.d("GetEntitiesUseCase", "remoteEntities: $remoteEntities")
//
//                val mergedEntities = localEntities.combine(flowOf(remoteEntities)) { local, remote ->
//                    Log.d("GetPropertiesUseCase", "Merging: Local - $local, Remote - $remote")
//                    val allEntities = (local + remote)
//                    allEntities.distinctBy { Triple(it.name, it.type, it.date) }
//                }
//
//                localRepository.clearAndCacheEntities(mergedEntities)
//
//                emitAll(mergedEntities)
//            } catch (e: Exception) {
//                emitAll(localRepository.getAllEntities())
//            }
//        } else {
//            emitAll(localRepository.getAllEntities())
//        }
//    }.flowOn(Dispatchers.IO)
//
//}

class GetAllEntitiesUseCase(
    private val localRepository: LocalEntityRepository,
    private val remoteRepository: RemoteEntityRepository,
    private val networkStatusTracker: NetworkStatusTracker
) {
    operator fun invoke(forceUpdate: Boolean = false): Flow<List<Entity>> = flow {
        val isNetworkAvailable = networkStatusTracker.isCurrentlyAvailable()
        val localEntities = localRepository.getAllEntities().first()

        if (localEntities.isEmpty()) {
            if (isNetworkAvailable) {
                try {
                    val remoteEntities = remoteRepository.getAllEntities()
                    Log.d("GetEntitiesUseCase", "Fetched remote entities: $remoteEntities")

                    localRepository.clearAndCacheEntities(flowOf(remoteEntities))

                    emitAll(flowOf(remoteEntities))
                } catch (e: Exception) {
                    Log.e("GetEntitiesUseCase", "Error fetching from remote, falling back to local cache")
                    emitAll(flowOf(localEntities))
                }
            } else {
                emitAll(flowOf(localEntities))
            }
        } else {
            emitAll(flowOf(localEntities))
        }
    }.flowOn(Dispatchers.IO)
}