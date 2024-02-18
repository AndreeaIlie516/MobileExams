package com.andreeailie.reexam.feature.presentation.entities

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreeailie.reexam.feature.data.network.NetworkStatusTracker
import com.andreeailie.reexam.feature.data.network.WebSocketClient
import com.andreeailie.reexam.feature.domain.model.Entity
import com.andreeailie.reexam.feature.domain.model.Property
import com.andreeailie.reexam.feature.domain.repository.LocalEntityRepository
import com.andreeailie.reexam.feature.domain.repository.RemoteEntityRepository
import com.andreeailie.reexam.feature.domain.use_case.EntityUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntitiesViewModel
@Inject
constructor(
    private val entityUseCases: EntityUseCases,
    private val localEntityRepository: LocalEntityRepository,
    private val remoteEntityRepository: RemoteEntityRepository,
    private val networkStatusTracker: NetworkStatusTracker,
    private val webSocketClient: WebSocketClient
) : ViewModel() {
    private val _state = mutableStateOf(EntitiesState())
    val state: State<EntitiesState> = _state

    private val _topEntities = mutableStateOf<List<Entity>>(listOf())
    val topEntities: State<List<Entity>> = _topEntities

    private val _isNetworkAvailable = MutableStateFlow(false)
    val isNetworkAvailable: MutableStateFlow<Boolean> = _isNetworkAvailable


    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    val snackbarHostState = SnackbarHostState()

    private val _showCreationDialog = mutableStateOf(false)
    val showCreationDialog: State<Boolean> = _showCreationDialog

    private val _newEntity = mutableStateOf<Entity?>(null)
    val newEntity: State<Entity?> = _newEntity

    val deletionStatus = MutableStateFlow<Boolean?>(null)

    init {
        performServerOperation {
            getProperties()
            calculateTopEntities()
        }
        //observeWebSocketEvents()

        viewModelScope.launch {
            localEntityRepository.getAllEntities().collect { entities ->
                _state.value = _state.value.copy(entities = entities)
            }
        }
        observeNetworkStatus()
        observeWebSocketEvents()
        fetchEntitiesIfNeeded()
    }


    private fun observeNetworkStatus() {
        viewModelScope.launch {
            networkStatusTracker.networkStatus.collect { status ->
                _isNetworkAvailable.value = status == NetworkStatusTracker.NetworkStatus.Available
                if (_isNetworkAvailable.value) {
                    Log.d("EntitiesViewModel", "Network is available, syncing pending changes.")
                    syncPendingChanges()
                }
            }
        }
    }

    fun fetchEntitiesIfNeeded() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val entities = entityUseCases.getAllEntitiesUseCase(forceUpdate = _state.value.entities.isEmpty()).first()
                _state.value = _state.value.copy(entities = entities)
            } catch (e: Exception) {
                Log.e("EntitiesViewModel", "Error fetching entities: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
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

    private suspend fun getProperties() {
        Log.d("EntitiesViewModel", "getProperties() called")
        val properties = entityUseCases.getAllPropertiesUseCase().first()
        _state.value = state.value.copy(properties = properties)
        Log.d("EntitiesViewModel", "Properties: ${state.value.properties}")

    }

    private suspend fun getEntities() {
        Log.d("EntitiesViewModel", "getEntities() called")
        val entities = entityUseCases.getAllEntitiesUseCase().first()
        _state.value = state.value.copy(entities = entities)
        Log.d(
            "EntitiesViewModel",
            "ViewModel state updated with new entities: ${state.value.entities}"
        )
    }

    fun fetchEntitiesForSelectedProperty(property: Property) {
        viewModelScope.launch {
            try {
                val entities = entityUseCases.getEntitiesByPropertyUseCase(property).first()
                _state.value = _state.value.copy(
                    entitiesForProperties = _state.value.entitiesForProperties.toMutableMap().apply { put(property, entities) }
                )
            } catch (e: Exception) {
                Log.e("EntitiesViewModel", "Error fetching entities for property ${property.name}: ${e.message}")
            }
        }
    }

    private fun deleteEntityFromProperty(property: Property, entity: Entity) {
        val currentEntities = _state.value.entitiesForProperties[property]?.toMutableList() ?: mutableListOf()
        val updatedEntities = currentEntities.filterNot { it.id == entity.id }
        val updatedMap = _state.value.entitiesForProperties.toMutableMap().apply {
            put(property, updatedEntities)
        }
        _state.value = _state.value.copy(entitiesForProperties = updatedMap)
    }

    fun getPropertyByName(propertyName: String): Property? {
        return _state.value.properties.firstOrNull { it.name == propertyName }
    }

    fun onEvent(event: EntitiesEvent) {
        when (event) {
            is EntitiesEvent.CreateEntity -> performServerOperation {
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
                        val property = getPropertyByName(event.entity.type)
                        if(property != null) {
                            deleteEntityFromProperty(property = property, entity = event.entity)
                        }
                        viewModelScope.launch {
                            state.value.properties.forEach { property ->
                                localEntityRepository.getEntitiesByProperty(property).collect { entities ->
                                    _state.value = _state.value.copy(
                                            entitiesForProperties = _state.value.entitiesForProperties.toMutableMap().apply { put(property, entities) }
                                        )
                                }
                            }
                        }

                        Log.d("EntitiesViewModel", "entitiesForProperties: ${state.value.entitiesForProperties[property]}")
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

    private fun displayMealNotification() {
        _showCreationDialog.value = true
    }

    fun dismissCreationDialog() {
        _showCreationDialog.value = false
    }

    private fun observeWebSocketEvents() {
        viewModelScope.launch {
            try {
                webSocketClient.connect("ws://10.0.2.2:2320/ws")
                webSocketClient.events.collect { event ->
                    val newEntity = Entity(
                        idLocal = 0,
                        id = event.id,
                        name = event.name,
                        type = event.type,
                        calories = event.calories,
                        date = event.date,
                        notes = event.notes,
                        action = null
                    )
                    _newEntity.value = newEntity

                    val foundEntity = localEntityRepository.findEntityByAttributes(
                        newEntity.name,
                        newEntity.type,
                        newEntity.date,
                        newEntity.calories)

                    Log.d("EntitiesViewModel", "entity found: $foundEntity")
                    if(foundEntity == null) {
                        addEntityAndUpdateState(newEntity)
                        displayMealNotification()
                    }
                }
            } catch (e: Exception) {
                Log.e("EntitiesViewModel", "Error observing WebSocket events:", e)
                // Decide if reconnection is needed or if you want to signal an error state
            }
        }
    }

    private suspend fun addEntityAndUpdateState(entity: Entity) {
        entityUseCases.addEntityUseCase(entity)
        // Refresh the entities list
        val updatedEntities = entityUseCases.getAllEntitiesUseCase().first()
        Log.d("EntitiesViewModel", "updatedEntities: $updatedEntities")
        _state.value = _state.value.copy(entities = updatedEntities)
    }


    private suspend fun handleOperationResult(result: String, operation: String) {
        if (result == "Success") {
            Log.d("EntitiesViewModel", "$operation Success")
        } else {
            Log.d("EntitiesViewModel", "$operation Failure")
            snackbarHostState.showSnackbar("Failed to $operation entity")
        }
    }

    private suspend fun calculateTopEntities() {
        val entities = entityUseCases.getAllEntitiesUseCase().first()
        val sortedEntities = entities.sortedByDescending { it.calories }.take(10)

        Log.d("EntitiesViewModel", "SortedEntities: $sortedEntities")

        _topEntities.value = sortedEntities
    }
}