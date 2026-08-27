package com.dylanbeebe.fuelbuddy.data.room.mapper.vehicle

import com.dylanbeebe.fuelbuddy.data.room.entity.VehicleEntity
import com.dylanbeebe.fuelbuddy.domain.model.vehicle.Vehicle

fun VehicleEntity.toDomain(): Vehicle =
    Vehicle(
        vehicleID = vehicleID,
        nickname = nickname,
        make = make,
        model = model,
        modelYear = modelYear,
        plate = plate
    )

fun Vehicle.toEntity(): VehicleEntity =
    VehicleEntity(
        vehicleID = vehicleID,
        nickname = nickname,
        make = make,
        model = model,
        modelYear = modelYear,
        plate = plate
    )