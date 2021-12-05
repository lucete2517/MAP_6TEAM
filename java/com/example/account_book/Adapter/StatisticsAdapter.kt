package com.example.account_book.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.example.account_book.MainActivity.Companion.MoneyFormat
import com.example.account_book.R

class StatisticsAdapter(context : Context, Content: String, Income : Int, Spend : Int) : LinearLayout(context) {
    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.statistics_created, this, true)

        var CategoryContent = findViewById<TextView>(R.id.CategoryContent)
        var CategorySpendMoney = findViewById<TextView>(R.id.CategorySpendMoney)
        var CategoryIncomeMoney = findViewById<TextView>(R.id.CategoryIncomeMoney)
        var StatisticsProgressBar = findViewById<ProgressBar>(R.id.StatisticsProgressBar)

        CategoryContent.text = Content
        CategorySpendMoney.text = MoneyFormat.format(Spend)
        CategoryIncomeMoney.text = MoneyFormat.format(Income)
        StatisticsProgressBar.max = Income
        StatisticsProgressBar.progress = Spend
    }
}