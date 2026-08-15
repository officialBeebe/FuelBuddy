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
import com.dylanbeebe.fuelbuddy.data.model.Mileage
import com.dylanbeebe.fuelbuddy.data.room.entity.MileageAttachment
import com.dylanbeebe.fuelbuddy.domain.repository.MileageRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**VehicleMileageViewModel
 *
 * State holder for a vehicle's mileage log, including on-demand
 * attachment loading for whichever entry is currently expanded.
 * */
class VehicleMileageViewModel(
    savedStateHandle: SavedStateHandle,
    private val mileageRepository: MileageRepository
) : ViewModel() {
    private val _vehicleID: String = checkNotNull(savedStateHandle["vehicleID"])

    val uiState: StateFlow<VehicleMileageUiState> =
        mileageRepository.observeAllForVehicle(_vehicleID)
            .map { mileage -> VehicleMileageUiState(mileage = mileage) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), VehicleMileageUiState())

    fun addMileage(mileage: Mileage) {
        viewModelScope.launch { mileageRepository.insert(mileage) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val app = (this[APPLICATION_KEY] as FuelBuddyApplication)
                VehicleMileageViewModel(savedStateHandle, app.mileageRepository)
            }
        }
    }
}

data class VehicleMileageUiState(
    val mileage: List<Mileage> = emptyList()
)