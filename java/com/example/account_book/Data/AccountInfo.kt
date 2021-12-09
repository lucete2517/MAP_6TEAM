package com.example.account_book.Data

import android.provider.BaseColumns

//-----------------------------------------------------------------------------------------------------------------------
//DB 저장을 위한 object
//-----------------------------------------------------------------------------------------------------------------------
object AccountInfo {
    object SpendInfo : BaseColumns{
        const val TABLE_NAME = "spendAccount"
        const val SPEND_ID = BaseColumns._ID
        const val SPEND_DATE = "date"
        const val SPEND_CATEGORY = "place"
        const val SPEND_PRICE = "price"
        const val SPEND_PAY_METHOD = "paymethod"
        const val SPEND_MEMO = "memo"
        const val SPEND_PHOTO = "photo"
        val CREATE_TABLE_SPEND = "CREATE TABLE $TABLE_NAME (" +
                "$SPEND_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$SPEND_DATE TEXT not null," +
                "$SPEND_PAY_METHOD TEXT not null," +
                "$SPEND_PRICE INTEGER not null," +
                "$SPEND_CATEGORY TEXT," +
                "$SPEND_MEMO TEXT," +
                "$SPEND_PHOTO TEXT);"
        val DROP_TABLE_SPEND = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

    object IncomeInfo : BaseColumns{
        const val TABLE_NAME = "IncomeAccount"
        const val INCOME_ID = BaseColumns._ID
        const val INCOME_DATE = "date"
        const val INCOME_CATEGORY = "place"
        const val INCOME_PRICE = "price"
        const val INCOME_PAY_METHOD = "paymethod"
        const val INCOME_MEMO = "memo"
        const val INCOME_PHOTO = "photo"
        val CREATE_TABLE_INCOME = "CREATE TABLE $TABLE_NAME (" +
                "$INCOME_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$INCOME_DATE TEXT not null," +
                "$INCOME_PAY_METHOD TEXT not null," +
                "$INCOME_PRICE INTEGER not null," +
                "$INCOME_CATEGORY TEXT," +
                "$INCOME_MEMO TEXT," +
                "$INCOME_PHOTO TEXT);"
        val DROP_TABLE_INCOME = "DROP TABLE IF EXISTS $TABLE_NAME"
    }

    object TotalMoneyInfo : BaseColumns {
        const val TABLE_NAME = "totalMoney"
        const val TOTAL_ID = BaseColumns._ID
        const val TOTAL_DATE = "date"
        const val TOTAL_INCOME = "incomeprice"
        const val TOTAL_SPEND = "spendprice"
        const val TOTAL_MONEY = "money"
        val CREATE_TABLE_TOTAL = "CREATE TABLE $TABLE_NAME (" +
                "$TOTAL_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$TOTAL_DATE TEXT not null," +
                "$TOTAL_INCOME INTEGER not null," +
                "$TOTAL_SPEND INTEGER not null," +
                "$TOTAL_MONEY INTEGER not null);"
        val DROP_TABLE_TOTAL = "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}
