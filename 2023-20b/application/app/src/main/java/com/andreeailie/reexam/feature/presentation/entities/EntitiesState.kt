package com.andreeailie.reexam.feature.presentation.entities

import com.andreeailie.reexam.feature.domain.model.Entity
import com.andreeailie.reexam.feature.domain.model.Property

data class EntitiesState(
    val entities: List<Entity> = emptyList(),
    val properties: List<Property> = emptyList(),
    val entitiesForProperties: Map<Property, List<Entity>> = emptyMap()
)