package com.andreeailie.reexam.feature.domain.use_case

data class EntityUseCases(
    val getAllEntitiesUseCase: GetAllEntitiesUseCase,
    val getAllPropertiesUseCase: GetAllPropertiesUseCase,
    val getEntitiesByPropertyUseCase: GetEntitiesByPropertyUseCase,
    val getEntityUseCase: GetEntityUseCase,
    val addEntityUseCase: AddEntityUseCase,
    val deleteEntityUseCase: DeleteEntityUseCase,
    val updateEntityUseCase: UpdateEntityUseCase,
    //val enrollInEventUseCase: EnrollInEventUseCase,
    //val getInProgressEventsUseCase: GetInProgressEventsUseCase
)