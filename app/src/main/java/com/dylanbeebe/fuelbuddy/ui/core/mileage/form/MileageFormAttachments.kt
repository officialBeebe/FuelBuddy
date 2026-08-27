package com.dylanbeebe.fuelbuddy.ui.core.mileage.form

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageAttachment
import com.dylanbeebe.fuelbuddy.ui.component.rememberCameraLauncher
import com.dylanbeebe.fuelbuddy.ui.component.rememberMultiPhotoPickerLauncher
import java.io.File

/** Unified view of an attachment for display, whether already persisted or staged pending save. */
private sealed interface AttachmentDisplayItem {
    val filePath: String

    data class Existing(val attachment: MileageAttachment) : AttachmentDisplayItem {
        override val filePath: String get() = attachment.filepath
    }

    data class Staged(val file: File) : AttachmentDisplayItem {
        override val filePath: String get() = file.absolutePath
    }
}

@Composable
fun MileageFormAttachments(
    existingAttachments: List<MileageAttachment>,
    stagedAttachments: List<File>,
    onStageAttachments: (List<File>) -> Unit,
    onRemoveExisting: (MileageAttachment) -> Unit,
    onRemoveStaged: (File) -> Unit,
) {
    var viewingAttachmentPath by remember { mutableStateOf<String?>(null) }

    val displayItems: List<AttachmentDisplayItem> =
        existingAttachments.map { AttachmentDisplayItem.Existing(it) } +
                stagedAttachments.map { AttachmentDisplayItem.Staged(it) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                8.dp,
                Alignment.CenterHorizontally
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            displayItems.forEach { displayItem ->
                Box(modifier = Modifier.size(80.dp)) {
                    SubcomposeAsyncImage(
                        model = File(displayItem.filePath),
                        contentDescription = "Attachment",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { viewingAttachmentPath = displayItem.filePath },
                        loading = {
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Filled.Image,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(60.dp)
                                )
                            }
                        },
                        error = {
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.errorContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Filled.BrokenImage,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onErrorContainer,
                                    modifier = Modifier.size(60.dp)
                                )
                            }
                        }
                    )

                    // Remove button
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.error)
                            .clickable(onClickLabel = "Remove attachment") {
                                when (displayItem) {
                                    is AttachmentDisplayItem.Existing ->
                                        onRemoveExisting(displayItem.attachment)

                                    is AttachmentDisplayItem.Staged ->
                                        onRemoveStaged(displayItem.file)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onError,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            // Take photo
            // ASSUMPTION: rememberCameraLauncher(subdir) hands back a single File.
            // Signature wasn't provided — confirm/correct if it differs.
            val takePhoto = rememberCameraLauncher(subdir = "mileage_attachments") { file ->
                onStageAttachments(listOf(file))
            }
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable(onClickLabel = "Take photo") { takePhoto() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.PhotoCamera,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(60.dp)
                )
            }

            // Photo picker
            // ASSUMPTION: rememberMultiPhotoPickerLauncher(subdir) hands back a List<File>.
            val pickPhotos =
                rememberMultiPhotoPickerLauncher(subdir = "mileage_attachments") { files ->
                    onStageAttachments(files)
                }
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable(onClickLabel = "Pick photo") { pickPhotos() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.PhotoLibrary,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(60.dp)
                )
            }
        }
    }
}