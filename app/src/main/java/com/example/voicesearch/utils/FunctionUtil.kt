package com.example.voicesearch.utils

import android.content.Context
import android.widget.Toast
import java.util.*

fun Context.showToast(message:String){
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
}

val currentDate: Long
    get() {
        return Calendar.getInstance().time.time
    }