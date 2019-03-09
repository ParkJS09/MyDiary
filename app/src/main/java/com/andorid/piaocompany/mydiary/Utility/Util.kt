package com.andorid.piaocompany.mydiary.Utility

import com.andorid.piaocompany.mydiary.Utility.TodayCalendar.cal
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

object TodayCalendar {
    val cal : Calendar = Calendar.getInstance()
}


fun getTodayString() : String {
    var sdf: SimpleDateFormat = SimpleDateFormat("yyyy년 MM월 dd일")
    return sdf.format(cal.time)
}

fun getTodayString2() : String {
    var sdf: SimpleDateFormat = SimpleDateFormat("yyyyMMdd")
    return sdf.format(cal.time)
}


fun createDB(){

}

