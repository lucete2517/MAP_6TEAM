package com.example.account_book.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.example.account_book.MainActivity
import com.example.account_book.MainActivity.Companion.CategoryTotalIncome
import com.example.account_book.MainActivity.Companion.CategoryTotalSpend
import com.example.account_book.MainActivity.Companion.MoneyFormat
import com.example.account_book.R

class StatisticsAdapter(context : Context, Content: String, Income : Int, Spend : Int) : LinearLayout(context) {
    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.statistics_created, this, true)

        var CategoryContent = findViewById<TextView>(R.id.CategoryContent)
        var CategoryIncomeMoney = findViewById<TextView>(R.id.CategoryIncomeMoney)
        var CategorySpendMoney = findViewById<TextView>(R.id.CategorySpendMoney)
        var IncomePercent = findViewById<TextView>(R.id.IncomePercent)
        var SpendPercent = findViewById<TextView>(R.id.SpendPercent)
        var IncomeProgressBar = findViewById<ProgressBar>(R.id.IncomeProgressBar)
        var SpendProgressBar = findViewById<ProgressBar>(R.id.SpendProgressBar)
        var IncomePer = 0.0
        var SpendPer = 0.0

        if(CategoryTotalIncome == 0) {
            IncomePer = 0.0
        }
        else {
            IncomePer = ((Income / CategoryTotalIncome) * 100).toDouble()
        }

        if(CategoryTotalSpend == 0) {
            SpendPer = 0.0
        }
        else {
            SpendPer = ((Spend / CategoryTotalSpend) * 100).toDouble()
        }

        CategoryContent.text = Content
        CategoryIncomeMoney.text = MoneyFormat.format(Income)
        CategorySpendMoney.text = MoneyFormat.format(Spend)

        IncomePercent.text = IncomePer.toString()
        SpendPercent.text = SpendPer.toString()

        IncomeProgressBar.max = CategoryTotalIncome
        IncomeProgressBar.progress = Income

        SpendProgressBar.max = CategoryTotalSpend
        SpendProgressBar.progress = Spend
    }
}