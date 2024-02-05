package com.andreeailie.exam.feature.domain.use_case

import android.util.Log
import com.andreeailie.exam.feature.data.network.NetworkChecker
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

class GetPropertiesUseCase(
    private val localRepository: LocalEntityRepository,
    private val remoteRepository: RemoteEntityRepository,
    private val networkChecker: NetworkChecker
) {
//    operator fun invoke(): Flow<List<Property>> = flow {
//        val localProperties = localRepository.getProperties()
//
//        if (networkChecker.isNetworkAvailable()) {
//            try {
//                val remotePropertiesStrings = remoteRepository.getProperties()
//                Log.d("GetPropertiesUseCase", "remoteProperties: $remotePropertiesStrings")
//
//                val remoteProperties = remotePropertiesStrings.map { name ->
//                    Property(idLocal = 0, name = name)
//                }
//
//                val mergedProperties = localProperties.combine(flowOf(remoteProperties)) { local, remote ->
//                    Log.d("GetPropertiesUseCase", "Merging: Local - $local, Remote - $remote")
//                    val allProperties = (local + remote)
//                    allProperties.distinctBy { it.name }
//                }
//
//                localRepository.clearAndCacheProperties(mergedProperties)
//
//                emitAll(mergedProperties)
//            } catch (e: Exception) {
//                emitAll(localRepository.getProperties())
//            }
//        } else {
//           emitAll(localRepository.getProperties())
//        }
//    }.flowOn(Dispatchers.IO)
}