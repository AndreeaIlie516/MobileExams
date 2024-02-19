package com.andreeailie.reexam.feature.presentation.third_section

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andreeailie.reexam.R
import com.andreeailie.reexam.feature.data.network.NetworkStatusTracker
import com.andreeailie.reexam.feature.domain.model.Entity
import com.andreeailie.reexam.feature.presentation.SetTitle
import com.andreeailie.reexam.feature.presentation.entities.EntitiesViewModel

@Composable
fun ThirdScreen(
    viewModel: EntitiesViewModel = hiltViewModel(),
    networkStatusTracker: NetworkStatusTracker
) {
    val topEntities = viewModel.topEntities.value
    val networkStatus by networkStatusTracker.networkStatus.collectAsState(initial = NetworkStatusTracker.NetworkStatus.Unavailable)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            SetTitle()
            SetScreenTitle()

            when (networkStatus) {
                NetworkStatusTracker.NetworkStatus.Available -> {
                    Box(
                        modifier = Modifier
                            .padding(bottom = 60.dp, start = 10.dp, end = 10.dp)
                            .fillMaxSize()
                    ) {
                        LazyColumn {
                            items(topEntities) { entity ->
                                EntityItem(entity = entity)
                            }
                        }
                    }
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
fun EntityItem(entity: Entity) {
    Row(
        modifier = Modifier
            .padding(top = 1.dp, bottom = 8.dp, start = 24.dp, end = 24.dp)
            .fillMaxWidth()
            .background(
                color = colorResource(id = R.color.light_purple),
                shape = AbsoluteRoundedCornerShape(
                    topLeft = 10.dp,
                    topRight = 10.dp,
                    bottomLeft = 10.dp,
                    bottomRight = 10.dp
                )
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp, start = 10.dp)

        ) {
            Text(
                text = "Name: " + entity.title,
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = "Type: " + entity.author,
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = "Calories: " + entity.year.toString(),
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = "Date: " + entity.ISBN,
                style = MaterialTheme.typography.headlineSmall,
            )
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
                text = "Top Meals",
                textAlign = TextAlign.Left,
                color = colorResource(R.color.black),
                style = MaterialTheme.typography.headlineLarge,
            )
        }
    }
}




//@Composable
//fun ProgressScreen(viewModel: EntitiesViewModel = hiltViewModel(), isConnected: Boolean) {
//    if (isConnected) {
//        val inProgressEvents = viewModel.inProgressEvents.value
//
//        Log.d("ProgressScreen", "events: $inProgressEvents")
//
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colorScheme.background
//        ) {
//            Column {
//                SetTitle()
//                // Display in-progress events
//                inProgressEvents.forEach { event ->
//                    Log.d("ProgressScreen", "event: $event")
//                    InProgressEventItem(event, viewModel)
//                }
//            }
//        }
//    }
//    else {
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colorScheme.background
//        ) {
//            Column(
//                modifier = Modifier.padding(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                Text(
//                    text = "No Internet Connection",
//                    style = MaterialTheme.typography.headlineMedium,
//                    color = Color.Gray
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun InProgressEventItem(event: Entity, viewModel: EntitiesViewModel) {
//    Row(
//        modifier = Modifier
//            .padding(all = 8.dp)
//            .fillMaxWidth()
//            .background(
//                color = colorResource(id = R.color.light_purple),
//                shape = AbsoluteRoundedCornerShape(8.dp)
//            ),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Column(
//            modifier = Modifier
//                .padding(16.dp)
//                .weight(1f)
//        ) {
//            Text(
//                text = "Event: ${event.name}",
//                style = MaterialTheme.typography.bodyLarge,
//                color = Color.Black
//            )
//            Text(
//                text = "Details: ${event.details}",
//                style = MaterialTheme.typography.bodyMedium,
//                color = Color.Gray
//            )
//            Text(
//                text = "Status: ${event.status}",
//                style = MaterialTheme.typography.bodyMedium,
//                color = Color.Gray
//            )
//        }
//        Button(
//            onClick = { viewModel.enrollInEvent(event) },
//            modifier = Modifier.padding(16.dp)
//        ) {
//            Text(text = "Enroll")
//        }
//    }
//}
//
//@Composable
//fun SetTitle(modifier: Modifier = Modifier) {
//    Row(
//        modifier = modifier.padding(start = 25.dp, top = 10.dp)
//    ) {
//        Text(
//            text = "In Progress Events",
//            textAlign = TextAlign.Left,
//            color = colorResource(R.color.purple),
//            style = MaterialTheme.typography.headlineLarge
//        )
//    }
//}
