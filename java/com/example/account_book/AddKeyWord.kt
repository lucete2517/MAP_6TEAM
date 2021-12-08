package com.example.account_book

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.account_book.Adapter.CategoryKeyWordAdapter
import com.example.account_book.Adapter.OneDayAdapter
import com.example.account_book.Data.DateClass
import com.example.account_book.MainActivity.Companion.CategoryInfoMap
import com.example.account_book.MainActivity.Companion.CategoryclassList

class AddKeyWord : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addkeyword)

        var CategoryKeyWordAddBut = findViewById<Button>(R.id.CategoryKeyWordAddBut)
        var CategoryKeyWordViewBut = findViewById<Button>(R.id.CategoryKeyWordViewBut)
        var CategoryListSpinner = findViewById<Spinner>(R.id.CategoryListSpinner)
        var KeyWordLayout = findViewById<LinearLayout>(R.id.KeyWordLayout)


        InitialSetting()

        CategoryKeyWordViewBut.setOnClickListener {
            val CategoryNum : Int = CategoryListSpinner.selectedItemPosition

            KeyWordLayout.removeAllViews()
            LoadKeyword(CategoryNum)
        }

        CategoryKeyWordAddBut.setOnClickListener {
            CategoryKeyWordAdd()
        }
    }

    private fun  CategoryKeyWordAdd() {
        var CategoryListSpinner = findViewById<Spinner>(R.id.CategoryListSpinner)
        var CategoryKeyWordField = findViewById<EditText>(R.id.CategoryKeyWordField)

        val Category = CategoryListSpinner.selectedItem.toString()

        val CategoryNum : Int = CategoryListSpinner.selectedItemPosition

        val KeyWordName = CategoryKeyWordField.text.toString()

        if(KeyWordName.length != 0) {
            if (CategoryInfoMap.containsKey(Category)) {
                CategoryclassList[CategoryNum].KeywordList.add(KeyWordName)
                CategoryInfoMap.put(Category, CategoryInfoMap.get(Category)!!.plus(1))
            } else {
                CategoryclassList[CategoryNum].KeywordList.add(KeyWordName)
                CategoryInfoMap.put(Category, 1)
            }

            finish()
        }

        else {
            Toast.makeText(
                applicationContext, "키워드를 1글자 이상 입력해주세요",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun InitialSetting() {
        var CategoryListSpinner = findViewById<Spinner>(R.id.CategoryListSpinner)

        CategoryListSpinner.adapter = ArrayAdapter(this,
            R.layout.support_simple_spinner_dropdown_item, MainActivity.CategoryList)

        val CategoryNum : Int = CategoryListSpinner.selectedItemPosition

        LoadKeyword(CategoryNum)
    }

    private fun LoadKeyword(Category: Int) {
        var KeyWordLayout = findViewById<LinearLayout>(R.id.KeyWordLayout)

        if (CategoryInfoMap.containsKey(CategoryclassList[Category].CategoryName)) {
            try {
                val KeyWordList : ArrayList<String> = CategoryclassList[Category].KeywordList
                for(KeyWord in KeyWordList){
                    val OneInfo = CategoryKeyWordAdapter(this, CategoryclassList[Category].CategoryName, KeyWord)
                    KeyWordLayout.addView(OneInfo)
                }
            } catch (e: Exception){
            }
        }
    }
}