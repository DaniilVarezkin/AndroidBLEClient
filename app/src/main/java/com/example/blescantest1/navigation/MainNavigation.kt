package com.example.blescantest1.navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.blescantest1.remotecontrol.presentation.connect.ConnectScreen
import com.example.blescantest1.remotecontrol.presentation.device.DeviceScreen
import com.example.blescantest1.remotecontrol.presentation.util.permissions.PermissionManager
import com.example.blescantest1.remotecontrol.presentation.device_list.DeviceListScreen
import com.example.blescantest1.remotecontrol.presentation.permissions.PermissionsScreen
import com.example.blescantest1.remotecontrol.presentation.scanning.ScanningScreen
import com.example.blescantest1.remotecontrol.presentation.start.StartScreen

@SuppressLint("MissingPermission")
@Composable
fun MainNavigation() {

    val navController = rememberNavController()

    val context = LocalContext.current
    var allPermissionsGranted by remember {
        mutableStateOf(PermissionManager.haveAllPermissions(context))
    }

    Log.d("MainNavigation", "Разрешения выданы: $allPermissionsGranted")

    NavHost(
        navController = navController,
        startDestination =
            if (allPermissionsGranted) Routes.Start.route //Потом логика усложнится
            else Routes.Permissions.route
    ) {
        composable(Routes.Permissions.route) {
            PermissionsScreen {
                //При выдаче разрешений переходим на экран списка устройств
                navController.navigate(Routes.Start.route)
            }
        }

        composable(Routes.Start.route) {
            StartScreen(navController)
        }

        composable(Routes.Scan.route) {
            ScanningScreen(navController)
        }

        composable(
            Routes.ConnectDevice.route,
            arguments = listOf(navArgument("address") { type = NavType.StringType })
        ) {
            ConnectScreen(navController)
        }

        composable(
            Routes.Device.route,
            arguments = listOf(navArgument("address") { type = NavType.StringType })
        ) {
            DeviceScreen()
        }

        composable(Routes.DeviceList.route) { //ToDo убрать
            DeviceListScreen(navController)
        }
    }
}