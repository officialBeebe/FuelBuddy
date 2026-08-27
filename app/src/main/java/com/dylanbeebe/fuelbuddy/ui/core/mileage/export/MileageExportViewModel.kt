package com.dylanbeebe.fuelbuddy.ui.core.mileage.export

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dylanbeebe.fuelbuddy.FuelBuddyApplication
import com.dylanbeebe.fuelbuddy.domain.model.mileage.ExportMileageInput
import com.dylanbeebe.fuelbuddy.domain.model.mileage.Mileage
import com.dylanbeebe.fuelbuddy.domain.usecase.mileage.DownloadMileage
import com.dylanbeebe.fuelbuddy.domain.usecase.mileage.ExportMileage
import com.dylanbeebe.fuelbuddy.domain.usecase.mileage.ObserveAllMileageForVehicle
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**VehicleMileageViewModel
 *
 * State holder for a vehicle's mileage log, including on-demand
 * attachment loading for whichever entry is currently expanded.
 * */
enum class ExportRangeMode { LATEST, CUSTOM }
enum class ExportFormat { CSV, JSON }

class MileageExportViewModel(
    savedStateHandle: SavedStateHandle,
    private val appContext: Context,
    private val observeAllMileageForVehicle: ObserveAllMileageForVehicle,
    private val downloadMileage: DownloadMileage,
    private val exportMileage: ExportMileage
) : ViewModel() {
    private val vehicleID: String = checkNotNull(savedStateHandle["vehicleID"])

    val uiState: StateFlow<VehicleMileageUiState> =
        observeAllMileageForVehicle(vehicleID)
            .map { mileage ->
                VehicleMileageUiState(
                    vehicleID = vehicleID,
                    mileage = mileage
                )
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),
                VehicleMileageUiState()
            )

    fun downloadMileage(
        vehicleID: String,
        input: ExportMileageInput,
        context: Context,
        onReady: (Intent) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val uri: Uri = downloadMileage.invoke(vehicleID, input, context)
                Toast.makeText(context, "Saved to Downloads", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, e.message ?: "Export failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun exportMileage(
        vehicleID: String,
        input: ExportMileageInput,
        context: Context,
        onReady: (Intent) -> Unit
    ) {
        viewModelScope.launch {
            val intent = exportMileage.invoke(vehicleID, input, context)
            onReady(intent) // file sharing intent
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FuelBuddyApplication)
                MileageExportViewModel(
                    savedStateHandle,
                    app,
                    observeAllMileageForVehicle = ObserveAllMileageForVehicle(
                        app.mileageRepositoryRoomImpl
                    ),
                    downloadMileage = DownloadMileage(
                        app.mileageRepositoryRoomImpl,
                        app.vehicleRepositoryRoomImpl
                    ),
                    exportMileage = ExportMileage(
                        app.mileageRepositoryRoomImpl,
                        app.vehicleRepositoryRoomImpl
                    )
                )
            }
        }
    }
}

data class VehicleMileageUiState(
    val vehicleID: String = "",
    val mileage: List<Mileage> = emptyList()
)