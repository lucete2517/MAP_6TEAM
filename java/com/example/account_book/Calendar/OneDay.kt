package com.example.account_book.Calendar

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.account_book.Adapter.OneDayAdapter
import com.example.account_book.AddRecord
import com.example.account_book.Data.AccountInfo
import com.example.account_book.Data.AccountInfoManager
import com.example.account_book.Data.DateClass
import com.example.account_book.Data.ExpenseClass
import com.example.account_book.MainActivity
import com.example.account_book.R

//-----------------------------------------------------------------------------------------------------------------------
//일별 칸을 클릭 시 나오는 창
//-----------------------------------------------------------------------------------------------------------------------
class OneDay : AppCompatActivity() {
    //-----------------------------------------------------------------------------------------------------------------------
    //선언부
    //-----------------------------------------------------------------------------------------------------------------------
    val SpecificDayInfoList : ArrayList<OneDayAdapter> = ArrayList() //일별 기록 표기한 레이아웃 배열
    var year = 0
    var month = 0
    var date = 0
    var date8 = 0

    //-----------------------------------------------------------------------------------------------------------------------
    //레이아웃 선언부
    //-----------------------------------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.specificday)

        val SpecificDayAddRecordButton = findViewById<Button>(R.id.SpecificDayAddRecordButton)

        LoadSpecificDayInfo(GetDateInString())

        //-----------------------------------------------------------------------------------------------------------------------
        //생성 버튼 누를 시 AddRecord 호출
        //-----------------------------------------------------------------------------------------------------------------------
        SpecificDayAddRecordButton.setOnClickListener {
            val intent = Intent(this, AddRecord::class.java)
            intent.putExtra(MainActivity.YEAR, year)
            intent.putExtra(MainActivity.MONTH, month)
            intent.putExtra(MainActivity.DATE, date)

            startActivity(intent)
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //다른 창 이동 후 돌아왔을때 재부팅
    //-----------------------------------------------------------------------------------------------------------------------
    override fun onResume() {
        super.onResume()
        var DayInfoLayout = findViewById<LinearLayout>(R.id.DayInfoLayout)

        DayInfoLayout.removeAllViews()
        LoadSpecificDayInfo(date8)
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //캘린더 뷰 로드
    //-----------------------------------------------------------------------------------------------------------------------
    private fun LoadNewCalendarView(){
        for(i in 0 until MainActivity.TotalCalendarFragmentNum){
            if(MainActivity.CalendarYearList[i]==year&& MainActivity.CalendarMonthList[i]-1==month){
                val Frags = CalendarViewFragment()
                Frags.apply {
                    arguments = Bundle().apply {
                        putInt(MainActivity.YEAR,year)
                        putInt(MainActivity.MONTH,month)
                    }
                }
                MainActivity.CalendarViewFragmentList[i]=Frags
                break
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //사용 내역 출력
    //-----------------------------------------------------------------------------------------------------------------------
    @SuppressLint("Range")
    private fun LoadSpecificDayInfo(Date: Int) {
        getIncomeRecord(1, Date)
        getSpendRecord(2, Date)
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //지출 내역 DB 연동 후 불러오기
    //-----------------------------------------------------------------------------------------------------------------------
    @SuppressLint("Range")
    private fun getSpendRecord(tableNum: Int, Date: Int) {
        val dayInfoLayout = findViewById<LinearLayout>(R.id.DayInfoLayout)

        val columns = arrayOf(
            AccountInfo.SpendInfo.SPEND_ID,
            AccountInfo.SpendInfo.SPEND_PAY_METHOD,
            AccountInfo.SpendInfo.SPEND_CATEGORY,
            AccountInfo.SpendInfo.SPEND_PRICE,
            AccountInfo.SpendInfo.SPEND_MEMO
        )
        val selection = "date = ?"
        val selectionArgs = arrayOf(date8.toString())
        val orderBy = ""

        val cursor = AccountInfoManager.getInstance(applicationContext, "useraccount")
            .query(tableNum, columns, selection, selectionArgs, orderBy)


        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(AccountInfo.SpendInfo.SPEND_ID))
            val payMethod = cursor.getString(cursor.getColumnIndex(AccountInfo.SpendInfo.SPEND_PAY_METHOD))
            val category = cursor.getInt(cursor.getColumnIndex(AccountInfo.SpendInfo.SPEND_CATEGORY))
            val price = cursor.getString(cursor.getColumnIndex(AccountInfo.SpendInfo.SPEND_PRICE))
            val memo = cursor.getString(cursor.getColumnIndex(AccountInfo.SpendInfo.SPEND_MEMO))
            val cursorInfo = ExpenseClass(
                Date8 = Date,
                Milli = id.toLong(),
                Inout = "지출",
                Category = category.toString(),
                Amount = price.toInt(),
                Payment = payMethod.toString(),
                Content = memo.toString()
            )
            val oneInfo = OneDayAdapter(this, cursorInfo)
            dayInfoLayout.addView(oneInfo)
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //수입 내역 DB 연동 후 불러오기
    //-----------------------------------------------------------------------------------------------------------------------
    @SuppressLint("Range")
    private fun getIncomeRecord(tableNum: Int, Date: Int) {
        val dayInfoLayout = findViewById<LinearLayout>(R.id.DayInfoLayout)

        val columns = arrayOf(
            AccountInfo.IncomeInfo.INCOME_ID,
            AccountInfo.IncomeInfo.INCOME_PAY_METHOD,
            AccountInfo.IncomeInfo.INCOME_CATEGORY,
            AccountInfo.IncomeInfo.INCOME_PRICE,
            AccountInfo.IncomeInfo.INCOME_MEMO
        )
        val selection = "date = ?"
        val selectionArgs = arrayOf(date8.toString())
        val orderBy = ""

        val cursor = AccountInfoManager.getInstance(applicationContext, "useraccount")
            .query(tableNum, columns, selection, selectionArgs, orderBy)


        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(AccountInfo.IncomeInfo.INCOME_ID))
            val payMethod = cursor.getString(cursor.getColumnIndex(AccountInfo.IncomeInfo.INCOME_PAY_METHOD))
            val category = cursor.getInt(cursor.getColumnIndex(AccountInfo.IncomeInfo.INCOME_CATEGORY))
            val price = cursor.getString(cursor.getColumnIndex(AccountInfo.IncomeInfo.INCOME_PRICE))
            val memo = cursor.getString(cursor.getColumnIndex(AccountInfo.IncomeInfo.INCOME_MEMO))
            val cursorInfo = ExpenseClass(
                Date8 = Date,
                Milli = id.toLong(),
                Inout = "수입",
                Category = category.toString(),
                Amount = price.toInt(),
                Payment = payMethod.toString(),
                Content = memo.toString()
            )
            val oneInfo = OneDayAdapter(this, cursorInfo)
            dayInfoLayout.addView(oneInfo)
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //사용 내역 삭제 (DB 연동 후 삭제 방식에 관해 문제가 생겨 일단 연기)
    //-----------------------------------------------------------------------------------------------------------------------
    private fun removeOneInfo(Date : Int, dateInfo : DateClass, expenseClass: ExpenseClass){
        var DayInfoLayout = findViewById<LinearLayout>(R.id.DayInfoLayout)

        if (dateInfo.ExpenseList.size == 1) {
            MainActivity.FullList.remove(dateInfo)
            MainActivity.DateInfoMap.remove(Date)
        } else {
            dateInfo.ExpenseList.remove(expenseClass)
            if(expenseClass.Inout == "수입")
                dateInfo.Income -= expenseClass.Amount
            else if(expenseClass.Inout == "지출")
                dateInfo.Spend -= expenseClass.Amount
            dateInfo.Total -= expenseClass.Amount
        }
        DayInfoLayout.removeAllViews() //삭제 후 초기화 재부팅
        LoadSpecificDayInfo(Date)

        LoadNewCalendarView()
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //저장을 위한 날짜 호출 및 날짜 띄움
    //-----------------------------------------------------------------------------------------------------------------------
    private fun GetDateInString(): Int {
        var date8string = ""

        var InfoDate = findViewById<TextView>(R.id.InfoDate)

        year = if (intent.hasExtra(MainActivity.YEAR))
            intent.getIntExtra(MainActivity.YEAR, MainActivity.CalYear(MainActivity.Today))
        else
            MainActivity.CalYear(MainActivity.Today)
        month = if (intent.hasExtra(MainActivity.MONTH))
            intent.getIntExtra(MainActivity.MONTH, MainActivity.CalMonth(MainActivity.Today))
        else
            MainActivity.CalMonth(MainActivity.Today)
        date = if (intent.hasExtra(MainActivity.DATE))
            intent.getIntExtra(MainActivity.DATE, MainActivity.CalDate(MainActivity.Today))
        else
            MainActivity.CalDate(MainActivity.Today)

        val monthString = if (month >= 10) //AddRecord를 가기 위한 변수
            month.toString()
        else
            "0$month"

        val monthS = if ((month + 1) >= 10) //날짜 보여주기 위한 변수 (숫자가 1 낮게 나와서 더해줌)
            (month + 1).toString()
        else
            "0" + (month + 1).toString()

        val dateString = if (date >= 10)
            date.toString()
        else
            "0$date"

        date8 = (year.toString() + monthString + dateString).toInt()
        date8string = (year.toString() + "년 " + monthS + "월 " + dateString + "일") //날짜 띄우기

        InfoDate.text = date8string

        return date8
    }
}