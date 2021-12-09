package com.example.account_book.Calendar

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.account_book.MainActivity.Companion.CalendarMonthList
import com.example.account_book.MainActivity.Companion.CalendarViewFragmentList
import com.example.account_book.MainActivity.Companion.CalendarYearList
import com.example.account_book.MainActivity.Companion.MONTH
import com.example.account_book.MainActivity.Companion.Today
import com.example.account_book.MainActivity.Companion.TotalCalendarFragmentNum
import com.example.account_book.MainActivity.Companion.YEAR
import com.example.account_book.R
import java.util.*

//-----------------------------------------------------------------------------------------------------------------------
//달력 위의 (버튼 날짜 버튼) 부분
//-----------------------------------------------------------------------------------------------------------------------
class CalendarFragment : Fragment() {
    //-----------------------------------------------------------------------------------------------------------------------
    //선언부
    //-----------------------------------------------------------------------------------------------------------------------

    val SELECT_MONTH_INTENT = 1000
    var ChangeCalendarSynchronization = 1

    private lateinit var PrevMonthBut: Button
    private lateinit var NextMonthBut: Button
    private lateinit var CalendarYearAndMonth: TextView

    //-----------------------------------------------------------------------------------------------------------------------
    //Fragment에서 findViewById가 일반적으로 안되서 사용
    //-----------------------------------------------------------------------------------------------------------------------
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PrevMonthBut = view.findViewById(R.id.PrevMonthBut)
        NextMonthBut = view.findViewById(R.id.NextMonthBut)
        CalendarYearAndMonth = view.findViewById(R.id.CalendarYearAndMonth)
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //동작 관련
    //-----------------------------------------------------------------------------------------------------------------------
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        ReStartCalendarList(Today.get(Calendar.YEAR),Today.get(Calendar.MONTH))

        //이전 달 보여주기
        PrevMonthBut.setOnClickListener {
            CalendarViewPagerControl("Prev")
        }
        //다음 달 보여주기
        NextMonthBut.setOnClickListener {
            CalendarViewPagerControl("Next")
        }
        super.onActivityCreated(savedInstanceState)
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //레이아웃 선언
    //-----------------------------------------------------------------------------------------------------------------------
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.calendar_frag, container, false)
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //다른 창 이동 후 돌아왔을때 재부팅
    //-----------------------------------------------------------------------------------------------------------------------
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SELECT_MONTH_INTENT) {
            if (resultCode == Activity.RESULT_OK) {
                if(data!=null&&data.hasExtra(YEAR) && data.hasExtra(MONTH)){
                    ReStartCalendarList(data.getIntExtra(YEAR,Today.get(Calendar.YEAR)),data.getIntExtra(MONTH,Today.get(
                        Calendar.MONTH)))
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        requireFragmentManager().beginTransaction().replace(R.id.CalendarViewFrame,CalendarViewFragmentList[TotalCalendarFragmentNum/2]).commit()
        SetCalendarYearAndMonth(TotalCalendarFragmentNum/2)
        ChangeCalendarSynchronization = 1
        if(CalendarViewFragmentList.size!=TotalCalendarFragmentNum||CalendarYearList.size!=TotalCalendarFragmentNum||CalendarMonthList.size!=TotalCalendarFragmentNum){
            ReStartCalendarList(Today.get(Calendar.YEAR),Today.get(Calendar.MONTH))
        }
    }

    private fun ReStartCalendarList(year: Int , month : Int){
        CalendarYearList.clear()
        CalendarMonthList.clear()
        CalendarViewFragmentList.clear()
        CreateCalendarFragmentList(year,month)
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //버튼 가운데 0000년 00월
    //-----------------------------------------------------------------------------------------------------------------------
    private fun SetCalendarYearAndMonth(index: Int) {
        CalendarYearAndMonth.text = String.format("${CalendarYearList[index]}년 ${CalendarMonthList[index]}월")

        val Date : String = when(CalendarMonthList[index]){
            in 1..9 -> CalendarYearList[index].toString() + "0" + CalendarMonthList[index]
            else -> CalendarYearList[index].toString() + CalendarMonthList[index]
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //버튼 동작 부분 함수
    //-----------------------------------------------------------------------------------------------------------------------
    private fun CalendarViewPagerControl(s: String) {
        if(ChangeCalendarSynchronization==1){
            ChangeCalendarSynchronization=0
            when (s) {
                "Prev" -> {
                    requireFragmentManager().beginTransaction().replace(R.id.CalendarViewFrame,CalendarViewFragmentList[11]).commit()
                    SetCalendarYearAndMonth(11)
                    AddCalendarFragment(s)
                }
                "Next" -> {
                    requireFragmentManager().beginTransaction().replace(R.id.CalendarViewFrame,CalendarViewFragmentList[13]).commit()
                    SetCalendarYearAndMonth(13)
                    AddCalendarFragment(s)
                }
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //달력 생성
    //-----------------------------------------------------------------------------------------------------------------------
    private fun CreateCalendarFragmentList(year: Int , month : Int) {
        for (i in 0 until TotalCalendarFragmentNum) {
            var settingYear = year
            var setttingMonth = month + i - 12

            while (setttingMonth < 0 || setttingMonth > 11) {
                if (setttingMonth < 0) {
                    setttingMonth += 12
                    settingYear--
                }
                if (setttingMonth > 11) {
                    setttingMonth -= 12
                    settingYear++
                }
            }
            CalendarYearList.add(settingYear)
            CalendarMonthList.add(setttingMonth + 1)
            val Frags = CalendarViewFragment()
            Frags.apply {
                arguments = Bundle().apply {
                    putInt(YEAR, settingYear)
                    putInt(MONTH, setttingMonth)
                }
            }
            CalendarViewFragmentList.add(Frags)
        }
        requireFragmentManager().beginTransaction().replace(R.id.CalendarViewFrame,CalendarViewFragmentList[TotalCalendarFragmentNum/2]).commit()
        SetCalendarYearAndMonth(TotalCalendarFragmentNum/2)
    }

    private fun AddCalendarFragment(s: String) {
        when (s) {
            "Prev" -> {
                var PrevMonth = (CalendarMonthList[0])-1
                var PrevYear = CalendarYearList[0]
                if(PrevMonth==0){
                    PrevMonth=12
                    PrevYear--
                }

                val Frags = CalendarViewFragment()
                Frags.apply {
                    arguments = Bundle().apply {
                        putInt(YEAR, PrevYear)
                        putInt(MONTH, PrevMonth)
                    }
                }
                CalendarYearList.add(0,PrevYear)
                CalendarMonthList.add(0,PrevMonth)
                CalendarViewFragmentList.add(0,Frags)
                CalendarViewFragmentList.removeAt(CalendarViewFragmentList.size-1)
                CalendarMonthList.removeAt(CalendarMonthList.size-1)
                CalendarYearList.removeAt(CalendarYearList.size-1)
            }
            "Next" -> {
                var NextMonth = (CalendarMonthList[CalendarMonthList.size-1])+1
                var NextYear = CalendarYearList[CalendarYearList.size-1]
                if(NextMonth==13){
                    NextMonth=1
                    NextYear++
                }

                val Frags = CalendarViewFragment()
                Frags.apply {
                    arguments = Bundle().apply {
                        putInt(YEAR, NextYear)
                        putInt(MONTH, NextMonth-1)
                    }
                }
                CalendarYearList.add(NextYear)
                CalendarMonthList.add(NextMonth)
                CalendarViewFragmentList.add(Frags)
                CalendarViewFragmentList.removeAt(0)
                CalendarMonthList.removeAt(0)
                CalendarYearList.removeAt(0)
            }
        }
        ChangeCalendarSynchronization =1
    }
}