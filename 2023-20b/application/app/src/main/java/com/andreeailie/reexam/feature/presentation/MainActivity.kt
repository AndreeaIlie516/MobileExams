package com.andreeailie.reexam.feature.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.andreeailie.reexam.R
import com.andreeailie.reexam.feature.data.network.NetworkStatusTracker
import com.andreeailie.reexam.feature.presentation.components.NetworkObserver
import com.andreeailie.reexam.feature.presentation.util.bottom_navigation.BottomNavigation
import com.andreeailie.reexam.feature.presentation.util.navigation.NavigationGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var networkStatusTracker: NetworkStatusTracker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkStatusTracker = NetworkStatusTracker(applicationContext)

        setContent {
            MainScreenView(networkStatusTracker = networkStatusTracker)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkStatusTracker.stop()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreenView(networkStatusTracker: NetworkStatusTracker) {

    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        bottomBar = { BottomNavigation(navController = navController) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        NetworkObserver(snackbarHostState = snackbarHostState, networkStatusTracker = networkStatusTracker)
        SetTitle()
        NavigationGraph(navController = navController, networkStatusTracker = networkStatusTracker)
    }
}

@Composable
fun SetTitle(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(start = 25.dp, top = 25.dp)
    ) {
        Column(
            modifier = modifier
        )
        {
            Text(
                text = "Nutrition App",
                textAlign = TextAlign.Left,
                color = colorResource(R.color.purple),
                style = MaterialTheme.typography.headlineLarge,
            )
        }
    }
}