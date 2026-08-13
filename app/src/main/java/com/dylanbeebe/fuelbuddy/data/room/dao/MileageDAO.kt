package com.dylanbeebe.fuelbuddy.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.dylanbeebe.fuelbuddy.data.model.Mileage
import com.dylanbeebe.fuelbuddy.data.room.relation.MileageWithAttachments
import kotlinx.coroutines.flow.Flow


@Dao
interface MileageDAO : BaseDao<Mileage> {
    @Query("SELECT * FROM mileage WHERE vehicle = :vehicleID ORDER BY timestamp DESC")
    fun observeAllForVehicle(vehicleID: String): Flow<List<Mileage>>

    @Transaction
    @Query("SELECT * FROM mileage WHERE mileageID = :mileageID")
    fun observeMileageWithAttachments(mileageID: String): Flow<MileageWithAttachments?>
}

