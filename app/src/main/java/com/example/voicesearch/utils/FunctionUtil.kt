package com.example.voicesearch.utils

import android.content.Context
import android.widget.Toast

fun Context.showToast(message:String){
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
}