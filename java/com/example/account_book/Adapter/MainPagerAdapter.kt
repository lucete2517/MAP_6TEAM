package com.example.account_book.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.account_book.Calendar.CalendarFragment
import com.example.account_book.Payment.PaymentFragment
import com.example.account_book.Setting.SettingFragment

import com.example.account_book.Statistics.StatisticsFragment

//-----------------------------------------------------------------------------------------------------------------------
//탭 전환을 위한 파일
//-----------------------------------------------------------------------------------------------------------------------
class MainPagerAdapter(FM: FragmentManager) : FragmentPagerAdapter(FM) {
    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> CalendarFragment()

            1 -> StatisticsFragment()

            2 -> PaymentFragment()

            3 -> SettingFragment()

            else -> CalendarFragment()
        }
    }

    override fun getCount(): Int {
        return 4
    }
}