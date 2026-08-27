package com.dylanbeebe.fuelbuddy.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.dylanbeebe.fuelbuddy.data.room.entity.VehicleEntity
import com.dylanbeebe.fuelbuddy.data.room.relation.VehicleEntityWithAttachments
import kotlinx.coroutines.flow.Flow


@Dao
interface VehicleDAO : BaseDao<VehicleEntity> {
    @Query("SELECT * FROM vehicle WHERE vehicleID = :vehicleID")
    suspend fun getVehicle(vehicleID: String): VehicleEntity

    @Query("SELECT * FROM vehicle")
    fun observeAllVehicles(): Flow<List<VehicleEntity>>

    @Query("SELECT * FROM vehicle WHERE vehicleID = :vehicleID")
    fun observeVehicle(vehicleID: String): Flow<VehicleEntity>

    @Transaction
    @Query("SELECT * FROM vehicle WHERE vehicleID = :vehicleID")
    fun observeVehicleWithAttachments(vehicleID: String): Flow<VehicleEntityWithAttachments?>
}