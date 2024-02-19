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

    private val _entityTitle = mutableStateOf(
        EntityTextFieldState(
            hint = "Enter title"
        )
    )

    val entityTitle: State<EntityTextFieldState> = _entityTitle

    private val _entityAuthor = mutableStateOf(
        EntityTextFieldState(
            hint = "Enter author"
        )
    )

    val entityAuthor: State<EntityTextFieldState> = _entityAuthor

    private val _entityGenre = mutableStateOf(
        EntityTextFieldState(
            hint = "Enter genre"
        )
    )

    val entityGenre: State<EntityTextFieldState> = _entityGenre

    private val _entityYear = mutableStateOf(
        EntityTextFieldState(
            hint = "Enter year"
        )
    )

    val entityYear: State<EntityTextFieldState> = _entityYear

    private val _entityIsbn = mutableStateOf(
        EntityTextFieldState(
            hint = "Enter ISBN"
        )
    )

    val entityIsbn: State<EntityTextFieldState> = _entityIsbn

    private val _entityAvailability = mutableStateOf(
        EntityTextFieldState(
            hint = "Enter availability"
        )
    )

    val entityAvailability: State<EntityTextFieldState> = _entityAvailability

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
                        _entityTitle.value = entityTitle.value.copy(
                            text = entity.title,
                            isHintVisible = false
                        )
                        _entityAuthor.value = entityAuthor.value.copy(
                            text = entity.author,
                            isHintVisible = false
                        )
                        _entityGenre.value = entityGenre.value.copy(
                            text = entity.genre,
                            isHintVisible = false
                        )
                        _entityYear.value = entityYear.value.copy(
                            text = entity.year.toString(),
                            isHintVisible = false
                        )
                        _entityIsbn.value = entityIsbn.value.copy(
                            text = entity.ISBN,
                            isHintVisible = false
                        )
                        _entityAvailability.value = entityAvailability.value.copy(
                            text = entity.availability,
                            isHintVisible = false
                        )
                    }
                }
            }

        }
    }

    fun onEvent(event: AddEditEntityEvent) {
        when (event) {
            is AddEditEntityEvent.EnteredTitle -> {
                _entityTitle.value = entityTitle.value.copy(
                    text = event.value
                )
            }

            is AddEditEntityEvent.ChangeTitleFocus -> {
                _entityTitle.value = entityTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            entityTitle.value.text.isBlank()
                )
            }

            is AddEditEntityEvent.EnteredAuthor -> {
                _entityAuthor.value = entityAuthor.value.copy(
                    text = event.value
                )
            }

            is AddEditEntityEvent.ChangeAuthorFocus -> {
                _entityAuthor.value = entityAuthor.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            entityAuthor.value.text.isBlank()
                )
            }

            is AddEditEntityEvent.EnteredGenre -> {
                _entityGenre.value = entityGenre.value.copy(
                    text = event.value
                )
            }

            is AddEditEntityEvent.ChangeGenreFocus -> {
                _entityGenre.value = entityGenre.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            entityGenre.value.text.isBlank()
                )
            }

            is AddEditEntityEvent.EnteredYear -> {
                _entityYear.value = entityYear.value.copy(
                    text = event.value
                )
            }

            is AddEditEntityEvent.ChangeYearFocus -> {
                _entityYear.value = entityYear.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            entityYear.value.text.isBlank()
                )
            }

            is AddEditEntityEvent.EnteredIsbn -> {
                _entityIsbn.value = entityIsbn.value.copy(
                    text = event.value
                )
            }

            is AddEditEntityEvent.ChangeIsbnFocus -> {
                _entityIsbn.value = entityIsbn.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            entityIsbn.value.text.isBlank()
                )
            }

            is AddEditEntityEvent.EnteredAvailability -> {
                _entityAvailability.value = entityAvailability.value.copy(
                    text = event.value
                )
            }

            is AddEditEntityEvent.ChangeAvailabilityFocus -> {
                _entityAvailability.value = entityAvailability.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            entityAvailability.value.text.isBlank()
                )
            }


            is AddEditEntityEvent.SaveNewEntity -> {
                viewModelScope.launch {
                    try {
                        val entity = if (currentEntityId != null) {
                            Entity(
                                idLocal = 0,
                                id = currentEntityId!!.toInt(),
                                title = entityTitle.value.text,
                                author = entityAuthor.value.text,
                                genre = entityGenre.value.text,
                                year = entityYear.value.text.toInt(),
                                ISBN = entityIsbn.value.text,
                                availability = entityAvailability.value.text,
                                action = null
                            )
                        } else {
                            Entity(
                                idLocal = 0,
                                id = 0,
                                title = entityTitle.value.text,
                                author = entityAuthor.value.text,
                                genre = entityGenre.value.text,
                                year = entityYear.value.text.toInt(),
                                ISBN = entityIsbn.value.text,
                                availability = entityAvailability.value.text,
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
                                title = entityTitle.value.text,
                                author = entityAuthor.value.text,
                                genre = entityGenre.value.text,
                                year = entityYear.value.text.toInt(),
                                ISBN = entityIsbn.value.text,
                                availability = entityAvailability.value.text,
                                action = null
                            )
                        } else {
                            Entity(
                                idLocal = 0,
                                id = 0,
                                title = entityTitle.value.text,
                                author = entityAuthor.value.text,
                                genre = entityGenre.value.text,
                                year = entityYear.value.text.toInt(),
                                ISBN = entityIsbn.value.text,
                                availability = entityAvailability.value.text,
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