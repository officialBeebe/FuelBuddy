package com.dylanbeebe.fuelbuddy.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.dylanbeebe.fuelbuddy.data.model.Vehicle
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDAO : BaseDao<Vehicle> {
    @Query("SELECT * FROM vehicle")
    suspend fun getAll(): List<Vehicle>
}