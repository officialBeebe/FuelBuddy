package com.dylanbeebe.fuelbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dylanbeebe.fuelbuddy.FuelBuddyApplication
import com.dylanbeebe.fuelbuddy.data.room.entity.Vehicle
import com.dylanbeebe.fuelbuddy.data.room.repository.VehicleRepositoryImpl
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

/**VehicleListViewModel
 *
 * State holder for the current list of vehicles.
 * */
class VehicleListViewModel(
    private val vehicleRepositoryImpl: VehicleRepositoryImpl
) : ViewModel() {
    val uiState: StateFlow<List<Vehicle>> =
        vehicleRepositoryImpl.observeAllVehicles()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as FuelBuddyApplication)
                VehicleListViewModel(app.vehicleRepositoryImpl)
            }
        }
    }
}