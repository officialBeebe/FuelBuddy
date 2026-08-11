package com.dylanbeebe.fuelbuddy.domain.repository

import com.dylanbeebe.fuelbuddy.data.dao.MileageDAO
import com.dylanbeebe.fuelbuddy.data.model.Mileage

class MileageRepository(private val mileageDAO: MileageDAO) {
    suspend fun getAllForVehicle(vehicleID: String): List<Mileage> =
        mileageDAO.getAllForVehicle(vehicleID)

    suspend fun insert(mileage: Mileage) {
        mileageDAO.insert(mileage)
    }
}