package com.dylanbeebe.fuelbuddy.ui.component

import android.content.Context
import android.net.Uri
import java.io.File
import java.util.UUID

fun copyUriToAppStorage(context: Context, sourceUri: Uri, subdir: String): File {
    val dir = File(context.filesDir, subdir).apply { mkdirs() }
    val destFile = File(dir, "${UUID.randomUUID()}.jpg")
    context.contentResolver.openInputStream(sourceUri)?.use { input ->
        destFile.outputStream().use { output -> input.copyTo(output) }
    }
    return destFile
}