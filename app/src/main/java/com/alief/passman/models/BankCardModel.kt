package com.alief.passman.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bankCards")
data class BankCardModel(

    @ColumnInfo(name = "bank_name")
    var bankName: String,

    @ColumnInfo(name = "card_number")
    var cardNumber: String,

    var cvv2: String,

    @ColumnInfo(name = "expiration_date1")
    var expirationDateP1: String,

    @ColumnInfo(name = "expiration_date2")
    var expirationDateP2: String,

    @ColumnInfo(name = "first_password")
    var firstPass: String,

    @ColumnInfo(name = "second_password")
    var secondPass: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
