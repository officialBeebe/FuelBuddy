package com.dylanbeebe.fuelbuddy.ui.mileage.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dylanbeebe.fuelbuddy.FuelBuddyApplication
import com.dylanbeebe.fuelbuddy.data.room.entity.Mileage
import com.dylanbeebe.fuelbuddy.data.room.entity.MileageAttachment
import com.dylanbeebe.fuelbuddy.data.room.repository.MileageRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MileageDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val mileageRepositoryImpl: MileageRepositoryImpl
) : ViewModel() {
    private val _mileageID: String? = savedStateHandle["mileageID"]
    private val _vehicleID: String? = savedStateHandle["vehicleID"]

    val uiState: StateFlow<MileageDetailUiState> =
        if (_mileageID == null) {
            MutableStateFlow(MileageDetailUiState(vehicleID = _vehicleID)).asStateFlow()
        } else {
            mileageRepositoryImpl.observeMileageWithAttachments(_mileageID)
                .map {
                    MileageDetailUiState(
                        mileage = it?.mileage,
                        mileageAttachments = (it?.attachments ?: emptyList()),
                        vehicleID = it?.mileage?.vehicle ?: _vehicleID,
                    )
                }
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MileageDetailUiState(vehicleID = _vehicleID))
        }

    fun addMileage(mileage: Mileage, attachments: List<MileageAttachment> = emptyList()) {
        viewModelScope.launch {
            mileageRepositoryImpl.insert(mileage)
            attachments.forEach { mileageRepositoryImpl.addAttachment(it) }
        }
    }

    fun updateMileage(mileage: Mileage) {
        viewModelScope.launch { mileageRepositoryImpl.update(mileage) }
    }

    fun deleteMileage() {
        viewModelScope.launch {
            uiState.value.mileage?.let { mileageRepositoryImpl.delete(it) }
        }
    }

    fun addAttachment(attachment: MileageAttachment) {
        viewModelScope.launch { mileageRepositoryImpl.addAttachment(attachment) }
    }

    fun removeAttachment(attachment: MileageAttachment) {
        viewModelScope.launch { mileageRepositoryImpl.deleteAttachment(attachment) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FuelBuddyApplication)
                MileageDetailViewModel(savedStateHandle, app.mileageRepositoryImpl)
            }
        }
    }
}

data class MileageDetailUiState(
    val mileage: Mileage? = null,
    val mileageAttachments: List<MileageAttachment> = emptyList(),
    val vehicleID: String? = null,
)