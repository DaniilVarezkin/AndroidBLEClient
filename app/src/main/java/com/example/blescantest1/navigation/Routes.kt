package com.example.blescantest1.navigation

sealed class Routes(val route: String) {
    object Permissions : Routes("permissions")

    object Start : Routes("start")
    object Scan : Routes("scan")

    object ConnectDevice : Routes("connect/{address}") {
        fun createRoute(address: String) = "connect/$address"
    }

    object Device : Routes("device/{address}") {
        fun createRoute(address: String) = "device/$address"
    }



    object DeviceList : Routes("device-list")
    object ConnectedDevice : Routes("device/{address}") {
        fun createRoute(address: String) = "device/$address"
    }
}