package com.dylanbeebe.fuelbuddy.domain.repository

import com.dylanbeebe.fuelbuddy.data.dao.MileageDAO
import com.dylanbeebe.fuelbuddy.data.model.Mileage
import com.dylanbeebe.fuelbuddy.data.room.dao.MinimalMileage

class MileageRepository(private val mileageDAO: MileageDAO) {
    suspend fun getAllForVehicle(vehicleID: String): List<MinimalMileage> =
        mileageDAO.getAllForVehicle(vehicleID)

    suspend fun insert(mileage: Mileage) {
        mileageDAO.insert(mileage)
    }
}