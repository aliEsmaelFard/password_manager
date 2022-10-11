package com.alief.passman.database

import androidx.room.*
import com.alief.passman.models.AccountModel
import com.alief.passman.models.BankCardModel
import com.alief.passman.models.UserModel

@Dao
interface MyDao
{
    //users table CRUD
    @Insert
    fun insertUser(user: UserModel)

    @Update
    fun updateUser(user: UserModel)

    @Query("SELECT * FROM users")
    fun getUsers(): MutableList<UserModel>

    @Query("SELECT * FROM users limit 1")
    fun getUser(): UserModel


    //accounts table CRUD
    @Insert
    fun insertAccount(account: AccountModel)

    @Update
    fun updateAccount(account: AccountModel)

    @Delete
    fun deleteAccount(account: AccountModel)

    @Query("SELECT * FROM accounts")
    fun getAccounts(): MutableList<AccountModel>

    @Query("SELECT * From accounts WHERE id = :id")
    fun getAccountById(id: Int): AccountModel


    //bank card table CRUD
    @Insert
    fun insertBankCard(bankCardModel: BankCardModel)

    @Update
    fun updateBankCard(bankCardModel: BankCardModel)

    @Delete
    fun deleteBankCard(bankCardModel: BankCardModel)

    @Query("SELECT * FROM bankCards")
    fun getBankCards(): MutableList<BankCardModel>

    @Query("SELECT * FROM bankCards WHERE id = :id")
    fun getBankCardById(id: Int): BankCardModel


}