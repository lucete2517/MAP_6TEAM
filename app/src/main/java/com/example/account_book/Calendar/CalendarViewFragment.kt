package com.example.account_book.Calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.account_book.Adapter.CalendarAdapter
import com.example.account_book.Data.AccountInfo
import com.example.account_book.Data.AccountInfoManager
import com.example.account_book.Data.DateClass
import com.example.account_book.MainActivity.Companion.DateInfoMap
import com.example.account_book.MainActivity.Companion.FullList
import com.example.account_book.MainActivity.Companion.MONTH
import com.example.account_book.MainActivity.Companion.YEAR
import com.example.account_book.R
import java.util.*

//-----------------------------------------------------------------------------------------------------------------------
//달력 틀 생성
//-----------------------------------------------------------------------------------------------------------------------
class CalendarViewFragment : Fragment() {
    //-----------------------------------------------------------------------------------------------------------------------
    //선언부
    //-----------------------------------------------------------------------------------------------------------------------
    private var MonthIncome = 0 //월별 수입
    private var MonthSpend = 0 //월별 지출

    private lateinit var CalendarMonthRecyclerView: RecyclerView
    private lateinit var CalendarIncomeTextView: TextView
    private lateinit var CalendarSpendTextView: TextView
    private lateinit var CalendarTotalTextView: TextView

    //-----------------------------------------------------------------------------------------------------------------------
    //Fragment에서 findViewById가 일반적으로 안되서 사용
    //-----------------------------------------------------------------------------------------------------------------------
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CalendarMonthRecyclerView = view.findViewById(R.id.CalendarMonthRecyclerView)
        CalendarIncomeTextView = view.findViewById(R.id.CalendarIncomeTextView)
        CalendarSpendTextView = view.findViewById(R.id.CalendarSpendTextView)
        CalendarTotalTextView = view.findViewById(R.id.CalendarTotalTextView)
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //초기화
    //-----------------------------------------------------------------------------------------------------------------------
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val YEAR = requireArguments().getInt(YEAR)
        val MONTH = requireArguments().getInt(MONTH)
        MonthIncome = 0
        MonthSpend = 0
        CreateMonthViewDynamically(YEAR,MONTH)

        super.onActivityCreated(savedInstanceState)
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //레이아웃 선언부
    //-----------------------------------------------------------------------------------------------------------------------
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.calendar,container,false)
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //달력 생성
    //-----------------------------------------------------------------------------------------------------------------------
    @SuppressLint("Range", "UseRequireInsteadOfGet")
    private fun CreateMonthViewDynamically(YEAR : Int, MONTH : Int){
        //-----------------------------------------------------------------------------------------------------------------------
        //일별 기록을 위한 DateClass 생성
        //-----------------------------------------------------------------------------------------------------------------------
        val calendar = Calendar.getInstance()
        calendar.set(YEAR, MONTH, 1)
        val DateClassList = ArrayList<DateClass>()
        for (i in 1..calendar.get(Calendar.DAY_OF_WEEK) - 1) {
            DateClassList.add(DateClass())
        }

        for (i in 1..calendar.getActualMaximum(java.util.Calendar.DATE)) {
            calendar.set(YEAR, MONTH, i)
            var date8 = YEAR.toString()
            when{
                MONTH < 10 -> date8 += ("0"+MONTH.toString()) //1~9로 저장시 7자리로 되어 8자리를 위해 앞에 0 추가
                else -> date8 += MONTH.toString()
            }
            when{
                i < 10 -> date8 += ("0"+i.toString()) //1~9로 저장시 7자리로 되어 8자리를 위해 앞에 0 추가
                else -> date8 += i.toString()
            }

            val IntDate8 = date8.toInt()

            //-----------------------------------------------------------------------------------------------------------------------
            //생성하면서 수입/지출 더함
            //-----------------------------------------------------------------------------------------------------------------------
            if(DateInfoMap.containsKey(IntDate8)){
                for(j in FullList){
                    if(j.Date8 == IntDate8){
                        DateClassList.add(DateClass(Date8 = IntDate8, Milli = calendar.timeInMillis, Income = j.Income, Spend = j.Spend, Total = j.Total, ExpenseList = j.ExpenseList))
                        MonthIncome += j.Income
                        MonthSpend += j.Spend
                        break;
                    }
                }
            } else{
                DateClassList.add(DateClass(Date8 = IntDate8, Milli = calendar.timeInMillis))
            }

//            val columns = arrayOf(
//                AccountInfo.TotalMoneyInfo.TOTAL_DATE
//            )
//            val selection = "date = ?"
//            val selectionArgs = arrayOf(date8)
//            val orderBy = ""
//            val cursor = AccountInfoManager.getInstance(this.requireActivity().applicationContext, "usertotalmoneyrecord")
//                .query(3, columns, selection, selectionArgs, orderBy)
//
//            if(cursor.count > 0){
//                cursor.moveToFirst()
//                val income = cursor.getInt(cursor.getColumnIndex(AccountInfo.TotalMoneyInfo.TOTAL_INCOME))
//                val spend = cursor.getInt(cursor.getColumnIndex(AccountInfo.TotalMoneyInfo.TOTAL_SPEND))
//                val totalMoney = cursor.getInt(cursor.getColumnIndex(AccountInfo.TotalMoneyInfo.TOTAL_MONEY))
//                DateClassList.add(DateClass(Date8 = IntDate8, Milli = calendar.timeInMillis, Income = income,
//                                                Spend = spend, Total = totalMoney))
//            }
//            else{
//                DateClassList.add(DateClass(Date8 = IntDate8, Milli = calendar.timeInMillis))
//            }
        }

        //-----------------------------------------------------------------------------------------------------------------------
        //일별 수입/지출/총합 작성을 위한 호출
        //-----------------------------------------------------------------------------------------------------------------------
        CalendarMonthRecyclerView.layoutManager = GridLayoutManager(context, 7)
        CalendarMonthRecyclerView.adapter = CalendarAdapter(this.requireContext(),DateClassList)

        //-----------------------------------------------------------------------------------------------------------------------
        //월별 수입/지출/총합 출력
        //-----------------------------------------------------------------------------------------------------------------------
        CalendarIncomeTextView.text = MonthIncome.toString()
        CalendarSpendTextView.text = MonthSpend.toString()
        CalendarTotalTextView.text = (MonthIncome - MonthSpend).toString()
    }

}
