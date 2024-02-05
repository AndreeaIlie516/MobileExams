package com.andreeailie.exam.feature.domain.use_case

import com.andreeailie.exam.feature.domain.model.Entity
import com.andreeailie.exam.feature.domain.model.Property
import com.andreeailie.exam.feature.domain.repository.LocalEntityRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetEntitiesByPropertyUseCase (
    private val localRepository: LocalEntityRepository,

) {
//    operator fun invoke(property: Property): Flow<List<Entity>> = flow {
//        emitAll(localRepository.getEntitiesByProperty(property))
//    }.flowOn(Dispatchers.IO)
}