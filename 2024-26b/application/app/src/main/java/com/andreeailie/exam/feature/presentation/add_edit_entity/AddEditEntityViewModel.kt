package com.andreeailie.exam.feature.presentation.add_edit_entity

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreeailie.exam.feature.domain.model.Entity
import com.andreeailie.exam.feature.domain.model.InvalidEntityException
import com.andreeailie.exam.feature.domain.use_case.EntityUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditEntityViewModel
@Inject
constructor(
    private val entityUseCases: EntityUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _entityName = mutableStateOf(
        EntityTextFieldState(
            hint = "Enter name"
        )
    )

    val entityName: State<EntityTextFieldState> = _entityName

    private val _entityTeam = mutableStateOf(
        EntityTextFieldState(
            hint = "Enter team"
        )
    )

    val entityTeam: State<EntityTextFieldState> = _entityTeam

    private val _entityDetails = mutableStateOf(
        EntityTextFieldState(
            hint = "Enter details"
        )
    )

    val entityDetails: State<EntityTextFieldState> = _entityDetails

    private val _entityStatus = mutableStateOf(
        EntityTextFieldState(
            hint = "Enter status"
        )
    )

    val entityStatus: State<EntityTextFieldState> = _entityStatus

    private val _entityParticipants = mutableStateOf(
        EntityTextFieldState(
            hint = "Enter participants"
        )
    )

    val entityParticipants: State<EntityTextFieldState> = _entityParticipants

    private val _entityType = mutableStateOf(
        EntityTextFieldState(
            hint = "Enter type"
        )
    )

    val entityType: State<EntityTextFieldState> = _entityType

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var currentEntityId: String? = null

    init {

        savedStateHandle.get<Int>("entityId")?.let { entityId ->

            if (entityId != -1) {
                //currentEventId = entityId.toString()
                Log.i("AddEditEventViewModel", "entityId: $entityId")
                viewModelScope.launch {
                    entityUseCases.getEntityUseCase(entityId)?.also { entity ->
                        Log.i("AddEditEntityViewModel", "currentEntityID: $currentEntityId")
                        currentEntityId = entity.id.toString()
                        _entityName.value = entityName.value.copy(
                            text = entity.name,
                            isHintVisible = false
                        )
                        _entityTeam.value = entityTeam.value.copy(
                            text = entity.team,
                            isHintVisible = false
                        )
                        _entityDetails.value = entityDetails.value.copy(
                            text = entity.details,
                            isHintVisible = false
                        )
                        _entityStatus.value = entityStatus.value.copy(
                            text = entity.status,
                            isHintVisible = false
                        )
                        _entityParticipants.value = entityParticipants.value.copy(
                            text = entity.participants,
                            isHintVisible = false
                        )
                        _entityType.value = entityType.value.copy(
                            text = entity.type,
                            isHintVisible = false
                        )
                    }
                }
            }

        }
    }

    fun onEvent(event: AddEditEntityEvent) {
        when (event) {
            is AddEditEntityEvent.EnteredName -> {
                _entityName.value = entityName.value.copy(
                    text = event.value
                )
            }

            is AddEditEntityEvent.ChangeNameFocus -> {
                _entityName.value = entityName.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            entityName.value.text.isBlank()
                )
            }

            is AddEditEntityEvent.EnteredTeam -> {
                _entityTeam.value = entityTeam.value.copy(
                    text = event.value
                )
            }

            is AddEditEntityEvent.ChangeTeamFocus -> {
                _entityTeam.value = entityTeam.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            entityTeam.value.text.isBlank()
                )
            }

            is AddEditEntityEvent.EnteredDetails -> {
                _entityDetails.value = entityDetails.value.copy(
                    text = event.value
                )
            }

            is AddEditEntityEvent.ChangeDetailsFocus -> {
                _entityDetails.value = entityDetails.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            entityDetails.value.text.isBlank()
                )
            }

            is AddEditEntityEvent.EnteredStatus -> {
                _entityStatus.value = entityStatus.value.copy(
                    text = event.value
                )
            }

            is AddEditEntityEvent.ChangeStatusFocus -> {
                _entityStatus.value = entityStatus.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            entityStatus.value.text.isBlank()
                )
            }

            is AddEditEntityEvent.EnteredParticipants -> {
                _entityParticipants.value = entityParticipants.value.copy(
                    text = event.value
                )
            }

            is AddEditEntityEvent.ChangeParticipantsFocus -> {
                _entityParticipants.value = entityParticipants.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            entityParticipants.value.text.isBlank()
                )
            }

            is AddEditEntityEvent.EnteredType -> {
                _entityType.value = entityType.value.copy(
                    text = event.value
                )
            }

            is AddEditEntityEvent.ChangeTypeFocus -> {
                _entityType.value = entityType.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            entityType.value.text.isBlank()
                )
            }


            is AddEditEntityEvent.SaveNewEntity -> {
                viewModelScope.launch {
                    try {
                        val entity = if (currentEntityId != null) {
                            Entity(
                                idLocal = 0,
                                id = currentEntityId!!.toInt(),
                                name = entityName.value.text,
                                team = entityTeam.value.text,
                                details = entityDetails.value.text,
                                participants = entityParticipants.value.text,
                                status = entityStatus.value.text,
                                type = entityType.value.text,
                                action = null
                            )
                        } else {
                            Entity(
                                idLocal = 0,
                                id = 0,
                                name = entityName.value.text,
                                team = entityTeam.value.text,
                                details = entityDetails.value.text,
                                participants = entityParticipants.value.text,
                                status = entityStatus.value.text,
                                type = entityType.value.text,
                                action = null
                            )
                        }
                        entityUseCases.addEntityUseCase(entity)
                        _eventFlow.emit(UiEvent.SaveNewEntity)
                    } catch (e: InvalidEntityException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save entity"
                            )
                        )
                    }
                }

            }

            is AddEditEntityEvent.SaveUpdatedEntity -> {
                Log.d("AddEditEntityViewModel", "currentEntityId: $currentEntityId")
                viewModelScope.launch {
                    try {
                        val entity = if (currentEntityId != null) {
                            Entity(
                                idLocal = 0,
                                id = currentEntityId!!.toInt(),
                                name = entityName.value.text,
                                team = entityTeam.value.text,
                                details = entityDetails.value.text,
                                participants = entityParticipants.value.text,
                                status = entityStatus.value.text,
                                type = entityType.value.text,
                                action = null
                            )
                        } else {
                            Entity(
                                idLocal = 0,
                                id = 0,
                                name = entityName.value.text,
                                team = entityTeam.value.text,
                                details = entityDetails.value.text,
                                participants = entityParticipants.value.text,
                                status = entityStatus.value.text,
                                type = entityType.value.text,
                                action = null
                            )
                        }
                        entityUseCases.updateEntityUseCase(entity)
                        _eventFlow.emit(UiEvent.SaveUpdatedEntity)
                    } catch (e: InvalidEntityException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't update entity"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveNewEntity : UiEvent()
        object SaveUpdatedEntity : UiEvent()
    }
}