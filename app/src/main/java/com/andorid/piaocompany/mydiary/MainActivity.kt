package com.andorid.piaocompany.mydiary

import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.andorid.piaocompany.mydiary.Utility.TodayCalendar.cal
import com.andorid.piaocompany.mydiary.Utility.getTodayString
import com.andorid.piaocompany.mydiary.Utility.getTodayString2
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity(), View.OnClickListener
{
    private lateinit var tv_today : TextView
    private lateinit var et_todayStroy : EditText
    private lateinit var btn_send : Button
    private lateinit var btn_writeData : Button
    private lateinit var btn_readData : Button

    private lateinit var myRef : DatabaseReference
    private val db_firebase : FirebaseDatabase by lazy {
        FirebaseDatabase.getInstance()
    }
    private val today : String by lazy {
        getTodayString()
    }
    private lateinit var userId : String

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        userId = intent.getStringExtra("userID")
        tv_today = findViewById(R.id.tv_today)
        et_todayStroy = findViewById(R.id.et_todayStroy)
        btn_send = findViewById(R.id.btn_send)
        btn_writeData = findViewById(R.id.btn_writeData)
        btn_readData = findViewById(R.id.btn_readData)
        tv_today.text = today
        tv_today.setOnClickListener(this)
        btn_send.setOnClickListener(this)
        btn_readData.setOnClickListener(this)
    }

    override fun onClick(v: View?)
    {
        when(v?.id)
        {
            R.id.tv_today ->
                DatePickerDialog(this@MainActivity, DatePickerDialog.OnDateSetListener { it, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    tv_today.text = getTodayString()
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()

            R.id.btn_send -> {
                if(et_todayStroy.text.toString().length != 0) {
                    myRef = db_firebase.getReference()
                    myRef.child("users").child(userId).child(getTodayString2()).setValue(et_todayStroy.text.toString())
                            .addOnSuccessListener { et_todayStroy.setText("")}
                            .addOnFailureListener { Toast.makeText(this,"잠시 후 다시 이용해 주시기 바랍니다 :( ", Toast.LENGTH_SHORT). show()}
                } else {
                    Toast.makeText(this, "너의 하루를 말해줘", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.btn_readData -> {
                myRef = db_firebase.getReference()
                myRef.ref.child("users").child(userId).child(getTodayString2()).addValueEventListener(object : ValueEventListener{
                    override fun onCancelled(p0: DatabaseError) {
                        Log.d("GET_DATA", "Something went wrong when retrieving data!")
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        Log.d("GET_DATA", "This is the value of snapshot: " + p0?.toString()?: "FUCK")
                        if(p0?.value != null)
                        {
                            et_todayStroy.setText(p0?.value.toString())
                        }
                        else
                        {
                            et_todayStroy.setText("작성된 일기가 없습니다.")
                        }
                    }
                })
            }
        }
    }
}
