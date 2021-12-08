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
import com.example.account_book.MyReceiver
import com.example.account_book.AddRecord
import com.example.account_book.MainActivity.Companion.CategoryList

class SettingFragment : Fragment() {
    private lateinit var msgtest : Button
    private lateinit var intent : Intent

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.setting_frag,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        msgtest = view.findViewById(R.id.msgtest)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        msgtest.setOnClickListener {
            intent = Intent(getActivity(), MyReceiver::class.java)
            startActivity(intent)
        }
    }
}