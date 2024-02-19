package com.andreeailie.reexam.feature.presentation.add_edit_entity

import androidx.compose.ui.focus.FocusState

sealed class AddEditEntityEvent {
    data class EnteredTitle(val value: String) : AddEditEntityEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : AddEditEntityEvent()
    data class EnteredAuthor(val value: String) : AddEditEntityEvent()
    data class ChangeAuthorFocus(val focusState: FocusState) : AddEditEntityEvent()
    data class EnteredGenre(val value: String) : AddEditEntityEvent()
    data class ChangeGenreFocus(val focusState: FocusState) : AddEditEntityEvent()
    data class EnteredYear(val value: String) : AddEditEntityEvent()
    data class ChangeYearFocus(val focusState: FocusState) : AddEditEntityEvent()
    data class EnteredIsbn(val value: String) : AddEditEntityEvent()
    data class ChangeIsbnFocus(val focusState: FocusState) : AddEditEntityEvent()
    data class EnteredAvailability(val value: String) : AddEditEntityEvent()
    data class ChangeAvailabilityFocus(val focusState: FocusState) : AddEditEntityEvent()

    object SaveNewEntity : AddEditEntityEvent()
    object SaveUpdatedEntity : AddEditEntityEvent()

}