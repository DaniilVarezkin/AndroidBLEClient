package com.example.blescantest1.remotecontrol.presentation.connect

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun ConnectScreen(
    navController: NavController,
    viewModel: ConnectScreenViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.navigateTo.collect {
            if(it != null) navController.navigate(it)
        }
    }

    ConnectScreenContent()
}

@Composable
fun ConnectScreenContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}


@Preview(showBackground = true)
@Composable
fun ConnectScreenPreview() {
    ConnectScreenContent()
}