package com.dylanbeebe.fuelbuddy.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.dylanbeebe.fuelbuddy.data.room.entity.Mileage
import com.dylanbeebe.fuelbuddy.data.room.relation.MileageWithAttachments
import kotlinx.coroutines.flow.Flow


@Dao
interface MileageDAO : BaseDao<Mileage> {
    @Query("SELECT * FROM mileage WHERE vehicle = :vehicleID ORDER BY timestamp DESC")
    fun observeAllForVehicle(vehicleID: String): Flow<List<Mileage>>

    @Transaction
    @Query("SELECT * FROM mileage WHERE mileageID = :mileageID")
    fun observeMileageWithAttachments(mileageID: String): Flow<MileageWithAttachments?>

    @Query("""
    SELECT * FROM mileage
    WHERE vehicle = :vehicleID
      AND isExported = 0
    ORDER BY timestamp DESC
""")
    suspend fun getUnexportedForVehicle(vehicleID: String): List<Mileage>

    @Query("SELECT * FROM Mileage WHERE vehicle = :vehicleID AND timestamp BETWEEN :start AND :end ORDER BY timestamp")
    suspend fun getForVehicleInRange(vehicleID: String, start: String, end: String): List<Mileage>
}

