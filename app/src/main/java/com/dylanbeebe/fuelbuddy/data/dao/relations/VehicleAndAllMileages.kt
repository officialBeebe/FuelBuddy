package com.dylanbeebe.fuelbuddy.data.dao.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.dylanbeebe.fuelbuddy.data.model.Mileage
import com.dylanbeebe.fuelbuddy.data.model.Vehicle

data class VehicleAndAllMileages(
    @Embedded val vehicle: Vehicle,
    @Relation(
        parentColumn = "vehicleID",
        entityColumn = "vehicle"
    )
    val mileages: List<Mileage> = ArrayList()
)
