package com.example.account_book.Data

//-----------------------------------------------------------------------------------------------------------------------
//일별 사용내역(수입/지출 내역)
//Date8 = 20211101 같은 년도월일 || Milli = 구분하는 ID || Inout = 수입/지출 || Category = 분류 || Amount = 금액 || Payment = 지출방법(현금, 카드) || content = 내용(메모)
//-----------------------------------------------------------------------------------------------------------------------
data class ExpenseClass(
    val Date8: Int,
    var Milli: Long = 0,
    var Inout: String,
    var Category: String,
    var Amount: Int,
    var Payment: String,
    var Content: String?
)