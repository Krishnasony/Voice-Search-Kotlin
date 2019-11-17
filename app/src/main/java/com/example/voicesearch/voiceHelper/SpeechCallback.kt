package com.example.voicesearch.voiceHelper

import java.util.ArrayList

interface SpeechCallback {
    fun onSpeechStart()
    fun onSpeechStop()
    fun onSpeechResult(result: ArrayList<String>?)
    fun onSpeechError(message : String)
}