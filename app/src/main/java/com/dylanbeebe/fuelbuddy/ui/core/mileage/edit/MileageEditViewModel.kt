package com.dylanbeebe.fuelbuddy.ui.core.mileage.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dylanbeebe.fuelbuddy.FuelBuddyApplication
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageAttachment
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageAttachmentInput
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageInput
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageWithAttachments
import com.dylanbeebe.fuelbuddy.domain.usecase.mileage.DeleteMileage
import com.dylanbeebe.fuelbuddy.domain.usecase.mileage.ObserveMileageWithAttachments
import com.dylanbeebe.fuelbuddy.domain.usecase.mileage.UpdateMileageWithAttachments
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MileageEditViewModel(
    savedStateHandle: SavedStateHandle,
    observeMileageWithAttachments: ObserveMileageWithAttachments,
    private val updateMileageWithAttachments: UpdateMileageWithAttachments,
    private val deleteMileage: DeleteMileage,
) : ViewModel() {
    val mileageID: String =
        requireNotNull(savedStateHandle.get<String>("mileageID"))

    val mileageWithAttachments: StateFlow<MileageWithAttachments?> =
        observeMileageWithAttachments(mileageID)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    fun updateMileage(
        input: MileageInput,
        newAttachments: List<MileageAttachmentInput>,
        removedAttachments: List<MileageAttachment>,
    ) {
        viewModelScope.launch {
            updateMileageWithAttachments(
                mileageID = mileageID,
                input = input,
                newAttachments = newAttachments,
                removedAttachments = removedAttachments,
            )
        }
    }

    fun delete() {
        viewModelScope.launch {
            deleteMileage(mileageID)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FuelBuddyApplication)
                MileageEditViewModel(
                    savedStateHandle,
                    observeMileageWithAttachments = ObserveMileageWithAttachments(app.mileageRepositoryRoomImpl),
                    updateMileageWithAttachments = UpdateMileageWithAttachments(app.mileageRepositoryRoomImpl),
                    deleteMileage = DeleteMileage(app.mileageRepositoryRoomImpl),
                )
            }
        }
    }
}