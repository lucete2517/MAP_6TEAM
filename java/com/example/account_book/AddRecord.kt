package com.example.account_book

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.account_book.Calendar.CalendarViewFragment
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
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import android.app.AlarmManager
import android.app.NotificationManager

import android.app.PendingIntent
import android.content.Context
import com.example.account_book.Data.*
import com.example.account_book.MainActivity.Companion.CategoryclassList


//-----------------------------------------------------------------------------------------------------------------------
//사용 내역 저장
//-----------------------------------------------------------------------------------------------------------------------
class AddRecord : AppCompatActivity() {
    //-----------------------------------------------------------------------------------------------------------------------
    //선언부
    //-----------------------------------------------------------------------------------------------------------------------

    lateinit var alarmManager : AlarmManager
    lateinit var notificationManager : NotificationManager

    private var MoneyRecordString = ""
    private var photoURI: String? = null
    private val SettingDate = Calendar.getInstance() //일단 오늘 날짜를 가져왔다가, 초기화 시에 값을 받아와서 변환
    var spendOrIncome = 2 //1. 수입, 2. 지출
    var alertNum = 0

    lateinit var DateRecordField : TextView
    lateinit var inoutSpinner : Spinner
    lateinit var alertSpinner : Spinner
    lateinit var btnPhoto : ImageButton
    lateinit var ConfirmButton : Button


    val CAMERA = arrayOf(Manifest.permission.CAMERA)
    val STORAGE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    val CAMERA_CODE = 98
    val STORAGE_CODE = 99

    //-----------------------------------------------------------------------------------------------------------------------
    //레이아웃 선언부
    //-----------------------------------------------------------------------------------------------------------------------
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addrecord)

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager


        val MoneyRecordField = findViewById<EditText>(R.id.MoneyRecordField) //쓴 돈 적는 EditText
        val CancelButton = findViewById<ImageButton>(R.id.CancelButton) //취소 버튼 (X)

        val CategorySpinner = findViewById<Spinner>(R.id.CategorySpinner) //카테고리
        val PaymentMethodSpinner = findViewById<Spinner>(R.id.PaymentMethodSpinner) //현금, 카드, 계좌이체

        CategorySpinner.adapter = ArrayAdapter(this,
            R.layout.support_simple_spinner_dropdown_item, CategoryList) //MainActivity

        PaymentMethodSpinner.adapter = ArrayAdapter(this,
            R.layout.support_simple_spinner_dropdown_item, PaymentList) //MainActivity

        btnPhoto = findViewById<ImageButton>(R.id.btnPhoto)
        alertSpinner = findViewById<Spinner>(R.id.alertSpinner)
        inoutSpinner = findViewById<Spinner>(R.id.inoutSpinner)
        DateRecordField = findViewById<TextView>(R.id.DateRecordField) //기록 날짜
        ConfirmButton = findViewById<Button>(R.id.ConfirmButton) //저장 버튼

        InitialSetting()
        setupSpinnerAlert()
        setupSpinnerHandler()

        btnPhoto.setOnClickListener {
            getAlbum()
        }

        //-----------------------------------------------------------------------------------------------------------------------
        //확인 버튼
        //-----------------------------------------------------------------------------------------------------------------------
        ConfirmButton.setOnClickListener {
            val MoneyString = MoneyRecordField.text.toString().replace(",", "")

            when {
                MoneyString.length < 3 -> Toast.makeText(
                    applicationContext, "금액을 100원 이상 입력해 주세요.",
                    Toast.LENGTH_SHORT).show()
                else -> {
                    AddSpendList()
                    setAlarm()
                    finish()
                }
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
                if (!TextUtils.isEmpty(s.toString()) && s.toString() != MoneyRecordString) {
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
    private fun InitialSetting() { //초기화

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
        //날짜 설정
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //저장 부분
    //-----------------------------------------------------------------------------------------------------------------------
    private fun AddSpendList(){
        //수입, 지출
        val CategorySpinner = findViewById<Spinner>(R.id.CategorySpinner) //식비, 교통, 마트, 편의점 등
        val PaymentMethodSpinner = findViewById<Spinner>(R.id.PaymentMethodSpinner) //현금, 카드, 계좌이체
        val ContentRecordField = findViewById<EditText>(R.id.ContentRecordField) //메모
        val MoneyRecordField = findViewById<EditText>(R.id.MoneyRecordField) //돈

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

        val CategoryNum : Int = CategorySpinner.selectedItemPosition

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
                    DateInfoMap[TimeInLength8] = DateInfoMap[TimeInLength8]!!.plus(1)
                }
            }
        }
        else { //해당 일에 추가된 날짜가 없을 경우
            if(Expense.Inout == "수입") {
                FullList.add(DateClass(TimeInLength8,TimeinMillis,Income = Money, Total = Money , ExpenseList = arrayListOf<ExpenseClass>(Expense)))
                if(PaymentMethodNum != 0){
                    PaymentMethodClassList[PaymentMethodNum-1].Balance += Money
                }
                CategoryclassList[CategoryNum].Income += Money
            }
            else if(Expense.Inout == "지출") {
                FullList.add(DateClass(TimeInLength8,TimeinMillis,Spend = Money, Total = -Money , ExpenseList = arrayListOf<ExpenseClass>(Expense)))
                if(PaymentMethodNum != 0){
                    PaymentMethodClassList[PaymentMethodNum-1].Balance -= Money
                }
                CategoryclassList[CategoryNum].Spend += Money
            }
            DateInfoMap[TimeInLength8] = 1
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
                break
            }
        }

        when(inoutSpinner.selectedItem){
            "수입" -> {
                spendOrIncome = 1
            }
            "지출" -> {
                spendOrIncome = 2
            }
        }

        //-----------------------------------------------------------------------------------------------------------------------
        //DB 저장
        //-----------------------------------------------------------------------------------------------------------------------
        val values = ContentValues()
        if(spendOrIncome == 1){ //Income
            values.put(AccountInfo.IncomeInfo.INCOME_DATE, TimeInLength8.toString())
            values.put(AccountInfo.IncomeInfo.INCOME_PAY_METHOD, PaymentMethodSpinner.selectedItem.toString())
            values.put(AccountInfo.IncomeInfo.INCOME_PRICE, Money)
            values.put(AccountInfo.IncomeInfo.INCOME_CATEGORY, Category)
            values.put(AccountInfo.IncomeInfo.INCOME_MEMO, Content)
            values.put(AccountInfo.IncomeInfo.INCOME_PHOTO, photoURI)
        }
        else{ //Spend
            values.put(AccountInfo.SpendInfo.SPEND_DATE,TimeInLength8.toString())
            values.put(AccountInfo.SpendInfo.SPEND_PAY_METHOD, PaymentMethodSpinner.selectedItem.toString())
            values.put(AccountInfo.SpendInfo.SPEND_PRICE, Money)
            values.put(AccountInfo.SpendInfo.SPEND_CATEGORY, Category)
            values.put(AccountInfo.SpendInfo.SPEND_MEMO, Content)
            values.put(AccountInfo.SpendInfo.SPEND_PHOTO, photoURI)
            //사진 URI 저장하고 불러오는 함수 생성해야 함
        }
        val insertedID = AccountInfoManager.getInstance(applicationContext, "useraccount").insert(spendOrIncome, values)

        updateTotalAccount(TimeInLength8, Money)
    }

    @SuppressLint("Range")
    private fun updateTotalAccount(date: Int, money: Int) {
        val columns = arrayOf(
            AccountInfo.TotalMoneyInfo.TOTAL_DATE,
            AccountInfo.TotalMoneyInfo.TOTAL_MONEY,
            AccountInfo.TotalMoneyInfo.TOTAL_INCOME,
            AccountInfo.TotalMoneyInfo.TOTAL_SPEND
        )
        val selection = "date = ?"
        val selectionArgs = arrayOf(date.toString())
        val orderBy = ""

        val cursor = AccountInfoManager.getInstance(applicationContext, "useraccount")
            .query(3, columns, selection, selectionArgs, orderBy)

        if(cursor.moveToFirst() && !cursor.isNull(cursor.getColumnIndex(AccountInfo.TotalMoneyInfo.TOTAL_DATE))){ //DB에 이미 저장한 이력이 있으면 값을 UPDATE
            val totalValues = ContentValues()
            val totalMoney = cursor.getInt(cursor.getColumnIndex(AccountInfo.TotalMoneyInfo.TOTAL_MONEY))
            if(spendOrIncome == 1){
                val income = cursor.getInt(cursor.getColumnIndex(AccountInfo.TotalMoneyInfo.TOTAL_INCOME))
                totalValues.put(AccountInfo.TotalMoneyInfo.TOTAL_INCOME, income + money)
                totalValues.put(AccountInfo.TotalMoneyInfo.TOTAL_MONEY, totalMoney + money)
            }
            else{
                val spend = cursor.getInt(cursor.getColumnIndex(AccountInfo.TotalMoneyInfo.TOTAL_SPEND))
                totalValues.put(AccountInfo.TotalMoneyInfo.TOTAL_SPEND, spend + money)
                totalValues.put(AccountInfo.TotalMoneyInfo.TOTAL_MONEY, totalMoney - money)
            }
            AccountInfoManager.getInstance(applicationContext, "useraccount")
                .update(3, totalValues, selection, selectionArgs)
        }
        else{ //이력이 없으면 INSERT
            val totalValues = ContentValues()
            totalValues.put(AccountInfo.TotalMoneyInfo.TOTAL_DATE, date.toString())
            if(spendOrIncome == 1){ //Income일 때
                totalValues.put(AccountInfo.TotalMoneyInfo.TOTAL_INCOME, money)
                totalValues.put(AccountInfo.TotalMoneyInfo.TOTAL_SPEND, 0)
                totalValues.put(AccountInfo.TotalMoneyInfo.TOTAL_MONEY, money)
            }
            else{ //Spend일 때
                totalValues.put(AccountInfo.TotalMoneyInfo.TOTAL_INCOME, 0)
                totalValues.put(AccountInfo.TotalMoneyInfo.TOTAL_SPEND, money)
                totalValues.put(AccountInfo.TotalMoneyInfo.TOTAL_MONEY, -money)
            }

            AccountInfoManager.getInstance(applicationContext, "useraccount").insert(3, totalValues)
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //카메라, 저장 등 권한 승인
    //-----------------------------------------------------------------------------------------------------------------------
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            CAMERA_CODE -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "카메라 권한 승인 필요", Toast.LENGTH_LONG).show()
                    }
                }
            }

            STORAGE_CODE -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "저장소 권한 승인 필요", Toast.LENGTH_LONG).show()
                        //finish() 앱을 종료함
                    }
                }
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //권한 요청하기 위한 함수
    //-----------------------------------------------------------------------------------------------------------------------
    private fun checkPermission(permissions: Array<out String>, type: Int): Boolean
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, permissions, type)
                    return false
                }
            }
        }

        return true
    }

    fun callCamera() {
        if (checkPermission(CAMERA, CAMERA_CODE) && checkPermission(STORAGE, STORAGE_CODE)) {
            val itt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(itt, CAMERA_CODE)
        }
    }

    private fun setupSpinnerAlert(){
        val alertTime = resources.getStringArray(R.array.alertSpinner)
        val alertAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, alertTime)
        alertSpinner.adapter = alertAdapter
    }
    
    //-----------------------------------------------------------------------------------------------------------------------
    //알람 스피너 설정
    //-----------------------------------------------------------------------------------------------------------------------
    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("ResourceType", "SimpleDateFormat")
    private fun setupSpinnerHandler(){
        alertSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                alertNum = position
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //알람 기능
    //-----------------------------------------------------------------------------------------------------------------------
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setAlarm() {
        //AlarmReceiver에 값 전달
        val receiverIntent = Intent(this@AddRecord, AlarmReceiver::class.java)

        val alarmMsg: String
        var alarmDate : LocalDateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val tz: TimeZone = SettingDate.timeZone
        val zid = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tz.toZoneId()
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val alarmDateTime = LocalDateTime.ofInstant(SettingDate.toInstant(), zid)
        val moneyRecordField = findViewById<EditText>(R.id.MoneyRecordField)

        when(alertNum){
            0->{

            }
            1->{
                alarmDate = alarmDateTime.minusDays(3)
            }
            2->{
                alarmDate = alarmDateTime.minusDays(2)
            }
            3->{
                alarmDate = alarmDateTime.minusDays(1)
            }
            4->{
                alarmDate = alarmDateTime.minusHours(3)
            }
            5->{
                alarmDate = alarmDateTime.plusMinutes(1)
            }
        }
        if(alertNum != 0 && alarmDate > LocalDateTime.now()){
            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.YEAR, alarmDate.year)
                set(Calendar.MONTH, alarmDate.monthValue - 1)
                set(Calendar.DAY_OF_MONTH, alarmDate.dayOfMonth)
                set(Calendar.HOUR_OF_DAY,alarmDate.hour)
                set(Calendar.MINUTE, alarmDate.minute)
                set(Calendar.SECOND, alarmDate.second)
            }

            alarmMsg = "${alarmDateTime.year}년 ${alarmDateTime.monthValue}월 ${alarmDateTime.dayOfMonth}일 ${moneyRecordField.text}원 지출 예정입니다"
            receiverIntent.putExtra("Message", alarmMsg)
            Log.e("Alarm : ", alarmMsg)

            Toast.makeText(applicationContext, "알림이 설정되었습니다", Toast.LENGTH_SHORT).show()

            val pendingIntent = PendingIntent.getBroadcast(this@AddRecord, 0, receiverIntent, 0)

            alarmManager.setExact(AlarmManager.RTC, calendar.timeInMillis, pendingIntent)
        }
        else if(alertNum != 0 && alarmDate < LocalDateTime.now()){
            Toast.makeText(applicationContext, "현재시간 이전으로 알림을 설정하셨습니다", Toast.LENGTH_SHORT).show()
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //사진 저장
    //-----------------------------------------------------------------------------------------------------------------------
    private fun saveFile(fileName: String, mimeType: String, bitmap: Bitmap): Uri?
    {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        if (uri != null) {
            val scriptor = contentResolver.openFileDescriptor(uri, "w")

            if (scriptor != null) {
                val fos = FileOutputStream(scriptor.fileDescriptor)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.close()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    values.clear()
                    values.put(MediaStore.Images.Media.IS_PENDING, 0)
                    contentResolver.update(uri, values, null, null)
                }
            }
        }

        return uri
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //result_ok를 받을 시 카메라, 저장소 연계
    //-----------------------------------------------------------------------------------------------------------------------
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_CODE -> {
                    if (data?.extras?.get("data") != null) {
                        val img = data.extras?.get("data") as Bitmap
                        val uri = saveFile(randomFileName(), "image/jpg", img)
                        //addPhotoView.setImageURI(uri)
                    }
                }
                STORAGE_CODE -> {
                    val uri = data?.data
                    photoURI = uri.toString()
                    //addPhotoView.setImageURI(uri)
                }
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //사진 파일 이름 저장
    //-----------------------------------------------------------------------------------------------------------------------
    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    fun randomFileName(): String {
        return android.icu.text.SimpleDateFormat("yyyy-MM-ddHH:mm:ss").format(System.currentTimeMillis())
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //갤러리에 접근하는 함수
    //-----------------------------------------------------------------------------------------------------------------------
    fun getAlbum() {
        if (checkPermission(STORAGE, STORAGE_CODE)) {
            val itt = Intent(Intent.ACTION_PICK)
            itt.type = MediaStore.Images.Media.CONTENT_TYPE
            startActivityForResult(itt, STORAGE_CODE)
        }
    }


}
