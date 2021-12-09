package com.example.account_book.Data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//-----------------------------------------------------------------------------------------------------------------------
//DB SQL문
//-----------------------------------------------------------------------------------------------------------------------
class AccountInfoManager(context: Context, fileName: String)
    : SQLiteOpenHelper(context, fileName, null, DATABASE_VERSION) {

    companion object {
        val DATABASE_VERSION = 1
        var INSTANCE: AccountInfoManager? = null
        fun getInstance(context: Context, fileName: String): AccountInfoManager {
            if (INSTANCE == null) {
                INSTANCE = AccountInfoManager(context, fileName)
            }
            return INSTANCE!!
        }
    }

    fun insert(tableNum: Int, values: ContentValues): Long {
        val db: SQLiteDatabase = writableDatabase
        if(tableNum == 1){
             return db.insert(AccountInfo.IncomeInfo.TABLE_NAME, null, values)
        }
        else if(tableNum == 2){
             return db.insert(AccountInfo.SpendInfo.TABLE_NAME, null, values)
        }
        else{
             return db.insert(AccountInfo.TotalMoneyInfo.TABLE_NAME, null, values)
        }
    }

    fun queryAll(tableNum: Int): Cursor? {
        val db: SQLiteDatabase = readableDatabase
        if(tableNum == 1){
            return db.query(AccountInfo.IncomeInfo.TABLE_NAME, null, null, null, null, null, null)
        }
        else if (tableNum == 2){
            return db.query(AccountInfo.SpendInfo.TABLE_NAME, null, null, null, null, null, null)
        }
        else{
            return db.query(AccountInfo.TotalMoneyInfo.TABLE_NAME, null, null, null, null, null, null)
        }
    }

    fun query(tableNum: Int, columns: Array<String>, selection: String, selectionArgs: Array<String>, orderBy: String): Cursor {
        val db: SQLiteDatabase = readableDatabase
        return if(tableNum == 1){
            db.query(AccountInfo.IncomeInfo.TABLE_NAME, columns, selection, selectionArgs, null, null, orderBy)
        } else if(tableNum == 2){
            db.query(AccountInfo.SpendInfo.TABLE_NAME, columns, selection, selectionArgs, null, null, orderBy)
        } else{
            db.query(AccountInfo.TotalMoneyInfo.TABLE_NAME, columns, selection, selectionArgs, null, null, orderBy)
        }
    }

    fun deleteAll(tableNum: Int): Int {
        val db: SQLiteDatabase = readableDatabase
        return if(tableNum == 1){
            db.delete(AccountInfo.IncomeInfo.TABLE_NAME, null, null)
        }
        else if(tableNum == 2){
            db.delete(AccountInfo.SpendInfo.TABLE_NAME, null, null)
        } else{
            db.delete(AccountInfo.TotalMoneyInfo.TABLE_NAME, null, null)
        }
    }

    fun delete(tableNum: Int, whereClause: String, whereArgs: Array<String>): Int {
        val db: SQLiteDatabase = readableDatabase
        return if(tableNum == 1){
            db.delete(AccountInfo.IncomeInfo.TABLE_NAME, whereClause, whereArgs)
        }
        else if(tableNum == 2){
            db.delete(AccountInfo.SpendInfo.TABLE_NAME, whereClause, whereArgs)
        } else {
            db.delete(AccountInfo.TotalMoneyInfo.TABLE_NAME, whereClause, whereArgs)
        }
    }

    fun update(tableNum: Int, values: ContentValues, whereClause: String, whereArgs: Array<String>): Int {
        var db: SQLiteDatabase = readableDatabase
        return if(tableNum == 1){
            db.update(AccountInfo.IncomeInfo.TABLE_NAME, values, whereClause, whereArgs)
        }
        else if(tableNum == 2){
            db.update(AccountInfo.SpendInfo.TABLE_NAME, values, whereClause, whereArgs)
        } else {
            db.update(AccountInfo.TotalMoneyInfo.TABLE_NAME, values, whereClause, whereArgs)
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(AccountInfo.SpendInfo.CREATE_TABLE_SPEND)
        db?.execSQL(AccountInfo.IncomeInfo.CREATE_TABLE_INCOME)
        db?.execSQL(AccountInfo.TotalMoneyInfo.CREATE_TABLE_TOTAL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(AccountInfo.SpendInfo.DROP_TABLE_SPEND)
        db?.execSQL(AccountInfo.IncomeInfo.DROP_TABLE_INCOME)
        db?.execSQL(AccountInfo.TotalMoneyInfo.DROP_TABLE_TOTAL)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

}