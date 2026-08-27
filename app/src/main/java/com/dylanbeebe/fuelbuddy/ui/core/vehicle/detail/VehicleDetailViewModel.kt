package com.dylanbeebe.fuelbuddy.ui.core.vehicle.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dylanbeebe.fuelbuddy.FuelBuddyApplication
import com.dylanbeebe.fuelbuddy.domain.model.mileage.Mileage
import com.dylanbeebe.fuelbuddy.domain.model.vehicle.Vehicle
import com.dylanbeebe.fuelbuddy.domain.model.vehicle.VehicleWithAttachments
import com.dylanbeebe.fuelbuddy.domain.usecase.mileage.ObserveAllMileageForVehicle
import com.dylanbeebe.fuelbuddy.domain.usecase.vehicle.ObserveVehicleWithAttachments
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

/**VehicleDetailViewModel
 *
 * State hodler for the selected vehicle and its attachments.
 * */
class VehicleDetailViewModel(
    savedStateHandle: SavedStateHandle,
    observeVehicleWithAttachments: ObserveVehicleWithAttachments,
    observeAllMileageForVehicle: ObserveAllMileageForVehicle
) : ViewModel() {
    private val vehicleID: String = requireNotNull(savedStateHandle["vehicleID"])
    val uiState: StateFlow<VehicleDetailUIState> =
        combine(
            observeVehicleWithAttachments(vehicleID),
            observeAllMileageForVehicle(vehicleID),
        ) { vehicle, mileage ->
            VehicleDetailUIState(vehicle = vehicle, mileage = mileage)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = VehicleDetailUIState(vehicle = null, mileage = emptyList())
        )


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FuelBuddyApplication)
                VehicleDetailViewModel(
                    savedStateHandle,
                    observeVehicleWithAttachments = ObserveVehicleWithAttachments(app.vehicleRepositoryRoomImpl),
                    observeAllMileageForVehicle = ObserveAllMileageForVehicle(app.mileageRepositoryRoomImpl)
                )
            }
        }
    }
}

data class VehicleDetailUIState(
    val vehicle: VehicleWithAttachments?,
    val mileage: List<Mileage> = emptyList(),
)