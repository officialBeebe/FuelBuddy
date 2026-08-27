package com.dylanbeebe.fuelbuddy.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.dylanbeebe.fuelbuddy.data.room.entity.MileageEntity
import com.dylanbeebe.fuelbuddy.data.room.relation.MileageEntityWithAttachments
import com.dylanbeebe.fuelbuddy.domain.model.mileage.Mileage
import kotlinx.coroutines.flow.Flow


@Dao
interface MileageDAO : BaseDao<MileageEntity> {
    @Query("SELECT * FROM mileage WHERE mileageID = :mileageID")
    suspend fun getMileage(mileageID: String): MileageEntity

    @Query("SELECT * FROM mileage WHERE vehicleID = :vehicleID ORDER BY timestamp DESC")
    fun observeAllForVehicle(vehicleID: String): Flow<List<MileageEntity>>

    @Transaction
    @Query("SELECT * FROM mileage WHERE mileageID = :mileageID")
    fun observeMileageWithAttachments(mileageID: String): Flow<MileageEntityWithAttachments?>

    @Query("""
    SELECT * FROM mileage
    WHERE vehicleID = :vehicleID
      AND isExported = 0
    ORDER BY timestamp DESC
""")
    suspend fun getUnexportedForVehicle(vehicleID: String): List<MileageEntity>

    @Query("SELECT * FROM mileage WHERE vehicleID = :vehicleID AND timestamp BETWEEN :start AND :end ORDER BY timestamp")
    suspend fun getForVehicleInRange(vehicleID: String, start: String, end: String): List<MileageEntity>
}

