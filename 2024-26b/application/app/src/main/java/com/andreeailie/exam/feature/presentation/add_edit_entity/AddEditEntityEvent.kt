package com.andreeailie.exam.feature.presentation.add_edit_entity

import androidx.compose.ui.focus.FocusState

sealed class AddEditEntityEvent {
    data class EnteredName(val value: String) : AddEditEntityEvent()
    data class ChangeNameFocus(val focusState: FocusState) : AddEditEntityEvent()
    data class EnteredTeam(val value: String) : AddEditEntityEvent()
    data class ChangeTeamFocus(val focusState: FocusState) : AddEditEntityEvent()
    data class EnteredDetails(val value: String) : AddEditEntityEvent()
    data class ChangeDetailsFocus(val focusState: FocusState) : AddEditEntityEvent()
    data class EnteredStatus(val value: String) : AddEditEntityEvent()
    data class ChangeStatusFocus(val focusState: FocusState) : AddEditEntityEvent()
    data class EnteredParticipants(val value: String) : AddEditEntityEvent()
    data class ChangeParticipantsFocus(val focusState: FocusState) : AddEditEntityEvent()
    data class EnteredType(val value: String) : AddEditEntityEvent()
    data class ChangeTypeFocus(val focusState: FocusState) : AddEditEntityEvent()

    object SaveNewEntity : AddEditEntityEvent()
    object SaveUpdatedEntity : AddEditEntityEvent()

}