package com.dylanbeebe.fuelbuddy.ui.core.mileage.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dylanbeebe.fuelbuddy.FuelBuddyApplication
import com.dylanbeebe.fuelbuddy.data.room.repository.MileageRepositoryRoomImpl
import com.dylanbeebe.fuelbuddy.domain.model.mileage.Mileage
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageAttachment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MileageDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val mileageRepositoryRoomImpl: MileageRepositoryRoomImpl
) : ViewModel() {
    private val _mileageID: String? = savedStateHandle["mileageID"]
    private val _vehicleID: String? = savedStateHandle["vehicleID"]

    val uiState: StateFlow<com.dylanbeebe.fuelbuddy.ui.core.mileage.detail.MileageDetailUiState> =
        if (_mileageID == null) {
            MutableStateFlow(
                _root_ide_package_.com.dylanbeebe.fuelbuddy.ui.core.mileage.detail.MileageDetailUiState(
                    vehicleID = _vehicleID
                )
            ).asStateFlow()
        } else {
            mileageRepositoryRoomImpl.observeMileageWithAttachments(_mileageID)
                .map {
                    _root_ide_package_.com.dylanbeebe.fuelbuddy.ui.core.mileage.detail.MileageDetailUiState(
                        mileage = it?.mileage,
                        mileageAttachments = (it?.attachments ?: emptyList()),
                        vehicleID = it?.mileage?.vehicleID ?: _vehicleID,
                    )
                }
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),
                    _root_ide_package_.com.dylanbeebe.fuelbuddy.ui.core.mileage.detail.MileageDetailUiState(
                        vehicleID = _vehicleID
                    )
                )
        }

    fun addMileage(mileage: Mileage, attachments: List<MileageAttachment> = emptyList()) {
        viewModelScope.launch {
            mileageRepositoryRoomImpl.insert(mileage)
            attachments.forEach { mileageRepositoryRoomImpl.addAttachment(it) }
        }
    }

    fun updateMileage(mileage: Mileage) {
        viewModelScope.launch { mileageRepositoryRoomImpl.update(mileage) }
    }

    fun deleteMileage() {
        viewModelScope.launch {
            uiState.value.mileage?.let { mileageRepositoryRoomImpl.delete(it) }
        }
    }

    // input?
    fun addAttachment(attachment: MileageAttachment) {
        viewModelScope.launch { mileageRepositoryRoomImpl.addAttachment(attachment) }
    }

    fun removeAttachment(attachment: MileageAttachment) {
        viewModelScope.launch { mileageRepositoryRoomImpl.deleteAttachment(attachment) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FuelBuddyApplication)
                _root_ide_package_.com.dylanbeebe.fuelbuddy.ui.core.mileage.detail.MileageDetailViewModel(
                    savedStateHandle,
                    app.mileageRepositoryRoomImpl
                )
            }
        }
    }
}

data class MileageDetailUiState(
    val mileage: Mileage? = null,
    val mileageAttachments: List<MileageAttachment> = emptyList(),
    val vehicleID: String? = null,
)