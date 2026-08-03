package com.dylanbeebe.fuelbuddy.data.room.repository

import com.dylanbeebe.fuelbuddy.data.dao.MileageDAO
import com.dylanbeebe.fuelbuddy.data.model.Mileage
import kotlinx.coroutines.flow.Flow


class MileageRepository(private val mileageDAO: MileageDAO) {
    fun getAllForVehicle(vehicleID: String): Flow<List<Mileage>> =
        mileageDAO.getAllForVehicle(vehicleID)

    suspend fun insert(mileage: Mileage) {
        mileageDAO.insert(mileage)
    }
}