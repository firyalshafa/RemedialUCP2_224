package com.example.remedialucp2.view.uicontroller

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun KonfirmasiDialog(
    title: String,
    message: String,
    textConfirm: String = "Ya",
    textDismiss: String = "Batal",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(textConfirm)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(textDismiss)
            }
        }
    )
}
