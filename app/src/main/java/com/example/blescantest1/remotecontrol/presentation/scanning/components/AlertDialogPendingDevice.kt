package com.example.blescantest1.remotecontrol.presentation.scanning.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun AlertDialogPendingDevice(
    title: String,
    text: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        title = {
            Text(title)
        },
        text = {
            Text(text)
        },
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Продолжить поиск")
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Подключить")
            }
        },

    )
}