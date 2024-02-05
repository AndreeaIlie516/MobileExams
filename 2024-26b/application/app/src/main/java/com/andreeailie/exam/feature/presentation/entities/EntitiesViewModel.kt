package com.andreeailie.exam.feature.presentation.entities

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.toUpperCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreeailie.exam.feature.domain.model.Entity
import com.andreeailie.exam.feature.domain.model.Property
import com.andreeailie.exam.feature.domain.repository.LocalEntityRepository
import com.andreeailie.exam.feature.domain.repository.RemoteEntityRepository
import com.andreeailie.exam.feature.domain.use_case.EntityUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntitiesViewModel
@Inject
constructor(
    private val entityUseCases: EntityUseCases,
    private val localEntityRepository: LocalEntityRepository,
    private val remoteEntityRepository: RemoteEntityRepository
) : ViewModel() {
    private val _state = mutableStateOf(EntitiesState())
    val state: State<EntitiesState> = _state

    private val _inProgressEvents = mutableStateOf<List<Entity>>(listOf())
    val inProgressEvents: State<List<Entity>> = _inProgressEvents

    private val _topEvents = mutableStateOf<List<Entity>>(listOf())
    val topEvents: State<List<Entity>> = _topEvents

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private var getPropertiesJob: Job? = null
    private var getEntitiesJob: Job? = null
    private var getEntitiesForPropertiesJob: Job? = null

    val snackbarHostState = SnackbarHostState()

    private val _showCreationDialog = mutableStateOf(false)
    val showCreationDialog: State<Boolean> = _showCreationDialog

    private val _newEntity = mutableStateOf<Entity?>(null)
    val newEntity: State<Entity?> = _newEntity

    val deletionStatus = MutableStateFlow<Boolean?>(null)

    init {
        performServerOperation {
            getEntities()
            getInProgressEvents()
            //calculateMonthlyDurations()
            //calculateTopCategories()

            calculateTopEvents()
        }
        observeWebSocketEvents()
    }

    private fun performServerOperation(operation: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                operation()
            } catch (e: Exception) {
                Log.e("EntitiesViewModel", "Error in server operation: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

//    private suspend fun getProperties() {
//        Log.d("EntitiesViewModel", "getProperties() called")
//        val properties = entityUseCases.getPropertiesUseCase().first()
//        _state.value = state.value.copy(properties = properties)
//        Log.d("EntitiesViewModel", "Properties: ${state.value.properties}")
//
//    }

    private suspend fun getEntities() {
        Log.d("EntitiesViewModel", "getEntities() called")
        val entities = entityUseCases.getAllEntitiesUseCase().first()
        _state.value = state.value.copy(entities = entities)
        Log.d("EntitiesViewModel", "ViewModel state updated with new entities: ${state.value.entities}")
    }


//    private suspend fun getEntitiesForProperties() {
//        Log.d("EntitiesViewModel", "getEntitiesForProperties() called")
//        val newEntitiesMap = mutableMapOf<Property, List<Entity>>()
//        state.value.properties.forEach { property ->
//            val entities = entityUseCases.getEntitiesByPropertyUseCase(property).first()
//            newEntitiesMap[property] = entities
//        }
//        _state.value = state.value.copy(entitiesForProperties = newEntitiesMap)
//    }


//    fun getEntitiesForProperty(property: Property): Flow<List<Entity>> = flow {
//        val entitiesForProperties = _state.value.entitiesForProperties
//        if (entitiesForProperties.containsKey(property)) {
//            emit(entitiesForProperties[property] ?: emptyList())
//        } else {
//            try {
//                val fetchedEntities = entityUseCases.getEntitiesByPropertyUseCase(property)
//                    .first()
//
//                val updatedMap = entitiesForProperties.toMutableMap().apply {
//                    put(property, fetchedEntities)
//                }
//
//                _state.value = _state.value.copy(entitiesForProperties = updatedMap)
//                emit(fetchedEntities)
//            } catch (e: Exception) {
//                Log.e("EntitiesViewModel", "Error fetching entities for property $property: ${e.message}")
//                emit(emptyList())
//            }
//        }
//    }

    fun onEvent(event: EntitiesEvent) {
        when (event) {
            is EntitiesEvent.CreateEntity ->  performServerOperation {
                val result = entityUseCases.addEntityUseCase(event.entity)
                //handleOperationResult(result, "Created")
                //getProperties()
                getEntities()
                //getEntitiesForProperties()
            }

            is EntitiesEvent.DeleteEntity -> performServerOperation {
                    try {
                        val result = entityUseCases.deleteEntityUseCase(event.entity)
                        handleOperationResult(result, "Deleted")
                        Log.d("EntitiesViewModel", "On try")
                        Log.d("EntitiesViewModel", "Result: $result")
                        if (result == "Success") {
                            Log.d("EntitiesViewModel", "Success")
                            deletionStatus.emit(true)
                            //getProperties()
                            getEntities()
                            //getEntitiesForProperties()
                        } else {
                            deletionStatus.emit(false)
                            Log.d("EntitiesViewModel", "Failure")
                            showSnackbarMessage("Cannot delete event when offline")
                        }
                    } catch (e: Exception) {
                        deletionStatus.emit(false)
                        Log.d("EntitiesViewModel", "On catch")
                        showSnackbarMessage("Cannot delete event when offline")
                    }
                }

            is EntitiesEvent.UpdateEntity -> performServerOperation {
                    val result = entityUseCases.updateEntityUseCase(event.entity)
                    handleOperationResult(result, "Updated")
                    //getProperties()
                    getEntities()
                    //getEntitiesForProperties()
                }
        }
    }

    private fun observeWebSocketEvents() {
        viewModelScope.launch {
            remoteEntityRepository.observeWebSocketEvents().collect { webSocketUpdate ->
                when (webSocketUpdate.action) {
                    "CreatedEntity" -> {
                        val newEntity = Entity(
                            name = webSocketUpdate.entity.name,
                            team = webSocketUpdate.entity.team,
                            details = webSocketUpdate.entity.details,
                            status = webSocketUpdate.entity.status,
                            participants = webSocketUpdate.entity.participants,
                            type = webSocketUpdate.entity.type,
                            id = 0,
                            idLocal = 0,
                            action = null
                        )
                        val message = buildEntityNotificationMessage(newEntity)
                        snackbarHostState.showSnackbar(message)
                        addEntityAndUpdateState(newEntity)
                        _newEntity.value = newEntity
                        _showCreationDialog.value = true
                    }
                }
            }
        }
    }

    private suspend fun addEntityAndUpdateState(entity: Entity) {
        entityUseCases.addEntityUseCase(entity)
        // Refresh the entities list
        val updatedEntities = entityUseCases.getAllEntitiesUseCase().first()
        Log.d("EntitiesViewModel", "updatedEntities: $updatedEntities")
        _state.value = _state.value.copy(entities = updatedEntities)
        performServerOperation {
            entityUseCases.addEntityUseCase(entity)
            //getProperties()
            getEntities()
            //getEntitiesForProperties()
        }

    }

    private suspend fun handleOperationResult(result: String, operation: String) {
        if (result == "Success") {
            Log.d("EntitiesViewModel", "$operation Success")
        } else {
            Log.d("EntitiesViewModel", "$operation Failure")
            snackbarHostState.showSnackbar("Failed to $operation entity")
        }
    }

    private fun buildEntityNotificationMessage(entity: Entity): String {
        // You can customize this message as per your requirement
        return "New Activity: ${entity.name}, ${entity.team} team, ${entity.details}"
    }

//    private suspend fun calculateMonthlyDurations() {
//        val entities = entityUseCases.getAllEntitiesUseCase().first()
//        val groupedByMonth = entities.groupBy {
//            it.date.substring(0, 7)
//        }
//
//        val durationsPerMonth = groupedByMonth.map { (month, entitiesList) ->
//            MonthlyDuration(
//                month = month,
//                totalDuration = entitiesList.sumOf { it.duration.toDouble() }
//            )
//        }.sortedByDescending { it.month }
//
//        _monthlyDurations.value = durationsPerMonth
//    }
//
//    data class MonthlyDuration(
//        val month: String,
//        val totalDuration: Double
//    )
//
//    private suspend fun calculateTopCategories() {
//        val activities = entityUseCases.getAllEntitiesUseCase().first()
//        val categoryCounts = activities.groupingBy { it.category }.eachCount()
//            .map { CategoryCount(it.key, it.value) }
//            .sortedByDescending { it.count }
//            .take(5)
//
//        _topEvents.value = categoryCounts
//    }

    private suspend fun calculateTopEvents() {
        val events = entityUseCases.getAllEntitiesUseCase().first()
        val sortedEvents = events.sortedWith(compareBy<Entity> { it.status.toUpperCase() }.thenByDescending { it.participants })
            .take(5)


        Log.d("EntitiesViewModel", "SortedEvents: $sortedEvents")

        _topEvents.value = sortedEvents
    }

    private suspend fun getInProgressEvents() {
        try {
            val inProgressEvents = entityUseCases.getInProgressEventsUseCase().first()
            _inProgressEvents.value = inProgressEvents
        } catch (e: Exception) {
            Log.e("EntitiesViewModel", "Error fetching in-progress events: ${e.message}")
        }
    }

    fun enrollInEvent(event: Entity) {
        viewModelScope.launch {
            try {
                    val result = entityUseCases.enrollInEventUseCase(event)
                    handleOperationResult(result, "Enrollment")
                    getInProgressEvents()
                    performServerOperation {  getEntities() }
                } catch (e: Exception) {
                    Log.e("EntitiesViewModel", "Error enrolling in event: ${e.message}")
                    showSnackbarMessage("Enrollment failed")
                }
            }
        }


    private fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    fun showSnackbarMessage(message: String) {
        viewModelScope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    suspend fun syncPendingChanges() {
        viewModelScope.launch(Dispatchers.IO) {
            val pendingEvents = localEntityRepository.getEventsWithPendingActions()
            pendingEvents.forEach { event ->
                when (event.action) {
                    "add" -> remoteEntityRepository.insertEntity(event)

                }
            }
        }
    }

    fun dismissCreationDialog() {
        _showCreationDialog.value = false
    }

}