package com.dylanbeebe.fuelbuddy

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dylanbeebe.fuelbuddy.data.room.FuelBuddyDB
import com.dylanbeebe.fuelbuddy.data.room.repository.MileageRepositoryRoomImpl
import com.dylanbeebe.fuelbuddy.data.room.repository.VehicleRepositoryRoomImpl
import com.dylanbeebe.fuelbuddy.domain.model.mileage.FuelType
import com.dylanbeebe.fuelbuddy.domain.model.mileage.Mileage
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageAttachmentInput
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageInput
import com.dylanbeebe.fuelbuddy.domain.model.mileage.MileageWithAttachments
import com.dylanbeebe.fuelbuddy.domain.model.vehicle.Vehicle
import com.dylanbeebe.fuelbuddy.domain.model.vehicle.VehicleInput
import com.dylanbeebe.fuelbuddy.domain.usecase.mileage.AddMileageWithAttachments
import com.dylanbeebe.fuelbuddy.domain.usecase.mileage.DeleteAttachment
import com.dylanbeebe.fuelbuddy.domain.usecase.mileage.DeleteMileage
import com.dylanbeebe.fuelbuddy.domain.usecase.mileage.ObserveAllMileageForVehicle
import com.dylanbeebe.fuelbuddy.domain.usecase.mileage.ObserveMileageWithAttachments
import com.dylanbeebe.fuelbuddy.domain.usecase.mileage.UpdateMileageWithAttachments
import com.dylanbeebe.fuelbuddy.domain.usecase.vehicle.AddVehicle
import com.dylanbeebe.fuelbuddy.domain.usecase.vehicle.ObserveAllVehicles
import com.dylanbeebe.fuelbuddy.domain.usecase.vehicle.ObserveVehicle
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SimpleMileageUseCaseTest {
    private lateinit var db: FuelBuddyDB
    private lateinit var vehicleRepositoryRoomImpl: VehicleRepositoryRoomImpl
    private lateinit var mileageRepositoryRoomImpl: MileageRepositoryRoomImpl

    private lateinit var addVehicle: AddVehicle
    private lateinit var observeAllVehicles: ObserveAllVehicles
    private lateinit var observeVehicle: ObserveVehicle

    private lateinit var addMileageWithAttachments: AddMileageWithAttachments
    private lateinit var observeAllMileageForVehicle: ObserveAllMileageForVehicle
    private lateinit var observeMileageWithAttachments: ObserveMileageWithAttachments
    private lateinit var updateMileageWithAttachments: UpdateMileageWithAttachments
    private lateinit var deleteMileage: DeleteMileage
    private lateinit var deleteAttachment: DeleteAttachment

    private lateinit var testVehicleInput: VehicleInput
    private lateinit var testVehicle: Vehicle

    private lateinit var testMileageInput: MileageInput
    private lateinit var testMileageAttachmentInput: MileageAttachmentInput

    @Before
    fun init() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, FuelBuddyDB::class.java).build()
        vehicleRepositoryRoomImpl = VehicleRepositoryRoomImpl(db.vehicleDAO(), db.vehicleAttachmentDAO())
        mileageRepositoryRoomImpl = MileageRepositoryRoomImpl(db.mileageDAO(), db.mileageAttachmentDAO())

        addVehicle = AddVehicle(repository = vehicleRepositoryRoomImpl)
        observeAllVehicles = ObserveAllVehicles(repository = vehicleRepositoryRoomImpl)
        observeVehicle = ObserveVehicle(repository = vehicleRepositoryRoomImpl)

        addMileageWithAttachments = AddMileageWithAttachments(repository = mileageRepositoryRoomImpl)
        observeAllMileageForVehicle = ObserveAllMileageForVehicle(repository = mileageRepositoryRoomImpl)
        observeMileageWithAttachments = ObserveMileageWithAttachments(repository = mileageRepositoryRoomImpl)
        updateMileageWithAttachments = UpdateMileageWithAttachments(repository = mileageRepositoryRoomImpl)
        deleteMileage = DeleteMileage(repository = mileageRepositoryRoomImpl)
        deleteAttachment = DeleteAttachment(repository = mileageRepositoryRoomImpl)

        testVehicleInput = VehicleInput(
            nickname = "Test Vehicle",
            make = "Test Make",
            model = "Test Model",
            modelYear = 1969,
            plate = "itstpl8"
        )
        addVehicle(testVehicleInput)
        testVehicle = observeAllVehicles().first().first()

        testMileageInput = MileageInput(
            odometerMiles = 169420.21,
            volumeGallons = 24.00,
            isFullTank = true,
            fuelType = FuelType.REGULAR,
            totalDollars = 96.69,
            journal = "The test journal.",
        )

        testMileageAttachmentInput = MileageAttachmentInput(
            filepath = "test/mileage/attachment/path/"
        )
    }

    @After
    @Throws(IOException::class)
    fun cleanUp() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun addMileageWithAttachmentsUseCase() = runTest {
        addMileageWithAttachments(testVehicle.vehicleID, testMileageInput, listOf(testMileageAttachmentInput))

        val addedMileage: Mileage = observeAllMileageForVehicle(testVehicle.vehicleID).first().first()
        val addedMileageWithAttachments: MileageWithAttachments? =
            observeMileageWithAttachments(addedMileage.mileageID).first()

        assertThat(addedMileageWithAttachments?.mileage?.mileageID, equalTo(addedMileage.mileageID))
    }

    @Test
    @Throws(Exception::class)
    fun updateMileageWithAttachmentsUseCase() = runTest {
        addMileageWithAttachments(testVehicle.vehicleID, testMileageInput, listOf(testMileageAttachmentInput))
        val addedMileage: Mileage = observeAllMileageForVehicle(testVehicle.vehicleID).first().first()
        val addedMileageWithAttachments: MileageWithAttachments? =
            observeMileageWithAttachments(addedMileage.mileageID).first()
        val existingAttachment = requireNotNull(addedMileageWithAttachments?.attachments?.first())

        val updatedInput = testMileageInput.copy(journal = "Updated journal.")
        val newAttachmentInput = MileageAttachmentInput(filepath = "test/mileage/attachment/updated/")

        updateMileageWithAttachments(
            addedMileage.mileageID,
            updatedInput,
            listOf(newAttachmentInput),
            listOf(existingAttachment)
        )

        val observedMileageWithAttachments: MileageWithAttachments? =
            observeMileageWithAttachments(addedMileage.mileageID).first()

        assertThat(observedMileageWithAttachments?.mileage?.journal, equalTo(updatedInput.journal))
        assertThat(observedMileageWithAttachments?.attachments?.size, equalTo(1))
        assertThat(observedMileageWithAttachments?.attachments?.first()?.filepath, equalTo(newAttachmentInput.filepath))
    }

    @Test
    @Throws(Exception::class)
    fun deleteMileageUseCase() = runTest {
        addMileageWithAttachments(testVehicle.vehicleID, testMileageInput, listOf(testMileageAttachmentInput))
        val addedMileage: Mileage = observeAllMileageForVehicle(testVehicle.vehicleID).first().first()

        deleteMileage(addedMileage.mileageID)

        val remainingMileage: List<Mileage> = observeAllMileageForVehicle(testVehicle.vehicleID).first()
        assertThat(remainingMileage.isEmpty(), equalTo(true))
    }

    @Test
    @Throws(Exception::class)
    fun deleteAttachmentUseCase() = runTest {
        addMileageWithAttachments(testVehicle.vehicleID, testMileageInput, listOf(testMileageAttachmentInput))
        val addedMileage: Mileage = observeAllMileageForVehicle(testVehicle.vehicleID).first().first()
        val addedMileageWithAttachments: MileageWithAttachments? =
            observeMileageWithAttachments(addedMileage.mileageID).first()
        val existingAttachment = requireNotNull(addedMileageWithAttachments?.attachments?.first())

        deleteAttachment(existingAttachment)

        val observedMileageWithAttachments: MileageWithAttachments? =
            observeMileageWithAttachments(addedMileage.mileageID).first()
        assertThat(observedMileageWithAttachments?.attachments?.isEmpty(), equalTo(true))
    }
}