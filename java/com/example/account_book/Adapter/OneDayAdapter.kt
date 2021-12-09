package com.example.account_book.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.example.account_book.Data.ExpenseClass
import com.example.account_book.R

//-----------------------------------------------------------------------------------------------------------------------
//사용 내역 띄우기를 위한 파일
//-----------------------------------------------------------------------------------------------------------------------
class OneDayAdapter(context : Context, info : ExpenseClass) : LinearLayout(context) {
    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.specific_item,this,true)

        val InfoCategory = findViewById<TextView>(R.id.InfoCategory)
        val InfoInout = findViewById<TextView>(R.id.InfoInout)
        val InfoAmount = findViewById<TextView>(R.id.InfoAmount)
        val InfoPayMent = findViewById<TextView>(R.id.InfoPayMent)
        val InfoContent = findViewById<TextView>(R.id.InfoContent)

        InfoCategory.text = info.Category
        InfoInout.text = info.Inout
        InfoAmount.text = info.Amount.toString()
        InfoPayMent.text= info.Payment
        InfoContent.text = info.Content
    }
}