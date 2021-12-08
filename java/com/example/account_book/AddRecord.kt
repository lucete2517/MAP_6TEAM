package com.example.account_book

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.account_book.Calendar.CalendarViewFragment
import com.example.account_book.Data.DateClass
import com.example.account_book.Data.ExpenseClass
import com.example.account_book.MainActivity.Companion.CalDate
import com.example.account_book.MainActivity.Companion.CalMonth
import com.example.account_book.MainActivity.Companion.CalYear
import com.example.account_book.MainActivity.Companion.CalendarMonthList
import com.example.account_book.MainActivity.Companion.CalendarViewFragmentList
import com.example.account_book.MainActivity.Companion.CalendarYearList
import com.example.account_book.MainActivity.Companion.CategoryList
import com.example.account_book.MainActivity.Companion.DATE
import com.example.account_book.MainActivity.Companion.DateInfoMap
import com.example.account_book.MainActivity.Companion.FullList
import com.example.account_book.MainActivity.Companion.MONTH
import com.example.account_book.MainActivity.Companion.MonthAndDate
import com.example.account_book.MainActivity.Companion.PaymentList
import com.example.account_book.MainActivity.Companion.PaymentMethodClassList
import com.example.account_book.MainActivity.Companion.Today
import com.example.account_book.MainActivity.Companion.TotalCalendarFragmentNum
import com.example.account_book.MainActivity.Companion.YEAR
import java.text.SimpleDateFormat
import java.util.*

//-----------------------------------------------------------------------------------------------------------------------
//사용 내역 저장
//-----------------------------------------------------------------------------------------------------------------------
class AddRecord : AppCompatActivity() {
    //-----------------------------------------------------------------------------------------------------------------------
    //선언부
    //-----------------------------------------------------------------------------------------------------------------------
    private var MoneyRecordString = ""
    private val SettingDate = Calendar.getInstance() //날짜 가져오는거

    //-----------------------------------------------------------------------------------------------------------------------
    //레이아웃 선언부
    //-----------------------------------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addrecord)

        var ConfirmButton = findViewById<Button>(R.id.ConfirmButton)
        var MoneyRecordField = findViewById<EditText>(R.id.MoneyRecordField)
        var CancelButton = findViewById<Button>(R.id.CancelButton)

        InitialSetting()

        //-----------------------------------------------------------------------------------------------------------------------
        //확인 버튼
        //-----------------------------------------------------------------------------------------------------------------------
        ConfirmButton.setOnClickListener {
            val MoneyString = MoneyRecordField.text.toString().replace(",", "")

            when {
                MoneyString.length < 3 -> Toast.makeText(
                    applicationContext, "금액을 100원 이상 입력해 주세요.",
                    Toast.LENGTH_SHORT).show()
                else -> AddSpendList()
            }
        }

        //-----------------------------------------------------------------------------------------------------------------------
        //취소 버튼
        //-----------------------------------------------------------------------------------------------------------------------
        CancelButton.setOnClickListener {
            finish()
        }

        //-----------------------------------------------------------------------------------------------------------------------
        //돈 입력시 000마다 , 입력
        //-----------------------------------------------------------------------------------------------------------------------
        MoneyRecordField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(MoneyRecordString)) {
                    MoneyRecordString =
                        MainActivity.MoneyFormat.format((s.toString().replace(",", "")).toDouble())
                    MoneyRecordField.setText(MoneyRecordString)
                    MoneyRecordField.setSelection(MoneyRecordString.length)
                }
            }

        })
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //날짜 및 레이아웃 설정
    //-----------------------------------------------------------------------------------------------------------------------
    private fun InitialSetting() {
        var DateRecordField = findViewById<TextView>(R.id.DateRecordField)
        var CategorySpinner = findViewById<Spinner>(R.id.CategorySpinner)
        var PaymentMethodSpinner = findViewById<Spinner>(R.id.PaymentMethodSpinner)


        CategorySpinner.adapter = ArrayAdapter(this,
            R.layout.support_simple_spinner_dropdown_item, CategoryList)

        PaymentMethodSpinner.adapter = ArrayAdapter(this,
            R.layout.support_simple_spinner_dropdown_item, PaymentList)

        var year : Int = CalYear(SettingDate)
        var month : Int = CalMonth(SettingDate)
        var date : Int = CalDate(SettingDate)

        if(intent.hasExtra(YEAR)&&intent.hasExtra(MONTH)&&intent.hasExtra(DATE)){
            year = intent.getIntExtra(YEAR,year)
            month = intent.getIntExtra(MONTH,month)
            date = intent.getIntExtra(DATE,date)
            SettingDate.set(year,month,date)
        }
        DateRecordField.text = SimpleDateFormat(MonthAndDate).format(SettingDate.time)
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //저장 부분
    //-----------------------------------------------------------------------------------------------------------------------
    private fun AddSpendList(){
        var inoutSpinner = findViewById<Spinner>(R.id.inoutSpinner)
        var CategorySpinner = findViewById<Spinner>(R.id.CategorySpinner)
        var PaymentMethodSpinner = findViewById<Spinner>(R.id.PaymentMethodSpinner)
        var ContentRecordField = findViewById<EditText>(R.id.ContentRecordField)
        var MoneyRecordField = findViewById<EditText>(R.id.MoneyRecordField)

        Today = Calendar.getInstance()
        
        val year : String = CalYear(SettingDate).toString()
        var month : String = CalMonth(SettingDate).toString()
        var date : String =  CalDate(SettingDate).toString()

        //-----------------------------------------------------------------------------------------------------------------------
        //레이아웃에서 정보 받기
        //-----------------------------------------------------------------------------------------------------------------------
        if(CalMonth(SettingDate)<10)
            month = "0"+CalMonth(SettingDate)
        if(CalDate(SettingDate)<10)
            date =  "0"+CalDate(SettingDate)
        val TimeInLength8 = Integer.parseInt(year + month + date)

        val TimeinMillis = Today.timeInMillis

        val Category = CategorySpinner.selectedItem.toString()

        val PaymentMethodNum : Int = PaymentMethodSpinner.selectedItemPosition

        val Money = MoneyRecordField.text.toString().replace(",","").toInt()

        val PaymentMethod = PaymentMethodSpinner.selectedItem.toString()

        val inout = inoutSpinner.selectedItem.toString()

        val Content = ContentRecordField.text.toString()

        val Expense = ExpenseClass(TimeInLength8, TimeinMillis, inout, Category, Money, PaymentMethod, Content)

        if(DateInfoMap.containsKey(TimeInLength8)){//해당 일에 이미 추가된 내역이 있다면
            for(i in FullList.indices){ //전체 리스트에서 해당일을 찾아 해당일의 내역에 추가
                if(FullList[i].Date8==TimeInLength8){
                    if(Expense.Inout.equals("수입")) {
                        FullList[i].Income += Money
                    }
                    else if(Expense.Inout.equals("지출")) {
                        FullList[i].Spend += Money
                    }
                    FullList[i].Total = FullList[i].Income - FullList[i].Spend
                    FullList[i].ExpenseList.add(Expense)
                    DateInfoMap.put(TimeInLength8, DateInfoMap.get(TimeInLength8)!!.plus(1))
                }
            }
        }
        else { //해당 일에 추가된 날짜가 없을 경우
            if(Expense.Inout.equals("수입")) {
                FullList.add(DateClass(TimeInLength8,TimeinMillis,Income = Money, Total = Money , ExpenseList = arrayListOf<ExpenseClass>(Expense)))
                if(PaymentMethodNum != 0){
                    PaymentMethodClassList[PaymentMethodNum-1].Balance += Money
                }
            }
            else if(Expense.Inout.equals("지출")) {
                FullList.add(DateClass(TimeInLength8,TimeinMillis,Spend = Money, Total = -Money , ExpenseList = arrayListOf<ExpenseClass>(Expense)))
                if(PaymentMethodNum != 0){
                    PaymentMethodClassList[PaymentMethodNum-1].Balance -= Money
                }
            }
            DateInfoMap.put(TimeInLength8,1)
        }

        for(i in 0 until TotalCalendarFragmentNum){
            if(CalendarYearList[i]==year.toInt()&& CalendarMonthList[i]-1==month.toInt()){
                val Frags = CalendarViewFragment()
                Frags.apply {
                    arguments = Bundle().apply {
                        putInt(MainActivity.YEAR,year.toInt())
                        putInt(MainActivity.MONTH,month.toInt())
                    }
                }
                CalendarViewFragmentList[i]=Frags
                break;
            }
        }
        finish()
    }
}