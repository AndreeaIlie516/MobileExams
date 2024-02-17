package com.andreeailie.reexam.feature.presentation.add_edit_entity

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreeailie.reexam.feature.domain.model.Entity
import com.andreeailie.reexam.feature.domain.model.InvalidEntityException
import com.andreeailie.reexam.feature.domain.use_case.EntityUseCases
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

    private val _entityType = mutableStateOf(
        EntityTextFieldState(
            hint = "Enter type"
        )
    )

    val entityType: State<EntityTextFieldState> = _entityType

    private val _entityCalories = mutableStateOf(
        EntityTextFieldState(
            hint = "Enter caloriess"
        )
    )

    val entityCalories: State<EntityTextFieldState> = _entityCalories

    private val _entityDate = mutableStateOf(
        EntityTextFieldState(
            hint = "Enter date"
        )
    )

    val entityDate: State<EntityTextFieldState> = _entityDate

    private val _entityNotes = mutableStateOf(
        EntityTextFieldState(
            hint = "Enter notes"
        )
    )

    val entityNotes: State<EntityTextFieldState> = _entityNotes

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
                        _entityType.value = entityType.value.copy(
                            text = entity.type,
                            isHintVisible = false
                        )
                        _entityCalories.value = entityCalories.value.copy(
                            text = entity.calories.toString(),
                            isHintVisible = false
                        )
                        _entityDate.value = entityDate.value.copy(
                            text = entity.date,
                            isHintVisible = false
                        )
                        _entityNotes.value = entityNotes.value.copy(
                            text = entity.notes,
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

            is AddEditEntityEvent.EnteredCalories -> {
                _entityCalories.value = entityCalories.value.copy(
                    text = event.value
                )
            }

            is AddEditEntityEvent.ChangeCaloriesFocus -> {
                _entityCalories.value = entityCalories.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            entityCalories.value.text.isBlank()
                )
            }

            is AddEditEntityEvent.EnteredDate -> {
                _entityDate.value = entityDate.value.copy(
                    text = event.value
                )
            }

            is AddEditEntityEvent.ChangeDateFocus -> {
                _entityDate.value = entityDate.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            entityDate.value.text.isBlank()
                )
            }

            is AddEditEntityEvent.EnteredNotes -> {
                _entityNotes.value = entityNotes.value.copy(
                    text = event.value
                )
            }

            is AddEditEntityEvent.ChangeNotesFocus -> {
                _entityNotes.value = entityNotes.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            entityNotes.value.text.isBlank()
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
                                type = entityType.value.text,
                                calories = entityCalories.value.text.toInt(),
                                date = entityDate.value.text,
                                notes = entityNotes.value.text,
                                action = null
                            )
                        } else {
                            Entity(
                                idLocal = 0,
                                id = 0,
                                name = entityName.value.text,
                                type = entityType.value.text,
                                calories = entityCalories.value.text.toInt(),
                                date = entityDate.value.text,
                                notes = entityNotes.value.text,
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
                                type = entityType.value.text,
                                calories = entityCalories.value.text.toInt(),
                                date = entityDate.value.text,
                                notes = entityNotes.value.text,
                                action = null
                            )
                        } else {
                            Entity(
                                idLocal = 0,
                                id = 0,
                                name = entityName.value.text,
                                type = entityType.value.text,
                                calories = entityCalories.value.text.toInt(),
                                date = entityDate.value.text,
                                notes = entityNotes.value.text,
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