package com.dylanbeebe.fuelbuddy.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.dylanbeebe.fuelbuddy.data.model.Vehicle
import com.dylanbeebe.fuelbuddy.data.room.relation.VehicleWithAttachments

@Dao
interface VehicleDAO : BaseDao<Vehicle> {
    @Query("SELECT * FROM vehicle")
    suspend fun getAll(): List<Vehicle>

    @Transaction
    @Query("SELECT * FROM vehicle WHERE vehicleID = :vehicleID")
    suspend fun getVehicleWithAttachments(vehicleID: String): VehicleWithAttachments
}