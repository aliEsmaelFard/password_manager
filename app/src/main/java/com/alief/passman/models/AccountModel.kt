package com.alief.passman.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountModel(

    @ColumnInfo(name = "user_name")
    var userName: String,

    var password: String,

    @ColumnInfo(name = "image_recourse")
    var imageRecourseName: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    )
