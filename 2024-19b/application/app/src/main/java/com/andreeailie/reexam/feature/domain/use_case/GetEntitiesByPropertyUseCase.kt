package com.andreeailie.reexam.feature.domain.use_case

import com.andreeailie.reexam.feature.domain.model.Entity
import com.andreeailie.reexam.feature.domain.model.Property
import com.andreeailie.reexam.feature.domain.repository.LocalEntityRepository
import com.andreeailie.reexam.feature.domain.repository.RemoteEntityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetEntitiesByPropertyUseCase (
    private val localRepository: LocalEntityRepository,
    private val remoteRepository: RemoteEntityRepository
    ) {
    operator fun invoke(property: Property): Flow<List<Entity>> = flow {
        emit(remoteRepository.getEntitiesByProperty(property.name))
    }.flowOn(Dispatchers.IO)
}