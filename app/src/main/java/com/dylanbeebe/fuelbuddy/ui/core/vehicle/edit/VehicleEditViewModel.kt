package com.dylanbeebe.fuelbuddy.ui.core.vehicle.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dylanbeebe.fuelbuddy.FuelBuddyApplication
import com.dylanbeebe.fuelbuddy.domain.model.vehicle.VehicleInput
import com.dylanbeebe.fuelbuddy.domain.model.vehicle.VehicleWithAttachments
import com.dylanbeebe.fuelbuddy.domain.usecase.vehicle.DeleteVehicle
import com.dylanbeebe.fuelbuddy.domain.usecase.vehicle.ObserveVehicleWithAttachments
import com.dylanbeebe.fuelbuddy.domain.usecase.vehicle.UpdateVehicle
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VehicleEditViewModel(
    savedStateHandle: SavedStateHandle,
    observeVehicleWithAttachments: ObserveVehicleWithAttachments,
    private val updateVehicle: UpdateVehicle,
    private val deleteVehicle: DeleteVehicle
) : ViewModel() {
    val vehicleID: String = requireNotNull(savedStateHandle["vehicleID"])

    val vehicleWithAttachments: StateFlow<VehicleWithAttachments?> =
        observeVehicleWithAttachments(vehicleID)
            .stateIn(
                scope = viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )


    fun update(vehicleID: String, input: VehicleInput) {
        viewModelScope.launch {
            updateVehicle(vehicleID, input)
        }
    }

    fun delete() {
        viewModelScope.launch {
            deleteVehicle(vehicleID)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FuelBuddyApplication)
                VehicleEditViewModel(
                    savedStateHandle,
                    observeVehicleWithAttachments = ObserveVehicleWithAttachments(app.vehicleRepositoryRoomImpl),
                    updateVehicle = UpdateVehicle(app.vehicleRepositoryRoomImpl),
                    deleteVehicle = DeleteVehicle(app.vehicleRepositoryRoomImpl)
                )
            }
        }
    }
}