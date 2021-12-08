package com.example.account_book.Data

//-----------------------------------------------------------------------------------------------------------------------
//일별 기록 변수
//Date8 = 20211101 같은 년도월일 || Milli = 구분하는 ID || Income = 수입 || Spend = 지출 || Total = 수입 - 지출 || ExpenseList = ExpendseClass 배열
//-----------------------------------------------------------------------------------------------------------------------
data class DateClass (var Date8 : Int = 0,
                      var Milli : Long = 0,
                      var Income : Int = 0,
                      var Spend : Int = 0,
                      var Total : Int = 0,
                      val ExpenseList : ArrayList<ExpenseClass> = ArrayList())