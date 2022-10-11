package com.alief.passman.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alief.passman.models.AccountModel
import com.alief.passman.models.BankCardModel
import com.alief.passman.models.UserModel

@Database(entities = [UserModel::class, BankCardModel::class, AccountModel::class], version = 1, exportSchema = false)
abstract class MyDataBase: RoomDatabase()
{

    abstract fun getDao(): MyDao?

    companion object {
        private var dataBase: MyDataBase? = null

        @Synchronized
        fun getInstance(context: Context): MyDataBase {
            if (dataBase == null) {
                dataBase = Room.databaseBuilder(
                    context.applicationContext,
                    MyDataBase::class.java,
                    "database"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return dataBase!!
        }
    }
}