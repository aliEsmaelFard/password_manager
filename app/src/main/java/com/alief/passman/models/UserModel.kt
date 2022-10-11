package com.alief.passman.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserModel(

    var password: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    )