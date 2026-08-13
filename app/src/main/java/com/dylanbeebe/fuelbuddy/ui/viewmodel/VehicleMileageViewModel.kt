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

    private val _expandedMileageID = MutableStateFlow<String?>(null)

    // user taps mileage card to expand mileage details and fetch attachments
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _expandedMileageAttachments: StateFlow<List<MileageAttachment>> =
        _expandedMileageID
            .flatMapLatest { id ->
                if (id == null) flowOf(emptyList())
                else mileageRepository.observeMileageWithAttachments(id)
                    .map { it?.attachments ?: emptyList() }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val uiState: StateFlow<VehicleMileageUiState> =
        combine(
            mileageRepository.observeAllForVehicle(_vehicleID),
            _expandedMileageID,
            _expandedMileageAttachments
        ) { mileage, expandedMileageID, expandedMileageAttachments ->
            VehicleMileageUiState(
                mileage = mileage,
                expandedMileageID = expandedMileageID,
                expandedMileageAttachments = expandedMileageAttachments
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), VehicleMileageUiState())

    fun toggleExpanded(mileageID: String) {
        _expandedMileageID.value = if (_expandedMileageID.value == mileageID) null else mileageID
    }

    fun addMileage(mileage: Mileage) {
        viewModelScope.launch { mileageRepository.insert(mileage) }
    }

    fun deleteMileage(mileage: Mileage) {
        viewModelScope.launch { mileageRepository.delete(mileage) }
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
    val mileage: List<Mileage> = emptyList(),
    val expandedMileageID: String? = null,
    val expandedMileageAttachments: List<MileageAttachment> = emptyList()
)