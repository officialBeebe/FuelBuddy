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
import com.dylanbeebe.fuelbuddy.data.room.relation.VehicleWithAttachments
import com.dylanbeebe.fuelbuddy.domain.repository.MileageRepository
import com.dylanbeebe.fuelbuddy.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class VehicleListUiState(
    val allVehicles: List<Vehicle> = emptyList(),
)



class VehicleViewModel(
    private val vehicleRepository: VehicleRepository,
    private val mileageRepository: MileageRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // TODO: ViewModel logic for all vehicle and mileage operations

    private val _uiState = MutableStateFlow(VehicleListUiState())
    private val _currentVehicle = MutableStateFlow<VehicleWithAttachments?>(null)

    val uiState: StateFlow<VehicleListUiState> = _uiState.asStateFlow()
    val currentVehicle: StateFlow<VehicleWithAttachments?> = _currentVehicle.asStateFlow()

    init { refreshVehicles() }

    fun addVehicle(vehicle: Vehicle) {
        viewModelScope.launch {
            vehicleRepository.insert(vehicle)
            refreshVehicles()
        }
    }

    private fun refreshVehicles() {
        viewModelScope.launch {
            _uiState.update { it.copy(allVehicles = vehicleRepository.allVehicles()) }
        }
    }


    fun loadVehicle(vehicleId: String) {
        viewModelScope.launch {
            val vehicle = vehicleRepository.getVehicleWithAttachments(vehicleId)
            _currentVehicle.value = vehicle
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val vehicleRepository = (this[APPLICATION_KEY] as FuelBuddyApplication).vehicleRepository
                val mileageRepository = (this[APPLICATION_KEY] as FuelBuddyApplication).mileageRepository
                VehicleViewModel(
                    vehicleRepository = vehicleRepository,
                    mileageRepository = mileageRepository,
                    savedStateHandle = savedStateHandle
                )

            }
        }
    }
}