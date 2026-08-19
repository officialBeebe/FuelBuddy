package com.dylanbeebe.fuelbuddy.ui.component

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.dylanbeebe.fuelbuddy.util.copyUriToAppStorage
import java.io.File

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