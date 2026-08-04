package com.dylanbeebe.fuelbuddy.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dylanbeebe.fuelbuddy.data.model.Vehicle
import com.dylanbeebe.fuelbuddy.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ViewModel
data class VehicleUIState(
    val allVehicles: List<Vehicle> = emptyList(),
    val currentVehicle: Vehicle? = null,
)

class VehicleViewModel(
    private val vehicleRepository: VehicleRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(VehicleUIState())
    val uiState: StateFlow<VehicleUIState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun selectVehicle(vehicleId: String) {
        val vehicle = _uiState.value.allVehicles.find { it.vehicleID == vehicleId }
            ?: throw IllegalStateException("Vehicle not found: $vehicleId")
        _uiState.update { it.copy(currentVehicle = vehicle) }
    }

    fun addVehicle(vehicle: Vehicle) {
        viewModelScope.launch {
            vehicleRepository.insert(vehicle)
            refresh()
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(allVehicles = vehicleRepository.allVehicles()) }
        }
    }
}