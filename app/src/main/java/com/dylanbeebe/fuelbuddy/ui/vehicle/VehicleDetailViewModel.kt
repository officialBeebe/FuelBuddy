package com.dylanbeebe.fuelbuddy.ui.vehicle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dylanbeebe.fuelbuddy.FuelBuddyApplication
import com.dylanbeebe.fuelbuddy.data.room.entity.Vehicle
import com.dylanbeebe.fuelbuddy.data.room.entity.VehicleAttachment
import com.dylanbeebe.fuelbuddy.data.room.repository.VehicleRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**VehicleDetailViewModel
 *
 * State hodler for the selected vehicle and its attachments.
 * */
class VehicleDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val vehicleRepositoryImpl: VehicleRepositoryImpl,
) : ViewModel() {
    private val _vehicleID: String? = savedStateHandle["vehicleID"]

    val uiState: StateFlow<VehicleDetailUiState> =
        if (_vehicleID == null) {
            MutableStateFlow(VehicleDetailUiState()).asStateFlow()
        } else {
            vehicleRepositoryImpl.observeVehicleWithAttachments(_vehicleID)
                .map {
                    VehicleDetailUiState(
                        vehicle = it?.vehicle,
                    )
                }
                .stateIn(
                    viewModelScope,
                    SharingStarted.WhileSubscribed(5000),
                    VehicleDetailUiState()
                )
        }

    fun addVehicle(vehicle: Vehicle, attachments: List<VehicleAttachment> = emptyList()) {
        viewModelScope.launch {
            vehicleRepositoryImpl.insert(vehicle)
            attachments.forEach { vehicleRepositoryImpl.addAttachment(it) }
        }
    }

    fun updateVehicle(vehicle: Vehicle) {
        viewModelScope.launch { vehicleRepositoryImpl.update(vehicle) }
    }

    fun deleteVehicle() {
        viewModelScope.launch {
            uiState.value.vehicle?.let { vehicleRepositoryImpl.delete(it) }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FuelBuddyApplication)
                VehicleDetailViewModel(savedStateHandle, app.vehicleRepositoryImpl)
            }
        }
    }
}

data class VehicleDetailUiState(
    val vehicle: Vehicle? = null,
)