package com.andreeailie.reexam.feature.presentation.second_section

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.andreeailie.reexam.R
import com.andreeailie.reexam.feature.data.network.NetworkStatusTracker
import com.andreeailie.reexam.feature.domain.model.Entity
import com.andreeailie.reexam.feature.domain.model.Property
import com.andreeailie.reexam.feature.presentation.SetTitle
import com.andreeailie.reexam.feature.presentation.components.AddButton
import com.andreeailie.reexam.feature.presentation.components.EntityCell
import com.andreeailie.reexam.feature.presentation.components.EntityCellSimple
import com.andreeailie.reexam.feature.presentation.entities.EntitiesEvent
import com.andreeailie.reexam.feature.presentation.entities.EntitiesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SecondScreen(
    modifier: Modifier = Modifier,
    viewModel: EntitiesViewModel = hiltViewModel(),
    navController: NavController,
    networkStatusTracker: NetworkStatusTracker
) {

    val networkStatus by networkStatusTracker.networkStatus.collectAsState(initial = NetworkStatusTracker.NetworkStatus.Unavailable)

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = viewModel.snackbarHostState) }
    ) {
        Column{
            SetTitle()
            SetScreenTitle(modifier = modifier)

            when (networkStatus) {
                NetworkStatusTracker.NetworkStatus.Available -> {
                    PropertyList(viewModel = viewModel, navController = navController)
                }
                NetworkStatusTracker.NetworkStatus.Unavailable -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(70.dp)
                        ,
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "This screen is unavailable offline. Please check your internet connection.",
                            textAlign = TextAlign.Center,
                            color = colorResource(R.color.grey),
                            style = MaterialTheme.typography.headlineMedium,
                        )
                    }
                }
            }
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
                text = "Manage",
                textAlign = TextAlign.Left,
                color = colorResource(R.color.black),
                style = MaterialTheme.typography.headlineLarge,
            )
        }
    }
}

@Composable
fun SectionHeader(property: Property, isExpanded: Boolean, onHeaderClicked: () -> Unit) {
    Row(modifier = Modifier
        .padding(vertical = 2.dp, horizontal = 5.dp)
        .clickable { onHeaderClicked() }
        .fillMaxWidth()
        .background(
            color = colorResource(id = R.color.not_really_white),
            shape = AbsoluteRoundedCornerShape(
                topLeft = 10.dp,
                topRight = 10.dp,
                bottomLeft = 10.dp,
                bottomRight = 10.dp
            )
        )
        .height(100.dp)
        ) {
        Row (
            modifier = Modifier.padding(top = 35.dp, start = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Type: " + property.name,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .weight(0.2f)
            )
            Text(text = if (isExpanded) "▼" else "▶", fontSize = 25.sp)
        }
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
        LazyColumn {
            items(state.properties) { property ->
                val isExpanded = expandedStateMap[property.name] ?: false
                PropertySection(
                    property = property,
                    isExpanded = isExpanded,
                    onHeaderClick = {
                        expandedStateMap[property.name] = !isExpanded
                    },
                    viewModel = viewModel
                )
            }
        }
    }
}


@Composable
fun PropertySection(
    property: Property,
    isExpanded: Boolean,
    onHeaderClick: () -> Unit,
    viewModel: EntitiesViewModel
) {
    SectionHeader(
        property = property,
        isExpanded = isExpanded,
        onHeaderClicked = {
            onHeaderClick()
            if (!isExpanded) {
                viewModel.fetchEntitiesForSelectedProperty(property)
            }

        }
    )

    val state = viewModel.state.value

    Log.d("MainScreen", "isExpanded: $isExpanded")
    if (isExpanded) {
        val entitiesForProperty = state.entitiesForProperties[property]
        Log.i("MainScreen", "property: $property, entities: $entitiesForProperty")
        if (entitiesForProperty!= null) {
            LazyRow {
                items(entitiesForProperty) { entity ->
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