package com.dylanbeebe.fuelbuddy.data.room.entity

import androidx.room.TypeConverter
import com.dylanbeebe.fuelbuddy.domain.model.mileage.FuelType

class FuelTypeConverter {
    @TypeConverter
    fun fromFuelType(value: FuelType): String = value.name

    @TypeConverter
    fun toFuelType(value: String): FuelType = FuelType.valueOf(value)
}