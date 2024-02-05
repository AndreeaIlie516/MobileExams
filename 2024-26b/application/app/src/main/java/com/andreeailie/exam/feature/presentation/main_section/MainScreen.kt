package com.andreeailie.exam.feature.presentation.main_section

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.andreeailie.exam.R
import com.andreeailie.exam.feature.domain.model.Entity
import com.andreeailie.exam.feature.domain.model.Property
import com.andreeailie.exam.feature.presentation.components.AddButton
import com.andreeailie.exam.feature.presentation.components.EntityCell
import com.andreeailie.exam.feature.presentation.entities.EntitiesEvent
import com.andreeailie.exam.feature.presentation.entities.EntitiesViewModel


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: EntitiesViewModel = hiltViewModel(),
    navController: NavController
) {
    val showCreationDialog by viewModel.showCreationDialog
    val newEntity by viewModel.newEntity

    if (showCreationDialog && newEntity != null) {
        AlertDialog(
            onDismissRequest = {
                viewModel.dismissCreationDialog()
            },
            title = {
                Text("New Entity Created")
            },
            text = {
                Column {
                    Text("Name: ${newEntity!!.name}")
                    Text("Team: ${newEntity!!.team}")
                    Text("Details: ${newEntity!!.details}")
                    Text("Status: ${newEntity!!.status}")
                    Text("Participants: ${newEntity!!.participants}")
                    Text("Type: ${newEntity!!.type}")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.dismissCreationDialog()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    val snackbarHostState = viewModel.snackbarHostState

    Scaffold (
        snackbarHost = { SnackbarHost(hostState = viewModel.snackbarHostState) }
    ){
        Column(
            modifier = modifier
        ) {
            Log.d("MainScreen", "Main")
            SetScreenTitle(modifier = modifier)
            PropertyList(viewModel = viewModel, modifier = modifier, navController = navController)
        }
    }
}

@Composable
fun SetScreenTitle(modifier: Modifier = Modifier) {
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
                text = "Home",
                textAlign = TextAlign.Left,
                color = colorResource(R.color.black),
                style = MaterialTheme.typography.headlineMedium,
            )
        }
    }
}

@Composable
fun SectionHeader(property: Entity, isExpanded: Boolean, onHeaderClicked: () -> Unit) {
    Row(modifier = Modifier
        .clickable { onHeaderClicked() }
        //.background(colorResource(id = R.color.light_purple2))
        .padding(vertical = 8.dp, horizontal = 16.dp)) {
        Text(
            text = "ID: " + property.id.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(0.2f)
        )
        Text(
            text = "Name: " + property.name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(0.2f)
        )
        Text(
            text = "Team: " + property.team,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(0.2f)
        )

        Text(
                text = "Type: " + property.type,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(0.2f)
            )
//        Column {
//            Text(
//                text = "ID" + property.id.toString(),
//                style = MaterialTheme.typography.headlineMedium,
//                modifier = Modifier.weight(1.0f)
//            )
//            Text(
//                text = "Name" + property.name,
//                style = MaterialTheme.typography.headlineMedium,
//                modifier = Modifier.weight(1.0f)
//            )
//            Text(
//                text = "Team" + property.team,
//                style = MaterialTheme.typography.headlineMedium,
//                modifier = Modifier.weight(1.0f)
//            )
//            Text(
//                text = "Type" + property.type,
//                style = MaterialTheme.typography.headlineMedium,
//                modifier = Modifier.weight(1.0f)
//            )
//        }
        Text(text = if (isExpanded) "▼" else "▶", fontSize = 18.sp)
    }
}

@Composable
fun PropertyList(
    viewModel: EntitiesViewModel,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val state = viewModel.state.value
    val expandedStateMap = remember { mutableStateMapOf<String, Boolean>() }
    val isLoading = viewModel.isLoading.value

    Box(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        LazyColumn(modifier = modifier.padding(top = 6.dp)) {
            items(state.entities) { entity ->
                val isExpanded = expandedStateMap[entity.name] ?: false
                PropertySection(
                    property = entity,
                    isExpanded = isExpanded,
                    onHeaderClick = {
                        expandedStateMap[entity.name] = !isExpanded
                    },
                    viewModel = viewModel
                )
            }
        }

        AddButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 45.dp, end = 10.dp),
            navController = navController
        )
    }
}


@Composable
fun PropertySection(
    property: Entity,
    isExpanded: Boolean,
    onHeaderClick: () -> Unit,
    viewModel: EntitiesViewModel
) {
    SectionHeader(
        property = property,
        isExpanded = isExpanded,
        onHeaderClicked = onHeaderClick
    )

    Log.d("MainScreen", "isExpanded: $isExpanded")
    if (isExpanded) {
        val entity = property
        Log.d("MainScreen", "Ent: $entity")
        //val entitiesForProperty = entities[property]
        //Log.d("MainScreen", "Entities: $entitiesForProperty")
        val entities = listOf(entity)
        if (entity != null) {
            LazyRow {
                    items(entities) { entity ->
                        EntityCell(entity = entity, onClick = {}, onDeleteConfirm = {
                            viewModel.onEvent(EntitiesEvent.DeleteEntity(entity))
                        })
                    }
                }
            } else {
                Text(
                    text = "No entities available",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }