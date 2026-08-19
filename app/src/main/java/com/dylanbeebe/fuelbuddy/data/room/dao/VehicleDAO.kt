package com.dylanbeebe.fuelbuddy.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.dylanbeebe.fuelbuddy.data.room.entity.Vehicle
import com.dylanbeebe.fuelbuddy.data.room.relation.VehicleWithAttachments
import kotlinx.coroutines.flow.Flow


@Dao
interface VehicleDAO : BaseDao<Vehicle> {
    @Query("SELECT * FROM vehicle")
    fun observeAllVehicles(): Flow<List<Vehicle>>

    @Query("SELECT * FROM vehicle WHERE vehicleID = :vehicleID")
    fun observeVehicle(vehicleID: String): Flow<Vehicle?>

    @Transaction
    @Query("SELECT * FROM vehicle WHERE vehicleID = :vehicleID")
    fun observeVehicleWithAttachments(vehicleID: String): Flow<VehicleWithAttachments?>
}