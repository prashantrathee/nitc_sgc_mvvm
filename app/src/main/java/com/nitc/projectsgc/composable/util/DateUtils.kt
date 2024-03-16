package com.nitc.projectsgc.composable.util

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class DateUtils {

    fun convertMillisToLocalDate(millis: Long) : LocalDate {
        return Instant
            .ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    private fun convertMillisToLocalDateWithFormatter(date: LocalDate, dateTimeFormatter: DateTimeFormatter) : LocalDate {
        //Convert the date to a long in millis using a dateformmater
        val dateInMillis = LocalDate.parse(date.format(dateTimeFormatter), dateTimeFormatter)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        //Convert the millis to a localDate object
        return Instant
            .ofEpochMilli(dateInMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }


    fun reverseDateString(inputDate:String):String{
        var numbers = inputDate.split('-')
        numbers = numbers.reversed()
        return numbers.joinToString('-'.toString())
    }
    fun dateToString(millis:Long): String {
        val localDate = convertMillisToLocalDate(millis)
        val dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM, yyyy", Locale.getDefault())
        val dateInMillis = convertMillisToLocalDateWithFormatter(localDate, dateFormatter)
        Log.d("dateToString",dateInMillis.toString())
//        return SimpleDateFormat("dd-MM-yyyy",Locale.getDefault()).format(SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(dateInMillis))
        val gotDate = reverseDateString(dateInMillis.toString())
        Log.d("dateToString","gotdate : $gotDate")
        return gotDate
    }
}