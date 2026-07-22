package com.dylanbeebe.fuelbuddy.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dylanbeebe.fuelbuddy.data.model.User

@Dao
interface UserDAO : BaseDao<User> {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

}
