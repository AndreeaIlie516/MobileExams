package com.andreeailie.exam.feature.presentation.progress_section

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.andreeailie.exam.R
import com.andreeailie.exam.feature.domain.model.Entity
import com.andreeailie.exam.feature.presentation.entities.EntitiesViewModel

@Composable
fun ProgressScreen(viewModel: EntitiesViewModel = hiltViewModel(), isConnected: Boolean) {
    if (isConnected) {
        val inProgressEvents = viewModel.inProgressEvents.value

        Log.d("ProgressScreen", "events: $inProgressEvents")

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                SetTitle()
                // Display in-progress events
                inProgressEvents.forEach { event ->
                    Log.d("ProgressScreen", "event: $event")
                    InProgressEventItem(event, viewModel)
                }
            }
        }
    }
    else {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No Internet Connection",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun InProgressEventItem(event: Entity, viewModel: EntitiesViewModel) {
    Row(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
            .background(
                color = colorResource(id = R.color.light_purple),
                shape = AbsoluteRoundedCornerShape(8.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .weight(1f)
        ) {
            Text(
                text = "Event: ${event.name}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black
            )
            Text(
                text = "Details: ${event.details}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Text(
                text = "Status: ${event.status}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
        Button(
            onClick = { viewModel.enrollInEvent(event) },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Enroll")
        }
    }
}

@Composable
fun SetTitle(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(start = 25.dp, top = 10.dp)
    ) {
        Text(
            text = "In Progress Events",
            textAlign = TextAlign.Left,
            color = colorResource(R.color.purple),
            style = MaterialTheme.typography.headlineLarge
        )
    }
}
