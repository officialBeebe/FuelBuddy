package com.dylanbeebe.fuelbuddy

import android.content.Context
import androidx.media3.test.utils.TestUtil
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dylanbeebe.fuelbuddy.data.dao.MileageDAO
import com.dylanbeebe.fuelbuddy.data.dao.UserDAO
import com.dylanbeebe.fuelbuddy.data.dao.VehicleDAO
import com.dylanbeebe.fuelbuddy.data.model.FuelType
import com.dylanbeebe.fuelbuddy.data.model.Mileage
import com.dylanbeebe.fuelbuddy.data.model.User
import com.dylanbeebe.fuelbuddy.data.model.Vehicle
import com.dylanbeebe.fuelbuddy.data.repository.FuelBuddyDB
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException
import java.time.LocalDateTime
import java.util.UUID

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.dylanbeebe.fuelbuddy", appContext.packageName)
    }
}

@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest {
    private lateinit var userDAO: UserDAO
    private lateinit var vehicleDAO: VehicleDAO
    private lateinit var mileageDAO: MileageDAO
    private lateinit var db: FuelBuddyDB

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, FuelBuddyDB::class.java).build()
        userDAO = db.userDAO()
        vehicleDAO = db.vehicleDAO()
        mileageDAO = db.mileageDAO()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() = runTest {
        val user = User()
        userDAO.insert(user)
        val users = userDAO.getAll()
        assertThat(users[0], equalTo(user))
    }

    @Test
    @Throws(Exception::class)
    fun writeVehicleAndReadInList() = runTest {
        val user = User()
        userDAO.insert(user)

        val vehicle = Vehicle(
            nickname = "The Kia",
            make = "Kia",
            model = "Sorento",
            modelYear = 2015,
            plate = "ih8dis1",
            user = user.userID)
        vehicleDAO.insert(vehicle)

        val vehicles = vehicleDAO.getAll()
        assertThat(vehicles[0], equalTo(vehicle))
    }

    @Test
    @Throws(Exception::class)
    fun writeMileageAndReadInList() = runTest {
        val user = User()
        userDAO.insert(user)

        val vehicle = Vehicle(
            nickname = "The Kia",
            make = "Kia",
            model = "Sorento",
            modelYear = 2015,
            plate = "ih8dis1",
            user = user.userID)
        vehicleDAO.insert(vehicle)

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
        mileageDAO.insert(mileage)

        val mileages = mileageDAO.getAll()
        assertThat(mileages[0], equalTo(mileage))
    }
}