package com.dylanbeebe.fuelbuddy.ui.core.mileage.form

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dylanbeebe.fuelbuddy.ui.component.ActionFAB

@Composable
fun MileageFormActions(
    canDelete: Boolean,
    onDelete: (() -> Unit)? = null,
    onSave: () -> Unit,
) {
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    ) {
        if (canDelete) {
            // Delete FAB
            ActionFAB(
                icon = Icons.Default.Delete,
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError,
                contentDescription = "Add vehicle",
                onClick = { showDeleteConfirmation = true },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            )
//            FloatingActionButton(
//                onClick = { showDeleteConfirmation = true },
//                modifier = Modifier
//                    .size(64.dp)
//                    .align(Alignment.CenterStart),
//                shape = RoundedCornerShape(16.dp),
//                containerColor = MaterialTheme.colorScheme.error,
//                contentColor = MaterialTheme.colorScheme.onError,
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Delete,
//                    contentDescription = "Delete mileage",
//                    modifier = Modifier.size(32.dp)
//                )
//            }
        }

        // Save FAB
        ActionFAB(
            icon = Icons.Default.Save,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            contentDescription = "Add vehicle",
            onClick = onSave,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
//        FloatingActionButton(
//            onClick = onSave,
//            modifier = Modifier
//                .size(64.dp)
//                .align(Alignment.CenterEnd),
//            shape = RoundedCornerShape(16.dp),
//            containerColor = MaterialTheme.colorScheme.primary,
//            contentColor = MaterialTheme.colorScheme.onPrimary,
//        ) {
//            Icon(
//                imageVector = Icons.Default.Save,
//                contentDescription = "Save mileage",
//                modifier = Modifier.size(32.dp)
//            )
//        }
    }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Delete mileage entry?") },
            text = { Text("This can't be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete?.invoke()
                    showDeleteConfirmation = false
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text("Cancel")
                }
            })
    }
}