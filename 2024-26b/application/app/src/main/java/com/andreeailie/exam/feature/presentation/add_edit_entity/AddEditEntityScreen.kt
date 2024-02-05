package com.andreeailie.exam.feature.presentation.add_edit_entity

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.andreeailie.exam.R
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SetScreenTitle(
    modifier: Modifier = Modifier,
    screenTitle: String
) {
    Row(
        modifier = modifier
            .padding(start = 25.dp, top = 65.dp, bottom = 20.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Column(
            modifier = modifier
        )
        {
            Text(
                text = screenTitle,
                textAlign = TextAlign.Left,
                color = colorResource(R.color.black),
                style = MaterialTheme.typography.headlineMedium,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddEditEntityScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: AddEditEntityViewModel = hiltViewModel(),
) {
    val nameState = viewModel.entityName.value
    val teamState = viewModel.entityTeam.value
    val participantsState = viewModel.entityParticipants.value
    val detailsState = viewModel.entityDetails.value
    val statusState = viewModel.entityStatus.value
    val typeState = viewModel.entityType.value



    val snackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()

    val entityId = viewModel.currentEntityId

    val screenScope = if (entityId == null) {
        "add"
    } else {
        "update"
    }

    Log.d("AddEditEntityScreen", "Entity: ${viewModel.currentEntityId}")

    val titleTextId = if (entityId == null) {
        R.string.add_entity_screen
    } else {
        R.string.update_entity_screen
    }
    val buttonTextId = if (entityId == null) {
        R.string.add_entity_button_label
    } else {
        R.string.update_entity_button_label
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditEntityViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is AddEditEntityViewModel.UiEvent.SaveNewEntity -> {
                    Log.d("AddEditEntityScreen", "Should navigate up")
                    navController.navigateUp()
                }

                is AddEditEntityViewModel.UiEvent.SaveUpdatedEntity -> {
                    Log.d("AddEditEntityScreen", "Should navigate up")
                    navController.navigateUp()
                }
            }
        }
    }
    Column(
        modifier = modifier
    )
    {
        SetScreenTitle(
            modifier = modifier,
            stringResource(id = titleTextId)
        )
        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = modifier
            ) {
                OutlinedTextField(
                    value = nameState.text,
                    label = { Text("Name") },
                    onValueChange = {
                        viewModel.onEvent(AddEditEntityEvent.EnteredName(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                OutlinedTextField(
                    value = teamState.text,
                    label = { Text("Team") },
                    onValueChange = {
                        viewModel.onEvent(AddEditEntityEvent.EnteredTeam(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                OutlinedTextField(
                    value = detailsState.text,
                    label = { Text("Details") },
                    onValueChange = {
                        viewModel.onEvent(AddEditEntityEvent.EnteredDetails(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                OutlinedTextField(
                    value = statusState.text,
                    label = { Text("status") },
                    onValueChange = {
                        viewModel.onEvent(AddEditEntityEvent.EnteredStatus(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                OutlinedTextField(
                    value = participantsState.text,
                    label = { Text("Description") },
                    onValueChange = {
                        viewModel.onEvent(AddEditEntityEvent.EnteredParticipants(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                OutlinedTextField(
                    value = typeState.text,
                    label = { Text("Type") },
                    onValueChange = {
                        viewModel.onEvent(AddEditEntityEvent.EnteredType(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 480.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Button(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(start = 15.dp, end = 15.dp)
                        .align(Alignment.BottomEnd),
                    shape = AbsoluteRoundedCornerShape(
                        topLeft = 15.dp,
                        topRight = 15.dp,
                        bottomLeft = 15.dp,
                        bottomRight = 15.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.purple)
                    ),
                    onClick = {
                        if (screenScope == "add") {
                            viewModel.onEvent(AddEditEntityEvent.SaveNewEntity)
                        } else {
                            viewModel.onEvent(AddEditEntityEvent.SaveUpdatedEntity)
                        }
                    }
                ) {
                    Text(

                        text = stringResource(id = buttonTextId),
                        color = colorResource(id = R.color.white),
                        style = TextStyle(
                            fontSize = 20.sp,
                        ),
                    )
                }

            }
        }
    }
}

