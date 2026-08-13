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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class MileageDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val mileageRepository: MileageRepository
) : ViewModel() {
    private val _mileageID: String = checkNotNull(savedStateHandle["mileageID"])
    val uiState: StateFlow<MileageDetailUiState> =
        mileageRepository.observeMileageWithAttachments(_mileageID)
            .map { MileageDetailUiState(
                    mileage = it?.mileage,
                    mileageAttachments = (it?.attachments ?: emptyList())
                )
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MileageDetailUiState())

    fun updateMileage(mileage: Mileage) {
        viewModelScope.launch { mileageRepository.update(mileage) }
    }

    fun deleteMileage(mileage: Mileage) {
        viewModelScope.launch { mileageRepository.delete(mileage) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val app = (this[APPLICATION_KEY] as FuelBuddyApplication)
                MileageDetailViewModel(savedStateHandle, app.mileageRepository)
            }
        }
    }
}

data class MileageDetailUiState(
    val mileage: Mileage? = null,
    val mileageAttachments: List<MileageAttachment> = emptyList()
)