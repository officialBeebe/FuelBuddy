package com.dylanbeebe.fuelbuddy

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dylanbeebe.fuelbuddy.data.room.FuelBuddyDB
import com.dylanbeebe.fuelbuddy.data.room.repository.MileageRepositoryRoomImpl
import com.dylanbeebe.fuelbuddy.data.room.repository.VehicleRepositoryRoomImpl
import com.dylanbeebe.fuelbuddy.domain.model.vehicle.Vehicle
import com.dylanbeebe.fuelbuddy.domain.model.vehicle.VehicleInput
import com.dylanbeebe.fuelbuddy.domain.repository.MileageRepository
import com.dylanbeebe.fuelbuddy.domain.usecase.mileage.AddMileageWithAttachments
import com.dylanbeebe.fuelbuddy.domain.usecase.mileage.DeleteAttachment
import com.dylanbeebe.fuelbuddy.domain.usecase.mileage.DeleteMileage
import com.dylanbeebe.fuelbuddy.domain.usecase.mileage.UpdateMileageWithAttachments
import com.dylanbeebe.fuelbuddy.domain.usecase.vehicle.AddVehicle
import com.dylanbeebe.fuelbuddy.domain.usecase.vehicle.DeleteVehicle
import com.dylanbeebe.fuelbuddy.domain.usecase.vehicle.ObserveAllVehicles
import com.dylanbeebe.fuelbuddy.domain.usecase.vehicle.ObserveVehicle
import com.dylanbeebe.fuelbuddy.domain.usecase.vehicle.UpdateVehicle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class SimpleVehicleUseCaseTest {
    private lateinit var db: FuelBuddyDB
    private lateinit var vehicleRepositoryRoomImpl: VehicleRepositoryRoomImpl

    private lateinit var addVehicle: AddVehicle
    private lateinit var observeAllVehicles: ObserveAllVehicles
    private lateinit var observeVehicle: ObserveVehicle
    private lateinit var updateVehicle: UpdateVehicle
    private lateinit var deleteVehicle: DeleteVehicle

    private lateinit var testVehicleInput: VehicleInput

    @Before
    fun init() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, FuelBuddyDB::class.java).build()
        vehicleRepositoryRoomImpl = VehicleRepositoryRoomImpl(db.vehicleDAO(), db.vehicleAttachmentDAO())

        addVehicle = AddVehicle(repository = vehicleRepositoryRoomImpl)
        observeAllVehicles = ObserveAllVehicles(repository = vehicleRepositoryRoomImpl)
        observeVehicle = ObserveVehicle(repository = vehicleRepositoryRoomImpl)
        updateVehicle = UpdateVehicle(repository = vehicleRepositoryRoomImpl)
        deleteVehicle = DeleteVehicle(repository = vehicleRepositoryRoomImpl)

        testVehicleInput = VehicleInput(
            nickname = "Test Vehicle",
            make = "Test Make",
            model = "Test Model",
            modelYear = 1969,
            plate = "itstpl8"
        )
    }

    @After
    @Throws(IOException::class)
    fun cleanUp() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun addVehicleUseCase() = runTest {
        addVehicle(testVehicleInput)
        val addedVehicle: Vehicle = observeAllVehicles().first().first()
        assertThat(addedVehicle.nickname, equalTo(testVehicleInput.nickname))
    }

    @Test
    @Throws(Exception::class)
    fun updateVehicleUseCase() = runTest {
        addVehicle(testVehicleInput)
        val addedVehicle: Vehicle = observeAllVehicles().first().first()

        val updatedInput = testVehicleInput.copy(nickname = "Updated Vehicle")
        updateVehicle(addedVehicle.vehicleID, updatedInput)

        val observedVehicle: Vehicle = observeVehicle(addedVehicle.vehicleID).first()
        assertThat(observedVehicle.nickname, equalTo(updatedInput.nickname))
    }

    @Test
    @Throws(Exception::class)
    fun deleteVehicleUseCase() = runTest {
        addVehicle(testVehicleInput)
        val addedVehicle: Vehicle = observeAllVehicles().first().first()

        deleteVehicle(addedVehicle.vehicleID)

        val remainingVehicles: List<Vehicle> = observeAllVehicles().first()
        assertThat(remainingVehicles.isEmpty(), equalTo(true))
    }
}