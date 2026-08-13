package com.dylanbeebe.fuelbuddy.data.room.dao

import androidx.room.Dao
import com.dylanbeebe.fuelbuddy.data.dao.BaseDao
import com.dylanbeebe.fuelbuddy.data.room.entity.MileageAttachment

@Dao
interface MileageAttachmentDAO : BaseDao<MileageAttachment> {
}