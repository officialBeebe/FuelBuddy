package com.dylanbeebe.fuelbuddy.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dylanbeebe.fuelbuddy.data.room.relation.VehicleWithAttachments
import com.dylanbeebe.fuelbuddy.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// --- Edit screen ---
class VehicleEditViewModel(
    private val vehicleRepository: VehicleRepository,
) : ViewModel() {

    private val _currentVehicle = MutableStateFlow<VehicleWithAttachments?>(null)
    val currentVehicle: StateFlow<VehicleWithAttachments?> = _currentVehicle.asStateFlow()

    fun loadVehicle(vehicleId: String) {
        viewModelScope.launch {
            val vehicle = vehicleRepository.getVehicleWithAttachments(vehicleId)
                ?: error("Vehicle not found for id: $vehicleId")
            _currentVehicle.value = vehicle
        }
    }
}

class VehicleEditViewModelFactory(
    private val vehicleRepository: VehicleRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        VehicleEditViewModel(vehicleRepository) as T
}