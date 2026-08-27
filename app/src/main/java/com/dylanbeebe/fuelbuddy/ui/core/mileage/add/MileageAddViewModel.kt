package com.dylanbeebe.fuelbuddy.ui.core.mileage.add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dylanbeebe.fuelbuddy.FuelBuddyApplication
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageAttachmentInput
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageInput
import com.dylanbeebe.fuelbuddy.domain.usecase.mileage.AddMileageWithAttachments
import kotlinx.coroutines.launch

class MileageAddViewModel(
    savedStateHandle: SavedStateHandle,
    private val addMileageWithAttachments: AddMileageWithAttachments,
) : ViewModel() {
    val vehicleID: String =
        requireNotNull(savedStateHandle.get<String>("vehicleID"))

    fun addMileage(
        input: MileageInput,
        attachments: List<MileageAttachmentInput>
    ) {
        viewModelScope.launch {
            addMileageWithAttachments(
                vehicleID = vehicleID,
                input = input,
                attachments = attachments
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FuelBuddyApplication)
                MileageAddViewModel(
                    savedStateHandle,
                    addMileageWithAttachments = AddMileageWithAttachments(app.mileageRepositoryRoomImpl)
                )

            }
        }
    }
}