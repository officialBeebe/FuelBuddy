package com.dylanbeebe.fuelbuddy.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.dylanbeebe.fuelbuddy.data.dao.relations.MileageMinimal
import com.dylanbeebe.fuelbuddy.data.dao.relations.VehicleAndAllMileages
import com.dylanbeebe.fuelbuddy.data.model.Mileage

@Dao
interface MileageDAO : BaseDao<Mileage> {
    @Query("SELECT * FROM mileage")
    fun getAll(): List<Mileage>

    @Query("SELECT mileageID, timestamp, totalDollars, vehicle FROM mileage")
    fun getAllMinimal(): List<MileageMinimal>

    @Transaction
    @Query("SELECT * FROM vehicle")
    fun getVehicleAndAllMileages(): List<VehicleAndAllMileages>
}