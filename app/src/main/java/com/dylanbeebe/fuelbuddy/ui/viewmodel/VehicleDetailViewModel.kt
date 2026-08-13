package com.dylanbeebe.fuelbuddy.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dylanbeebe.fuelbuddy.FuelBuddyApplication
import com.dylanbeebe.fuelbuddy.data.model.Vehicle
import com.dylanbeebe.fuelbuddy.data.room.entity.VehicleAttachment
import com.dylanbeebe.fuelbuddy.domain.repository.MileageRepository
import com.dylanbeebe.fuelbuddy.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**VehicleDetailViewModel
 *
 * State hodler for the selected vehicle and its attachments.
 * */
class VehicleDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val vehicleRepository: VehicleRepository,
) : ViewModel() {
    private val _vehicleID: String = checkNotNull(savedStateHandle["vehicleID"])

    val uiState: StateFlow<VehicleDetailUiState> =
        vehicleRepository.observeVehicleWithAttachments(_vehicleID)
            .map {
                VehicleDetailUiState(
                    vehicle = it?.vehicle,
                    vehicleAttachments = (it?.attachments ?: emptyList())
                )
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), VehicleDetailUiState())

    fun updateVehicle(vehicle: Vehicle) {
        viewModelScope.launch { vehicleRepository.update(vehicle) }
    }

    fun deleteVehicle() {
        viewModelScope.launch {
            uiState.value.vehicle?.let { vehicleRepository.delete(it) }
        }
    }

    fun addAttachment(uri: String) {
        viewModelScope.launch {
            vehicleRepository.addAttachment(VehicleAttachment(URI = uri, vehicle = _vehicleID))
        }
    }

    fun removeAttachment(attachment: VehicleAttachment) {
        viewModelScope.launch { vehicleRepository.removeAttachment(attachment) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val app = (this[APPLICATION_KEY] as FuelBuddyApplication)
                VehicleDetailViewModel(savedStateHandle, app.vehicleRepository)
            }
        }
    }
}

data class VehicleDetailUiState(
    val vehicle: Vehicle? = null,
    val vehicleAttachments: List<VehicleAttachment> = emptyList()
)