package com.example.account_book

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.account_book.Adapter.MainPagerAdapter
import com.example.account_book.Data.CategoryClass
import com.example.account_book.Data.DateClass
import com.example.account_book.Data.PaymentMethodClass
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import java.util.*

//-----------------------------------------------------------------------------------------------------------------------
//시작 화면
//-----------------------------------------------------------------------------------------------------------------------
class MainActivity : AppCompatActivity() {
    companion object {
        //-----------------------------------------------------------------------------------------------------------------------
        //편의
        //-----------------------------------------------------------------------------------------------------------------------
        val MoneyFormat = java.text.DecimalFormat("#,###") //돈 입력

        //-----------------------------------------------------------------------------------------------------------------------
        //저장 관련
        //-----------------------------------------------------------------------------------------------------------------------
        val CategoryList : ArrayList<String> = arrayListOf() //카테고리 배열 (스피너를 위해)
        val CategoryclassList : ArrayList<CategoryClass> = ArrayList() //카테고리 저장
        val PaymentList : ArrayList<String> = arrayListOf("현금") //결제 방법 이름 배열 (스피너를 위해)
        val PaymentMethodClassList : ArrayList<PaymentMethodClass> = ArrayList() //결제 방법(이름+금액) 저장
        var PaymentTotalBalance : Int = 0 //결제 방법 총합 금액
        var CategoryclassListNum : Int = 0
        var PaymentMethodClassListNum : Int = 0

        val FullList : ArrayList<DateClass> = ArrayList() //특정 날짜에 대한 돈 수입/지출 내역
        val DateInfoMap : HashMap<Int,Int> = HashMap() //내역이 존재하는 <일자, 내역의 수>

        //-----------------------------------------------------------------------------------------------------------------------
        //통계 관련
        //-----------------------------------------------------------------------------------------------------------------------
        var CategoryTotalIncome : Int = 0
        var CategoryTotalSpend : Int = 0

        //-----------------------------------------------------------------------------------------------------------------------
        //달력 관련
        //-----------------------------------------------------------------------------------------------------------------------
        val TotalCalendarFragmentNum = 25
        var CalendarYearList = mutableListOf<Int>();
        var CalendarMonthList = mutableListOf<Int>();
        var CalendarViewFragmentList : ArrayList<Fragment> = ArrayList()

        //-----------------------------------------------------------------------------------------------------------------------
        //날짜 관련
        //-----------------------------------------------------------------------------------------------------------------------
        val YEAR = "YEAR"
        val MONTH = "MONTH"
        val DATE = "DATE"
        val PAGE= "PAGE"
        val MonthAndDate = "MM월 dd일"
        var Today = Calendar.getInstance()

        fun CalYear(cal : Calendar) : Int{
            return cal.get(Calendar.YEAR)
        }
        fun CalMonth(cal : Calendar) : Int{
            return cal.get(Calendar.MONTH)
        }
        fun CalDate(cal : Calendar) : Int{
            return cal.get(Calendar.DATE)
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //화면 출력
    //-----------------------------------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var RecordButton = findViewById<FloatingActionButton>(R.id.RecordButton) //오른쪽 아래 버튼

        CreateTabViewPager() //탭 생성 및 호출

        RecordButton.setOnClickListener{
            //-----------------------------------------------------------------------------------------------------------------------
            //현재 날짜 받아서 AddRecord(수입/지출 기록)로 넘기기
            //-----------------------------------------------------------------------------------------------------------------------
            val intent = Intent(this, AddRecord::class.java)
            intent.putExtra(YEAR, CalYear(Today))
            intent.putExtra(MONTH, CalMonth(Today))
            intent.putExtra(DATE, CalDate(Today))
            startActivity(intent)
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //탭 관련
    //-----------------------------------------------------------------------------------------------------------------------
    private fun CreateTabViewPager(){
        var MainMenuTabLayout = findViewById<TabLayout>(R.id.MainMenuTabLayout)
        var MainMenuViewPager = findViewById<ViewPager>(R.id.MainMenuViewPager)

        MainMenuTabLayout.addTab(MainMenuTabLayout.newTab().setIcon(R.drawable.tab_ic_calendar).setText("달력"))
        MainMenuTabLayout.addTab(MainMenuTabLayout.newTab().setIcon(R.drawable.tab_ic_statistics).setText("통계"))
        MainMenuTabLayout.addTab(MainMenuTabLayout.newTab().setIcon(R.drawable.tab_ic_list).setText("목록"))
        MainMenuTabLayout.addTab(MainMenuTabLayout.newTab().setIcon(R.drawable.tab_ic_setting).setText("설정"))

        MainMenuViewPager.adapter = MainPagerAdapter(supportFragmentManager)
        MainMenuViewPager.offscreenPageLimit = 3

        MainMenuViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(MainMenuTabLayout))
        MainMenuTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(p0: TabLayout.Tab) {
                MainMenuViewPager.currentItem = p0.position
            }
        })

        if(intent.hasExtra(PAGE))
            MainMenuViewPager.currentItem = intent.getIntExtra(PAGE,0)
    }
}