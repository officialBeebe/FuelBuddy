package com.dylanbeebe.fuelbuddy.ui.core.vehicle.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dylanbeebe.fuelbuddy.FuelBuddyApplication
import com.dylanbeebe.fuelbuddy.domain.model.vehicle.VehicleInput
import com.dylanbeebe.fuelbuddy.domain.usecase.vehicle.AddVehicle
import kotlinx.coroutines.launch

class VehicleAddViewModel(
    private val addVehicleUseCase: AddVehicle
) : ViewModel() {
    fun add(input: VehicleInput) {
        viewModelScope.launch {
            addVehicleUseCase(input)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FuelBuddyApplication)
                VehicleAddViewModel(
                    addVehicleUseCase = AddVehicle(app.vehicleRepositoryRoomImpl)
                )
            }
        }
    }
}