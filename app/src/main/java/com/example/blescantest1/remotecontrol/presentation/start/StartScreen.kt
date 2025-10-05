package com.example.blescantest1.remotecontrol.presentation.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.blescantest1.navigation.Routes


@Composable
fun StartScreen(
    navController: NavController,
    viewModel: StartScreenViewModel = hiltViewModel()
) {
    StartScreenContent(
        onClickAddDevice = {
            navController.navigate(Routes.Scan.route)
        }
    )
}

@Preview(showBackground = true)
@Composable
fun StartScreenPreview() {
    StartScreenContent(
        onClickAddDevice = {}
    )
}

@Composable
fun StartScreenContent(
    onClickAddDevice: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onClickAddDevice
        ) {
            Text("Добавить устройство")
        }
    }
}

