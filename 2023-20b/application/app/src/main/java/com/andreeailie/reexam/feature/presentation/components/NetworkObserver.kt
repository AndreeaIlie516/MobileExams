package com.andreeailie.reexam.feature.presentation.components

import android.widget.Toast
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.andreeailie.reexam.feature.data.network.NetworkStatusTracker

@Composable
fun NetworkObserver(snackbarHostState: SnackbarHostState, networkStatusTracker: NetworkStatusTracker) {
    val context = LocalContext.current
    val networkStatus by networkStatusTracker.networkStatus.collectAsState(initial = NetworkStatusTracker.NetworkStatus.Unavailable)

    LaunchedEffect(networkStatus) {
        when (networkStatus) {
            NetworkStatusTracker.NetworkStatus.Unavailable -> {
                snackbarHostState.showSnackbar(
                    message = "Network connection lost",
                    duration = SnackbarDuration.Long
                )
            }
            NetworkStatusTracker.NetworkStatus.Available -> {
                snackbarHostState.showSnackbar(
                    message = "Reconnected to the network",
                    duration = SnackbarDuration.Long
                )
            }
        }
    }
}
