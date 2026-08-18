package com.dylanbeebe.fuelbuddy.ui.viewmodel

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
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
import com.dylanbeebe.fuelbuddy.data.model.Vehicle
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
import java.io.File
import java.time.LocalDate

/**VehicleMileageViewModel
 *
 * State holder for a vehicle's mileage log, including on-demand
 * attachment loading for whichever entry is currently expanded.
 * */
enum class ExportRangeMode { LATEST, CUSTOM }
enum class ExportFormat { CSV, JSON }

class VehicleMileageViewModel(
    savedStateHandle: SavedStateHandle,
    private val mileageRepository: MileageRepository,
    private val appContext: Context,
) : ViewModel() {
    private val _vehicleID: String = checkNotNull(savedStateHandle["vehicleID"])

    val uiState: StateFlow<VehicleMileageUiState> =
        mileageRepository.observeAllForVehicle(_vehicleID)
            .map { mileage -> VehicleMileageUiState(mileage = mileage) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), VehicleMileageUiState())

    fun exportMileage(
        vehicle: Vehicle,
        rangeMode: ExportRangeMode,
        startDate: LocalDate?,
        endDate: LocalDate?,
        format: ExportFormat,
        onReady: (Intent) -> Unit,
    ) {
        viewModelScope.launch {
            val toExport: List<Mileage> = when (rangeMode) {
                ExportRangeMode.LATEST -> mileageRepository.getUnexportedForVehicle(_vehicleID)
                ExportRangeMode.CUSTOM -> {
                    if (startDate == null || endDate == null) return@launch
                    mileageRepository.getForVehicleInRange(
                        _vehicleID,
                        startDate.atStartOfDay().toString(),
                        endDate.atTime(23, 59, 59).toString(),
                    )
                }
            }
            if (toExport.isEmpty()) return@launch

            val content = when (format) {
                ExportFormat.CSV -> mileageRepository.buildExportCsv(toExport)
                ExportFormat.JSON -> mileageRepository.buildExportJson(toExport)
            }
            val extension = if (format == ExportFormat.CSV) "csv" else "json"
            val mimeType = if (format == ExportFormat.CSV) "text/csv" else "application/json"

            val exportDir = File(appContext.cacheDir, "exports").apply { mkdirs() }
            val safeName = vehicle.nickname.trim().replace(Regex("\\s+"), "-")
            val file = File(exportDir, "${safeName}_${LocalDate.now()}.$extension")
            file.writeText(content)

            val uri = FileProvider.getUriForFile(appContext, "${appContext.packageName}.fileprovider", file)
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = mimeType
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            onReady(intent)

            toExport.forEach { m -> mileageRepository.update(m.copy(isExported = true)) }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val app = (this[APPLICATION_KEY] as FuelBuddyApplication)
                VehicleMileageViewModel(savedStateHandle, app.mileageRepository, app)
            }
        }
    }
}

data class VehicleMileageUiState(
    val mileage: List<Mileage> = emptyList()
)