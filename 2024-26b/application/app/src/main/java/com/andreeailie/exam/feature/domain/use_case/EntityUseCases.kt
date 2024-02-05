package com.andreeailie.exam.feature.domain.use_case

data class EntityUseCases(
    val getAllEntitiesUseCase: GetAllEntitiesUseCase,
    //val getPropertiesUseCase: GetPropertiesUseCase,
    //val getEntitiesByPropertyUseCase: GetEntitiesByPropertyUseCase,
    val getEntityUseCase: GetEntityUseCase,
    val addEntityUseCase: AddEntityUseCase,
    val deleteEntityUseCase: DeleteEntityUseCase,
    val updateEntityUseCase: UpdateEntityUseCase,
    val enrollInEventUseCase: EnrollInEventUseCase,
    val getInProgressEventsUseCase: GetInProgressEventsUseCase
)