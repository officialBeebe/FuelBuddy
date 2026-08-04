package com.dylanbeebe.fuelbuddy

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dylanbeebe.fuelbuddy.data.model.FuelType
import com.dylanbeebe.fuelbuddy.data.model.Mileage
import com.dylanbeebe.fuelbuddy.data.model.Vehicle
import com.dylanbeebe.fuelbuddy.data.room.FuelBuddyDB
import com.dylanbeebe.fuelbuddy.domain.repository.MileageRepository
import com.dylanbeebe.fuelbuddy.domain.repository.VehicleRepository
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
    private lateinit var vehicleRepository: VehicleRepository
    private lateinit var mileageRepository: MileageRepository
    private lateinit var db: FuelBuddyDB

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, FuelBuddyDB::class.java).build()
        vehicleRepository = VehicleRepository(db.vehicleDAO())
        mileageRepository = MileageRepository(db.mileageDAO())
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
        vehicleRepository.insert(vehicle)

        val vehicles = vehicleRepository.allVehicles()
        assertThat(vehicles[0], equalTo(vehicle))
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
        vehicleRepository.insert(vehicle)

        val mileage = Mileage(
            timestamp = LocalDateTime.now().toString(),
            latitude = -48.876667,
            longitude = -123.393333,
            odometerMiles = 80085.69,
            volumeGallons = 6.9,
            isFullTank = true,
            fuelType = FuelType.REGULAR,
            totalDollars = 19.84,
            journal = "This is a test mileage log.",
            vehicle = vehicle.vehicleID)
        mileageRepository.insert(mileage)

        val vehicleMileages = mileageRepository.getAllForVehicle(vehicle.vehicleID)
        assertThat(vehicleMileages[0], equalTo(mileage))
    }
}