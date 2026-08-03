package com.dylanbeebe.fuelbuddy.data.dao.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.dylanbeebe.fuelbuddy.data.model.User
import com.dylanbeebe.fuelbuddy.data.model.Vehicle

data class UserAndAllVehicles(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userID",
        entityColumn = "user"
    )
    val vehicles: List<Vehicle> = ArrayList()
)
