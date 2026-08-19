package com.dylanbeebe.fuelbuddy.ui.component

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.dylanbeebe.fuelbuddy.util.copyUriToAppStorage
import java.io.File
import java.util.UUID

@Composable
fun rememberCameraLauncher(
    subdir: String,
    onImageCaptured: (File) -> Unit,
): () -> Unit {
    val context = LocalContext.current
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUri?.let { uri ->
                onImageCaptured(copyUriToAppStorage(context, uri, subdir))
            }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val file = File(context.filesDir, subdir).apply { mkdirs() }
                .let { File(it, "${UUID.randomUUID()}.jpg") }
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            photoUri = uri
            launcher.launch(uri)
        }
    }

    return {
        val file = File(context.filesDir, subdir).apply { mkdirs() }
            .let { File(it, "${UUID.randomUUID()}.jpg") }
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        photoUri = uri

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            launcher.launch(uri)
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}