package com.dylanbeebe.fuelbuddy.ui.core.vehicle.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dylanbeebe.fuelbuddy.FuelBuddyApplication
import com.dylanbeebe.fuelbuddy.data.room.entity.VehicleEntity
import com.dylanbeebe.fuelbuddy.data.room.repository.VehicleRepositoryRoomImpl
import com.dylanbeebe.fuelbuddy.domain.model.vehicle.Vehicle
import com.dylanbeebe.fuelbuddy.domain.usecase.vehicle.ObserveAllVehicles
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.collections.emptyList

/**VehicleListViewModel
 *
 * State holder for the current list of vehicles.
 * */
class VehicleListViewModel(
    observeAllVehicles: ObserveAllVehicles
) : ViewModel() {
    val uiState: StateFlow<VehicleListUIState> = observeAllVehicles().map { vehicleList ->
        VehicleListUIState(
            vehicleList = vehicleList
        )
    }.stateIn(
        scope = viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = VehicleListUIState()
    )

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FuelBuddyApplication)
                VehicleListViewModel(observeAllVehicles = ObserveAllVehicles(app.vehicleRepositoryRoomImpl))
            }
        }
    }
}

data class VehicleListUIState(
    val vehicleList: List<Vehicle> = emptyList()
)