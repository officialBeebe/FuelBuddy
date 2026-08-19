package com.dylanbeebe.fuelbuddy

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dylanbeebe.fuelbuddy.domain.model.mileage.FuelType
import com.dylanbeebe.fuelbuddy.data.room.entity.Mileage
import com.dylanbeebe.fuelbuddy.data.room.entity.Vehicle
import com.dylanbeebe.fuelbuddy.data.room.FuelBuddyDB
import com.dylanbeebe.fuelbuddy.data.room.repository.MileageRepositoryImpl
import com.dylanbeebe.fuelbuddy.data.room.repository.VehicleRepositoryImpl
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
/** Instrumented test
 *
 * Confirm basic entity create and read functionality.
 * */
@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest {
//    private lateinit var vehicleDAO: VehicleDAO
//    private lateinit var mileageDAO: MileageDAO
    private lateinit var vehicleRepositoryImpl: VehicleRepositoryImpl
    private lateinit var mileageRepositoryImpl: MileageRepositoryImpl
    private lateinit var db: FuelBuddyDB

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, FuelBuddyDB::class.java).build()
        vehicleRepositoryImpl = VehicleRepositoryImpl(db.vehicleDAO(), db.vehicleAttachmentDAO())
        mileageRepositoryImpl = MileageRepositoryImpl(db.mileageDAO(), db.mileageAttachmentDAO())
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
            nickname = "The Kia",
            make = "Kia",
            model = "Sorento",
            modelYear = 2015,
            plate = "ih8dis1",
        )
        vehicleRepositoryImpl.insert(vehicle)

        val vehicles = vehicleRepositoryImpl.observeAllVehicles().first()

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
            nickname = "The Kia",
            make = "Kia",
            model = "Sorento",
            modelYear = 2015,
            plate = "ih8dis1",
        )
        vehicleRepositoryImpl.insert(vehicle)

        val mileage = Mileage(
            timestamp = LocalDateTime.now().toString(),
            odometerMiles = 80085.69,
            volumeGallons = 6.9,
            isFullTank = true,
            fuelType = FuelType.REGULAR,
            totalDollars = 19.84,
            journal = "This is a test mileage log.",
            vehicle = vehicle.vehicleID)
        mileageRepositoryImpl.insert(mileage).toString()

        val vehicleMileages = mileageRepositoryImpl.observeAllForVehicle(vehicle.vehicleID).first()

        val expectedMileage = Mileage(
            mileageID = mileage.mileageID,
            timestamp = mileage.timestamp,
            odometerMiles = mileage.odometerMiles,
            volumeGallons = mileage.volumeGallons,
            isFullTank = mileage.isFullTank,
            fuelType = mileage.fuelType,
            totalDollars = mileage.totalDollars,
            journal = mileage.journal,
            vehicle = mileage.vehicle,
            isExported = mileage.isExported
        )
        assertThat(vehicleMileages[0], equalTo(expectedMileage))
    }
}