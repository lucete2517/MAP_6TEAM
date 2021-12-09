package com.example.account_book.Setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.account_book.MainActivity
import com.example.account_book.R
import android.widget.ArrayAdapter
import com.example.account_book.AddKeyWord
import com.example.account_book.AddRecord
import com.example.account_book.MainActivity.Companion.CategoryList

//-----------------------------------------------------------------------------------------------------------------------
//설정 탭 (현재는 카테고리 키워드 추가 메뉴만 있음)
//-----------------------------------------------------------------------------------------------------------------------
class SettingFragment : Fragment() {
    private lateinit var KeyWordAddBut : Button
    private lateinit var intent : Intent

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.setting_frag,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        KeyWordAddBut = view.findViewById(R.id.KeyWordAddBut)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        KeyWordAddBut.setOnClickListener {
            intent = Intent(getActivity(), AddKeyWord::class.java)
            startActivity(intent)
        }
    }
}