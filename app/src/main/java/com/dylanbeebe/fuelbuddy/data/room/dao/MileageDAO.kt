package com.dylanbeebe.fuelbuddy.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.dylanbeebe.fuelbuddy.data.model.Mileage
import kotlinx.coroutines.flow.Flow

@Dao
interface MileageDAO : BaseDao<Mileage> {
    @Query("SELECT * FROM mileage WHERE vehicle = :vehicleID ORDER BY timestamp DESC")
    fun getAllForVehicle(vehicleID: String): Flow<List<Mileage>>
}