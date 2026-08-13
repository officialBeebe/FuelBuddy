package com.dylanbeebe.fuelbuddy.domain.repository

import com.dylanbeebe.fuelbuddy.data.dao.MileageDAO
import com.dylanbeebe.fuelbuddy.data.model.Mileage
import com.dylanbeebe.fuelbuddy.data.room.dao.MileageAttachmentDAO
import com.dylanbeebe.fuelbuddy.data.room.entity.MileageAttachment
import com.dylanbeebe.fuelbuddy.data.room.relation.MileageWithAttachments
import kotlinx.coroutines.flow.Flow

class MileageRepository(
    private val mileageDAO: MileageDAO,
    private val mileageAttachmentDAO: MileageAttachmentDAO
) {
    fun observeAllForVehicle(vehicleID: String): Flow<List<Mileage>> =
        mileageDAO.observeAllForVehicle(vehicleID)

    fun observeMileageWithAttachments(mileageID: String): Flow<MileageWithAttachments?> =
        mileageDAO.observeMileageWithAttachments(mileageID)

    suspend fun insert(mileage: Mileage) = mileageDAO.insert(mileage)
    suspend fun update(mileage: Mileage) = mileageDAO.update(mileage)
    suspend fun delete(mileage: Mileage) = mileageDAO.delete(mileage)

    suspend fun addAttachment(attachment: MileageAttachment) = mileageAttachmentDAO.insert(attachment)
    suspend fun removeAttachment(attachment: MileageAttachment) = mileageAttachmentDAO.delete(attachment)
}