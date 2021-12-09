package com.example.account_book.Data

//-----------------------------------------------------------------------------------------------------------------------
//카테고리 클래스 저장을 위한 class
//-----------------------------------------------------------------------------------------------------------------------
data class CategoryClass (val CategoryName : String, var Income : Int = 0, var Spend : Int = 0, val KeywordList : ArrayList<String> = ArrayList()) {
}
