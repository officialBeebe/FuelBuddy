package com.dylanbeebe.fuelbuddy.data.room.mapper.mileage

import com.dylanbeebe.fuelbuddy.data.room.entity.MileageEntity
import com.dylanbeebe.fuelbuddy.domain.model.mileage.Mileage

fun MileageEntity.toDomain(): Mileage =
    Mileage(
        mileageID = mileageID,
        timestamp = timestamp,
        odometerMiles = odometerMiles,
        volumeGallons = volumeGallons,
        isFullTank = isFullTank,
        fuelType = fuelType,
        totalDollars = totalDollars,
        journal = journal,
        isExported = isExported,
        vehicleID = vehicleID
    )

fun Mileage.toEntity(): MileageEntity =
    MileageEntity(
        mileageID = mileageID,
        timestamp = timestamp,
        odometerMiles = odometerMiles,
        volumeGallons = volumeGallons,
        isFullTank = isFullTank,
        fuelType = fuelType,
        totalDollars = totalDollars,
        journal = journal,
        isExported = isExported,
        vehicleID = vehicleID
    )