package com.andreeailie.reexam.feature.presentation.entities

import com.andreeailie.reexam.feature.domain.model.Entity

sealed class EntitiesEvent {
    data class CreateEntity(val entity: Entity) : EntitiesEvent()
    data class UpdateEntity(val entity: Entity) : EntitiesEvent()
    data class DeleteEntity(val entity: Entity) : EntitiesEvent()
}