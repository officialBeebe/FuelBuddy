package com.dylanbeebe.fuelbuddy.ui.component

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.io.File
import android.content.Context
import android.net.Uri
import java.util.UUID

fun copyUriToAppStorage(context: Context, sourceUri: Uri, subdir: String): File {
    val dir = File(context.filesDir, subdir).apply { mkdirs() }
    val destFile = File(dir, "${UUID.randomUUID()}.jpg")
    context.contentResolver.openInputStream(sourceUri)?.use { input ->
        destFile.outputStream().use { output -> input.copyTo(output) }
    }
    return destFile
}

@Composable
fun rememberPhotoPickerLauncher(
    subdir: String,
    onImagePicked: (File) -> Unit,
): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { onImagePicked(copyUriToAppStorage(context, it, subdir)) }
    }
    return {
        launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}

@Composable
fun rememberMultiPhotoPickerLauncher(
    subdir: String,
    maxItems: Int = 10,
    onImagesPicked: (List<File>) -> Unit,
): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems)
    ) { uris ->
        if (uris.isNotEmpty()) {
            onImagesPicked(uris.map { copyUriToAppStorage(context, it, subdir) })
        }
    }
    return {
        launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}