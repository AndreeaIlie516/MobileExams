package com.andreeailie.exam.feature.presentation.entities

import com.andreeailie.exam.feature.domain.model.Entity
import com.andreeailie.exam.feature.domain.model.Property

data class EntitiesState(
    val entities: List<Entity> = emptyList(),
//    val properties: List<Property> = emptyList(),
//    val entitiesForProperties: Map<Property, List<Entity>> = emptyMap()
)