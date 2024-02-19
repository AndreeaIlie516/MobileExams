package com.andreeailie.reexam.feature.presentation.main_section

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.material3.Button
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.andreeailie.reexam.R
import com.andreeailie.reexam.feature.domain.model.Entity
import com.andreeailie.reexam.feature.presentation.SetTitle
import com.andreeailie.reexam.feature.presentation.components.AddButton
import com.andreeailie.reexam.feature.presentation.components.EntityCell
import com.andreeailie.reexam.feature.presentation.entities.EntitiesEvent
import com.andreeailie.reexam.feature.presentation.entities.EntitiesViewModel

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
                    Text("Name: ${newEntity!!.title}")
                    Text("Type: ${newEntity!!.author}")
                    Text("Calories: ${newEntity!!.genre}")
                    Text("ISBN: ${newEntity!!.ISBN}")
                    Text("Year: ${newEntity!!.year}")
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

    Scaffold (
        snackbarHost = { SnackbarHost(hostState = viewModel.snackbarHostState) }
    ){
        Column(
            modifier = modifier
        ) {
            Log.d("MainScreen", "Main")
            SetTitle()
            SetScreenTitle(modifier = modifier)
            EntityListSimple(viewModel = viewModel, modifier = modifier, navController = navController)
        }
    }
}

@Composable
fun SetScreenTitle(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .padding(start = 25.dp, top = 10.dp, bottom = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Column(
            modifier = modifier
        )
        {
            Text(
                text = "Registration",
                textAlign = TextAlign.Left,
                color = colorResource(R.color.black),
                style = MaterialTheme.typography.headlineLarge,
            )
        }
    }
}

@Composable
fun EntityListSimple(
    viewModel: EntitiesViewModel,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val state = viewModel.state.value
    val isLoading = viewModel.isLoading.value
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsState()

    Log.d("MainScreen", "entities: ${state.entities}")

    Box(modifier = modifier
        .padding(bottom = 60.dp, start = 10.dp, end = 10.dp)
        .fillMaxSize()) {
        when {
            isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            state.entities.isNotEmpty() ->{
//                LazyColumn(modifier = modifier.padding(top = 6.dp)) {
//                    items(state.entities) { entity ->
//                        EntityCellSimple(entity = entity, onClick = {})
//                    }
//                }
                PropertyList(viewModel = viewModel, navController = navController)
                AddButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 5.dp, end = 5.dp),
                    navController = navController
                )
            }
            !isNetworkAvailable || state.entities.isEmpty()-> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(70.dp)
                    ,
                    contentAlignment = Alignment.Center
                ) {
                    Column {

                    }
                    Text(
                        text = "Offline. Please check your internet connection.",
                        textAlign = TextAlign.Center,
                        color = colorResource(R.color.grey),
                        style = MaterialTheme.typography.headlineMedium,
                    )
                    Button(onClick = { viewModel.fetchEntitiesIfNeeded() }, modifier = Modifier.align(Alignment.Center)) {
                        Text("Retry")
                    }
                }

            }
        }
    }
}





//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@Composable
//fun MainScreen(
//    modifier: Modifier = Modifier,
//    viewModel: EntitiesViewModel = hiltViewModel(),
//    navController: NavController
//) {
//    val showCreationDialog by viewModel.showCreationDialog
//    val newEntity by viewModel.newEntity
//
//    if (showCreationDialog && newEntity != null) {
//        AlertDialog(
//            onDismissRequest = {
//                viewModel.dismissCreationDialog()
//            },
//            title = {
//                Text("New Entity Created")
//            },
//            text = {
//                Column {
//                    Text("Name: ${newEntity!!.name}")
//                    Text("Team: ${newEntity!!.team}")
//                    Text("Details: ${newEntity!!.details}")
//                    Text("Status: ${newEntity!!.status}")
//                    Text("Participants: ${newEntity!!.participants}")
//                    Text("Type: ${newEntity!!.type}")
//                }
//            },
//            confirmButton = {
//                TextButton(
//                    onClick = {
//                        viewModel.dismissCreationDialog()
//                    }
//                ) {
//                    Text("OK")
//                }
//            }
//        )
//    }
//
//    val snackbarHostState = viewModel.snackbarHostState
//
//    Scaffold (
//        snackbarHost = { SnackbarHost(hostState = viewModel.snackbarHostState) }
//    ){
//        Column(
//            modifier = modifier
//        ) {
//            Log.d("MainScreen", "Main")
//            SetScreenTitle(modifier = modifier)
//            PropertyList(viewModel = viewModel, modifier = modifier, navController = navController)
//        }
//    }
//}
//
//@Composable
//fun SetScreenTitle(modifier: Modifier = Modifier) {
//    Row(
//        modifier = modifier
//            .padding(start = 25.dp, top = 65.dp, bottom = 20.dp)
//            .fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically,
//    ) {
//
//        Column(
//            modifier = modifier
//        )
//        {
//            Text(
//                text = "Home",
//                textAlign = TextAlign.Left,
//                color = colorResource(R.color.black),
//                style = MaterialTheme.typography.headlineMedium,
//            )
//        }
//    }
//}
//
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
            text = "Title: " + property.title,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(0.2f)
        )
        Text(
            text = "Author: " + property.author,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(0.2f)
        )

        Text(
            text = "Availability: " + property.availability,
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
                val isExpanded = expandedStateMap[entity.title] ?: false
                PropertySection(
                    property = entity,
                    isExpanded = isExpanded,
                    onHeaderClick = {
                        expandedStateMap[entity.title] = !isExpanded
                    },
                    viewModel = viewModel
                )
            }
        }

//        AddButton(
//            modifier = Modifier
//                .align(Alignment.BottomEnd)
//                .padding(bottom = 45.dp, end = 10.dp),
//            navController = navController
//        )
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
        onHeaderClicked = {
            onHeaderClick()
            if(!isExpanded) {
                viewModel.getEntityById(property.id)
            }
        }

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