package com.andreeailie.exam.feature.domain.use_case

import com.andreeailie.exam.feature.domain.model.Entity
import com.andreeailie.exam.feature.domain.repository.LocalEntityRepository

class GetEntityUseCase(
    private val repository: LocalEntityRepository
) {

    suspend operator fun invoke(id: Int): Entity? {
        return repository.getEntityById(id)
    }
}