package com.example.account_book.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.example.account_book.R

//-----------------------------------------------------------------------------------------------------------------------
//카테고리 키워드 나열을 위한 Adapter
//-----------------------------------------------------------------------------------------------------------------------
class CategoryKeyWordAdapter(context : Context, CategoryName : String, KeyWordName : String) : LinearLayout(context) {
    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.categorykeyword,this,true)

        var CategoryNameText = findViewById<TextView>(R.id.CategoryNameText)
        var KeyWordNameText = findViewById<TextView>(R.id.KeyWordNameText)

        CategoryNameText.text = CategoryName
        KeyWordNameText.text = KeyWordName
    }
}