package com.dylanbeebe.fuelbuddy.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dylanbeebe.fuelbuddy.data.dao.MileageDAO
import com.dylanbeebe.fuelbuddy.data.dao.VehicleDAO
import com.dylanbeebe.fuelbuddy.data.room.entity.VehicleEntity
import com.dylanbeebe.fuelbuddy.data.room.dao.MileageAttachmentDAO
import com.dylanbeebe.fuelbuddy.data.room.dao.VehicleAttachmentDAO
import com.dylanbeebe.fuelbuddy.data.room.entity.MileageAttachmentEntity
import com.dylanbeebe.fuelbuddy.data.room.entity.MileageEntity
import com.dylanbeebe.fuelbuddy.data.room.entity.VehicleAttachmentEntity

@Database(entities = [VehicleEntity::class, VehicleAttachmentEntity::class, MileageEntity::class, MileageAttachmentEntity::class], version = 1)
abstract class FuelBuddyDB : RoomDatabase() {
    abstract fun vehicleDAO(): VehicleDAO
    abstract fun vehicleAttachmentDAO(): VehicleAttachmentDAO
    abstract fun mileageDAO(): MileageDAO
    abstract fun mileageAttachmentDAO(): MileageAttachmentDAO

    companion object {
        @Volatile
        private var Instance: FuelBuddyDB? = null

        fun getDatabase(context: Context): FuelBuddyDB {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, FuelBuddyDB::class.java, "item_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}