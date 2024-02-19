package com.andreeailie.reexam.feature.presentation.add_edit_entity

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
import com.andreeailie.reexam.R
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
    val titleState = viewModel.entityTitle.value
    val genreState = viewModel.entityGenre.value
    val authorState = viewModel.entityAuthor.value
    val yearState = viewModel.entityYear.value
    val isbnState = viewModel.entityIsbn.value
    val availabilityState = viewModel.entityAvailability.value

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
                    value = titleState.text,
                    label = { Text("Title") },
                    onValueChange = {
                        viewModel.onEvent(AddEditEntityEvent.EnteredTitle(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                OutlinedTextField(
                    value = authorState.text,
                    label = { Text("Author") },
                    onValueChange = {
                        viewModel.onEvent(AddEditEntityEvent.EnteredAuthor(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                OutlinedTextField(
                    value = genreState.text,
                    label = { Text("Genre") },
                    onValueChange = {
                        viewModel.onEvent(AddEditEntityEvent.EnteredGenre(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                OutlinedTextField(
                    value = yearState.text,
                    label = { Text("Year") },
                    onValueChange = {
                        viewModel.onEvent(AddEditEntityEvent.EnteredYear(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                OutlinedTextField(
                    value = isbnState.text,
                    label = { Text("ISBN") },
                    onValueChange = {
                        viewModel.onEvent(AddEditEntityEvent.EnteredIsbn(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                OutlinedTextField(
                    value = availabilityState.text,
                    label = { Text("Availability") },
                    onValueChange = {
                        viewModel.onEvent(AddEditEntityEvent.EnteredAvailability(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
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

