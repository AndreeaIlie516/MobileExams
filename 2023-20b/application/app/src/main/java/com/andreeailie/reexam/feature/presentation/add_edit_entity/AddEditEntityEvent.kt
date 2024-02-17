package com.andreeailie.reexam.feature.presentation.add_edit_entity

import androidx.compose.ui.focus.FocusState

sealed class AddEditEntityEvent {
    data class EnteredName(val value: String) : AddEditEntityEvent()
    data class ChangeNameFocus(val focusState: FocusState) : AddEditEntityEvent()
    data class EnteredType(val value: String) : AddEditEntityEvent()
    data class ChangeTypeFocus(val focusState: FocusState) : AddEditEntityEvent()
    data class EnteredCalories(val value: String) : AddEditEntityEvent()
    data class ChangeCaloriesFocus(val focusState: FocusState) : AddEditEntityEvent()
    data class EnteredDate(val value: String) : AddEditEntityEvent()
    data class ChangeDateFocus(val focusState: FocusState) : AddEditEntityEvent()
    data class EnteredNotes(val value: String) : AddEditEntityEvent()
    data class ChangeNotesFocus(val focusState: FocusState) : AddEditEntityEvent()

    object SaveNewEntity : AddEditEntityEvent()
    object SaveUpdatedEntity : AddEditEntityEvent()

}