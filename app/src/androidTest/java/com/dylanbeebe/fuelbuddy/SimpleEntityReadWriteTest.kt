package com.dylanbeebe.fuelbuddy

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dylanbeebe.fuelbuddy.domain.model.mileage.FuelType
import com.dylanbeebe.fuelbuddy.data.room.FuelBuddyDB
import com.dylanbeebe.fuelbuddy.data.room.repository.MileageRepositoryRoomImpl
import com.dylanbeebe.fuelbuddy.data.room.repository.VehicleRepositoryRoomImpl
import com.dylanbeebe.fuelbuddy.domain.model.mileage.Mileage
import com.dylanbeebe.fuelbuddy.domain.model.vehicle.Vehicle
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDateTime
import java.util.UUID

/** Instrumented test
 *
 * Confirm basic entity create and read functionality.
 * */
@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest {
//    private lateinit var vehicleDAO: VehicleDAO
//    private lateinit var mileageDAO: MileageDAO
    private lateinit var vehicleRepositoryRoomImpl: VehicleRepositoryRoomImpl
    private lateinit var mileageRepositoryRoomImpl: MileageRepositoryRoomImpl
    private lateinit var db: FuelBuddyDB

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, FuelBuddyDB::class.java).build()
        vehicleRepositoryRoomImpl = VehicleRepositoryRoomImpl(db.vehicleDAO(), db.vehicleAttachmentDAO())
        mileageRepositoryRoomImpl = MileageRepositoryRoomImpl(db.mileageDAO(), db.mileageAttachmentDAO())
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeVehicleAndReadInList() = runTest {
        val vehicle = Vehicle(
            vehicleID = UUID.randomUUID().toString(),
            nickname = "The Kia",
            make = "Kia",
            model = "Sorento",
            modelYear = 2015,
            plate = "ih8dis1",
        )
        vehicleRepositoryRoomImpl.insert(vehicle)

        val vehicles = vehicleRepositoryRoomImpl.observeAllVehicles().first()

        val expectedVehicle = Vehicle(
            vehicleID = vehicle.vehicleID,
            nickname = vehicle.nickname,
            make = vehicle.make,
            model = vehicle.model,
            modelYear = vehicle.modelYear,
            plate = vehicle.plate
        )
        assertThat(vehicles[0], equalTo(expectedVehicle))
    }

    @Test
    @Throws(Exception::class)
    fun writeMileageAndReadInList() = runTest {
        val vehicle = Vehicle(
            vehicleID = UUID.randomUUID().toString(),
            nickname = "The Kia",
            make = "Kia",
            model = "Sorento",
            modelYear = 2015,
            plate = "ih8dis1",
        )
        vehicleRepositoryRoomImpl.insert(vehicle)

        val mileage = Mileage(
            mileageID = UUID.randomUUID().toString(),
            timestamp = LocalDateTime.now().toString(),
            odometerMiles = 80085.69,
            volumeGallons = 6.9,
            isFullTank = true,
            fuelType = FuelType.REGULAR,
            totalDollars = 19.84,
            journal = "This is a test mileage log.",
            isExported = false,
            vehicleID = vehicle.vehicleID
        )
        mileageRepositoryRoomImpl.insert(mileage).toString()

        val vehicleMileages = mileageRepositoryRoomImpl.observeAllMileageForVehicle(vehicle.vehicleID).first()

        val expectedMileage = Mileage(
            mileageID = mileage.mileageID,
            timestamp = mileage.timestamp,
            odometerMiles = mileage.odometerMiles,
            volumeGallons = mileage.volumeGallons,
            isFullTank = mileage.isFullTank,
            fuelType = mileage.fuelType,
            totalDollars = mileage.totalDollars,
            journal = mileage.journal,
            isExported = mileage.isExported,
            vehicleID = mileage.vehicleID,
        )
        assertThat(vehicleMileages[0], equalTo(expectedMileage))
    }
}