package com.dylanbeebe.fuelbuddy.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class User(
    @PrimaryKey val userID: String = UUID.randomUUID().toString()
)

