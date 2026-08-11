package com.dylanbeebe.fuelbuddy.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.dylanbeebe.fuelbuddy.data.model.Mileage
import com.dylanbeebe.fuelbuddy.data.room.dao.MinimalMileage
import com.dylanbeebe.fuelbuddy.data.room.relation.MileageWithAttachments

@Dao
interface MileageDAO : BaseDao<Mileage> {
//    @Query("SELECT * FROM mileage WHERE vehicle = :vehicleID ORDER BY timestamp DESC")
//    suspend fun getAllForVehicle(vehicleID: String): List<Mileage>
//
    @Query("SELECT mileageID, timestamp, odometerMiles, volumeGallons, totalDollars, isExported FROM mileage WHERE vehicle = :vehicleID ORDER BY timestamp DESC")
    suspend fun getAllForVehicle(vehicleID: String): List<MinimalMileage>

    @Transaction
    @Query("SELECT * FROM mileage WHERE mileageID = :mileageID")
    suspend fun getMileageWithAttachments(mileageID: String): MileageWithAttachments

}

