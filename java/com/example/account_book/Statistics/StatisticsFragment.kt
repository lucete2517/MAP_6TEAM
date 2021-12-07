package com.example.account_book.Statistics

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.account_book.Adapter.StatisticsAdapter
import com.example.account_book.Data.CategoryClass
import com.example.account_book.MainActivity
import com.example.account_book.MainActivity.Companion.CategoryList
import com.example.account_book.MainActivity.Companion.CategoryTotalIncome
import com.example.account_book.MainActivity.Companion.CategoryTotalSpend
import com.example.account_book.MainActivity.Companion.CategoryclassList
import com.example.account_book.MainActivity.Companion.CategoryclassListNum
import com.example.account_book.MainActivity.Companion.MoneyFormat
import com.example.account_book.R
import java.text.SimpleDateFormat

class StatisticsFragment : Fragment() {
    var CategoryEditText = View.GONE

    private lateinit var TotalIncomeTextView : TextView
    private lateinit var TotalSpendTextView : TextView
    private lateinit var CategoryStatisticsLayout : LinearLayout
    private lateinit var CategoryLayout : LinearLayout
    private lateinit var CategoryContentEditText : EditText
    private lateinit var CreateSubBut : Button
    private lateinit var AddCategoryBut : Button

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        AddStatisticsContent("기타")
        SettingStatisticsView()

        AddCategoryBut.setOnClickListener {
            AddCategoryFun()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.statistics_frag,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        TotalIncomeTextView = view.findViewById(R.id.TotalIncomeTextView)
        TotalSpendTextView = view.findViewById(R.id.TotalSpendTextView)
        CategoryStatisticsLayout = view.findViewById(R.id.CategoryStatisticsLayout)
        CategoryLayout = view.findViewById(R.id.CategoryLayout)
        CategoryContentEditText = view.findViewById(R.id.CategoryContentEditText)
        CreateSubBut = view.findViewById(R.id.CreateSubBut)
        AddCategoryBut = view.findViewById(R.id.AddCategoryBut)
    }

    override fun onResume() {
        super.onResume()
        SettingStatisticsView()
    }

    private fun CreateCompletedView(layout : LinearLayout){
        CategoryStatisticsLayout.addView(layout)
    }

    private fun SettingStatisticsView(){
        CategoryTotalIncome = 0
        CategoryTotalSpend = 0
        CategoryclassList.forEach {
            CategoryTotalIncome += it.Income
            CategoryTotalSpend += it.Spend
        }
        TotalIncomeTextView.setText(CategoryTotalIncome.toString())
        TotalSpendTextView.setText(CategoryTotalSpend.toString())

        CategoryStatisticsLayout.removeAllViews()

        for(i in CategoryclassList){
            CreateCompletedView(StatisticsAdapter(requireContext(),i.CategoryName,i.Income ,i.Spend))
        }
    }

    private fun AddCategoryFun(){
        if(CategoryEditText== View.GONE) {
            CategoryLayout.visibility = View.VISIBLE
            CategoryEditText = View.VISIBLE
            CreateSubBut.setOnClickListener {
                val ContentText = CategoryContentEditText.text.toString()
                if (ContentText.length < 1) {
                    Toast.makeText(context, "카레고리를 1글자 이상 입력해주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    CategoryLayout.visibility = View.GONE
                    CategoryEditText = View.GONE
                    AddStatisticsContent(CategoryContentEditText.text.toString())
                    CategoryContentEditText.setText("")
                }
            }
        }
    }

    private fun AddStatisticsContent(content : String){
        var Content = CategoryClass(CategoryName = content)
        CategoryclassList.add(Content)
        CategoryList.add(content)
        CreateCompletedView(StatisticsAdapter(requireContext(),content, 0, 0))
        CategoryclassListNum++
    }
}
