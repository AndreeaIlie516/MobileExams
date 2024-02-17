package com.andreeailie.reexam.feature.domain.use_case

import com.andreeailie.reexam.feature.domain.model.Entity
import com.andreeailie.reexam.feature.domain.repository.LocalEntityRepository
import com.andreeailie.reexam.feature.domain.repository.RemoteEntityRepository

class GetEntityUseCase(
    private val localRepository: LocalEntityRepository,
    private val remoteRepository: RemoteEntityRepository
) {

    suspend operator fun invoke(id: Int): Entity? {
        return remoteRepository.getEntityById(id)
    }
}