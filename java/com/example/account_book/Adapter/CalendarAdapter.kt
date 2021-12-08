package com.example.account_book.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.account_book.Calendar.OneDay
import com.example.account_book.Data.DateClass
import com.example.account_book.MainActivity
import com.example.account_book.MainActivity.Companion.CalendarYearList
import com.example.account_book.MainActivity.Companion.DATE
import com.example.account_book.MainActivity.Companion.MONTH
import com.example.account_book.MainActivity.Companion.TotalCalendarFragmentNum
import com.example.account_book.MainActivity.Companion.YEAR
import com.example.account_book.R
import java.util.*

//-----------------------------------------------------------------------------------------------------------------------
//일별 수입/지출/총합 표기
//-----------------------------------------------------------------------------------------------------------------------
class CalendarAdapter (val context : Context, val DateInfoList : ArrayList<DateClass>) : RecyclerView.Adapter<CalendarAdapter.Holder>(){

    //-----------------------------------------------------------------------------------------------------------------------
    //현재 날짜
    //-----------------------------------------------------------------------------------------------------------------------
    object GetTime{
        var cal = Calendar.getInstance()
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //레이아웃 선언부
    //-----------------------------------------------------------------------------------------------------------------------
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(context).inflate(R.layout.calendar_one_day,parent,false))
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //저장 얼마나 되었는지(반복문용)
    //-----------------------------------------------------------------------------------------------------------------------
    override fun getItemCount(): Int {
        return DateInfoList.size
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //쓸데 없는 재호출 막아주는
    //-----------------------------------------------------------------------------------------------------------------------
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(DateInfoList.get(position) , position)
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //일별 수입/지출/총합 색 및 일자 표기
    //-----------------------------------------------------------------------------------------------------------------------
    inner class Holder(view : View) : RecyclerView.ViewHolder(view){
        val DateLinearLayout = itemView?.findViewById<LinearLayout>(R.id.DateLinearLayout)
        val OneDay_Date = itemView?.findViewById<TextView>(R.id.OneDay_Date)
        val OneDay_Income = itemView?.findViewById<TextView>(R.id.OneDay_Income)
        val OneDay_Spend = itemView?.findViewById<TextView>(R.id.OneDay_Spend)
        val OneDay_Total = itemView?.findViewById<TextView>(R.id.OneDay_Total)

        fun bind(dateClass : DateClass, position : Int){
            var date : Int
            if(dateClass.Milli==0L)
                date = 0
            else {
                GetTime.cal.timeInMillis = (dateClass.Milli)
                date = GetTime.cal.get(Calendar.DATE)
            }
            if (OneDay_Date != null) {
                OneDay_Date.text = date.toString()
            }
            if(date==0)
                if (OneDay_Date != null) {
                    OneDay_Date.visibility = View.INVISIBLE
                }
            else{
                if (OneDay_Date != null) {
                    OneDay_Date.visibility = View.VISIBLE
                }
                when(position%7){
                    6 -> if (OneDay_Date != null) {
                        OneDay_Date.setTextColor(ContextCompat.getColor(context,R.color.blue))
                    }
                    0 -> if (OneDay_Date != null) {
                        OneDay_Date.setTextColor(ContextCompat.getColor(context,R.color.red))
                    }
                    else -> if (OneDay_Date != null) {
                        OneDay_Date.setTextColor(ContextCompat.getColor(context,R.color.black))
                    }
                }

                //-----------------------------------------------------------------------------------------------------------------------
                //일별 칸 클릭 시 OneDay 호출
                //-----------------------------------------------------------------------------------------------------------------------
                if (DateLinearLayout != null) {
                    DateLinearLayout.setOnClickListener {
                        val intent = Intent(context, OneDay::class.java)
                        intent.putExtra(YEAR, CalendarYearList[TotalCalendarFragmentNum/2])
                        intent.putExtra(MONTH, MainActivity.CalendarMonthList[TotalCalendarFragmentNum/2]-1)
                        intent.putExtra(DATE, date)

                        context.startActivity(intent)
                    }
                }
            }

            //-----------------------------------------------------------------------------------------------------------------------
            //일별 수입/지출/총합 표기
            //-----------------------------------------------------------------------------------------------------------------------
            if(dateClass.ExpenseList.size!=0){
                if (OneDay_Income != null) {
                    OneDay_Income.text = dateClass.Income.toString()
                }
                if (OneDay_Spend != null) {
                    OneDay_Spend.text = dateClass.Spend.toString()
                }
                if (OneDay_Total != null) {
                    OneDay_Total.text = dateClass.Total.toString()
                }
            }
        }
    }
}