package com.dylanbeebe.fuelbuddy.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dylanbeebe.fuelbuddy.data.room.dao.MinimalVehicle
import com.dylanbeebe.fuelbuddy.data.model.Vehicle
import com.dylanbeebe.fuelbuddy.data.room.relation.VehicleWithAttachments
import com.dylanbeebe.fuelbuddy.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ViewModel
data class VehicleUIState(
    val allVehicles: List<MinimalVehicle> = emptyList(),
    val currentVehicle: VehicleWithAttachments? = null,
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
        viewModelScope.launch {
            val vehicle = vehicleRepository.getVehicleWithAttachments(vehicleId)
            _uiState.update { it.copy(currentVehicle = vehicle) }
        }
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

class VehicleViewModelFactory(
    private val vehicleRepository: VehicleRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return VehicleViewModel(vehicleRepository) as T
    }
}