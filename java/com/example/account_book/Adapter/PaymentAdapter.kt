package com.example.account_book.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.example.account_book.R

//-----------------------------------------------------------------------------------------------------------------------
//결제 방법 띄우기를 위한 파일
//-----------------------------------------------------------------------------------------------------------------------
class PaymentAdapter(context : Context, PaymentName: String, Balance : Int) : LinearLayout(context){
    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.payment_created,this,true)

        var SubPaymentName = findViewById<TextView>(R.id.SubPaymentName)
        var SubPaymentMoney = findViewById<TextView>(R.id.SubPaymentMoney)

        SubPaymentName.text = PaymentName
        SubPaymentMoney.text = Balance.toString()
    }
}