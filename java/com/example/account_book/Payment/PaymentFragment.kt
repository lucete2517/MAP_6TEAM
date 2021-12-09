package com.example.account_book.Payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.account_book.Adapter.PaymentAdapter
import com.example.account_book.Data.PaymentMethodClass
import com.example.account_book.MainActivity
import com.example.account_book.MainActivity.Companion.PaymentList
import com.example.account_book.MainActivity.Companion.PaymentMethodClassList
import com.example.account_book.MainActivity.Companion.PaymentTotalBalance
import com.example.account_book.R
import java.text.SimpleDateFormat

//-----------------------------------------------------------------------------------------------------------------------
//결제 방법 관리 탭
//-----------------------------------------------------------------------------------------------------------------------
class PaymentFragment : Fragment() {
    //-----------------------------------------------------------------------------------------------------------------------
    //선언부
    //-----------------------------------------------------------------------------------------------------------------------
    var SubPaymentEditText = View.GONE

    private lateinit var AddPaymentBut : Button
    private lateinit var SubPaymentLayout : LinearLayout
    private lateinit var CreateSubBut : Button
    private lateinit var SubPaymentNameEditText : EditText
    private lateinit var SubPaymentMoneyEditText : EditText
    private lateinit var PaymentLayout : LinearLayout
    private lateinit var PaymentTotalMoney : TextView

    //-----------------------------------------------------------------------------------------------------------------------
    //레이아웃 선언부
    //-----------------------------------------------------------------------------------------------------------------------
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.payment_frag,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AddPaymentBut = view.findViewById(R.id.AddPaymentBut)
        SubPaymentLayout = view.findViewById(R.id.SubPaymentLayout)
        CreateSubBut = view.findViewById(R.id.CreateSubBut)
        SubPaymentNameEditText = view.findViewById(R.id.SubPaymentNameEditText)
        SubPaymentMoneyEditText = view.findViewById(R.id.SubPaymentMoneyEditText)
        PaymentLayout = view.findViewById(R.id.PaymentLayout)
        PaymentTotalMoney = view.findViewById(R.id.PaymentTotalMoney)

    }

    //-----------------------------------------------------------------------------------------------------------------------
    //작동 부분
    //-----------------------------------------------------------------------------------------------------------------------
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        AddPaymentBut.setOnClickListener {
            AddPaymentFun()
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //결제 방법 추가
    //-----------------------------------------------------------------------------------------------------------------------
    private fun AddPaymentFun(){
        if(SubPaymentEditText== View.GONE){
            SubPaymentLayout.visibility = View.VISIBLE
            SubPaymentEditText = View.VISIBLE
            CreateSubBut.setOnClickListener {
                val NameText = SubPaymentNameEditText.text.toString()
                val MoneyText = SubPaymentMoneyEditText.text.toString()

                if(NameText.length < 1 || MoneyText.length < 1){
                    Toast.makeText(context,"내역을 1글자 이상, 금액을 0원 이상 입력해주세요.", Toast.LENGTH_SHORT).show()
                }

                else{
                    SubPaymentLayout.visibility = View.GONE
                    SubPaymentEditText = View.GONE
                    AddPaymentContent(SubPaymentNameEditText.text.toString(),SubPaymentMoneyEditText.text.toString())
                    SubPaymentNameEditText.setText("")
                    SubPaymentMoneyEditText.setText("")
                }
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //결제 방법 저장
    //-----------------------------------------------------------------------------------------------------------------------
    private fun AddPaymentContent(name : String, balance : String){
        val IntBalance = Integer.parseInt(balance.replace(",",""))
        var Content = PaymentMethodClass(PaymentMethodName = name, Balance = IntBalance)
        PaymentMethodClassList.add(Content)
        PaymentList.add(name)
        CreateCompletedView(PaymentAdapter(requireContext(), name, IntBalance))

        PaymentTotalBalance += IntBalance
        PaymentTotalMoney.setText(PaymentTotalBalance.toString())
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //저장 시 레이아웃에 추가
    //-----------------------------------------------------------------------------------------------------------------------
    private fun CreateCompletedView(layout : LinearLayout){
        PaymentLayout.addView(layout)
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //다른 창 갔다가 올때나 초기에 띄우는 용도
    //-----------------------------------------------------------------------------------------------------------------------
    override fun onResume() {
        super.onResume()
        SettingPaymentView()
    }

    private fun SettingPaymentView(){
        PaymentTotalBalance = 0
        PaymentMethodClassList.forEach {
            PaymentTotalBalance += it.Balance
        }

        PaymentTotalMoney.setText(PaymentTotalBalance.toString())
        PaymentLayout.removeAllViews()

        for(i in PaymentMethodClassList){
            CreateCompletedView(PaymentAdapter(requireContext(), i.PaymentMethodName, i.Balance))
        }
    }
}
