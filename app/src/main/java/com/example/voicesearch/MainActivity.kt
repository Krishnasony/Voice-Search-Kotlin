package com.example.voicesearch

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognizerIntent
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.voicesearch.room.entity.RecentSearch
import com.example.voicesearch.utils.currentDate
import com.example.voicesearch.utils.showToast
import com.example.voicesearch.viewModel.RecentSearchViewModel
import com.example.voicesearch.voiceHelper.SpeechCallback
import com.example.voicesearch.voiceHelper.VoiceRecognizerHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private val mViewModel:RecentSearchViewModel by viewModel()
    private lateinit var mVoiceRecognizerHelper : VoiceRecognizerHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initVoiceRecognizationDialog()
        onClick()

    }

    private fun onClick() {
        floating_action_btn.setOnClickListener {
            if(hasAudioRecordingPermission()){
                mVoiceRecognizerHelper.startSpeech()
            } else {
                requestAudioRecordingPermission()
            }
        }

        searchView.setOnSearchButtonClickListener(View.OnClickListener {
            val searchText = searchView.text.toString()
            if (searchText.isNotEmpty()){
                GlobalScope.launch(Dispatchers.IO) {
                    mViewModel.addRecentSearch(recentSearch = RecentSearch(search = searchText,searchDate = currentDate))
                }
            }else{
                this.showToast("Say something on mic for search!")
            }

        })

    }

    private fun initVoiceRecognizationDialog() {
        mVoiceRecognizerHelper = VoiceRecognizerHelper(this, object : SpeechCallback {
            override fun onSpeechStart() {
                floating_action_btn.setImageResource(R.drawable.ic_mic)
                voice_state.text = getString(R.string.listening)
            }

            override fun onSpeechStop() {
                floating_action_btn.setImageResource(R.drawable.ic_mic_none)
            }

            override fun onSpeechResult(result: ArrayList<String>?) {
                Log.d(TAG, "Speech Result : ${result.toString()}")
                floating_action_btn.setImageResource(R.drawable.ic_mic_none)
                searchView.setText(result!![0])
            }

            override fun onSpeechError(message: String) {
                Log.d(TAG, "Speech Error : $message")
                floating_action_btn.setImageResource(R.drawable.ic_mic_off)
                voice_state.text = STOP

            }
        })
    }
    private fun hasAudioRecordingPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestAudioRecordingPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            REQUEST_CODE_AUDIO_PERMISSION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {

        if(requestCode == REQUEST_CODE_AUDIO_PERMISSION){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mVoiceRecognizerHelper.startSpeech()
            } else{
                floating_action_btn.setImageResource(R.drawable.ic_mic_off)

                if(isPermanentlyDenied()){
                    showPermissionDialog()
                } else {
                    requestAudioRecordingPermission()
                }
            }
        }
    }

    private fun isPermanentlyDenied(): Boolean {
        return !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)
    }

    private fun showPermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle(TITLE)
            .setMessage(MESSAGE)
            .setPositiveButton(POSITIVE_BUTTON_TEXT) { _, _ ->
                goToAppSetting()
            }
            .setNegativeButton(NEGATIVE_BUTTON_TEXT) { _, _ ->
                this.showToast("Cancelled")
                finish()
            }
            .create()
            .show()
    }

    private fun goToAppSetting() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            REQUEST_CODE_SPEECH_INPUT -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    val recognizedSTT =
                        data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    searchView.setText(recognizedSTT!![0])
                }
            }
        }

    }

    override fun onStop() {
        super.onStop()
        mVoiceRecognizerHelper.stopSpeech()
    }

    override fun onRestart() {
        super.onRestart()
        if(hasAudioRecordingPermission()){
            mVoiceRecognizerHelper.startSpeech()
        }
    }

    companion object{
        private var TAG = MainActivity::class.java.simpleName
        const val REQUEST_CODE_AUDIO_PERMISSION = 100
        const val REQUEST_CODE_SPEECH_INPUT = 101
        const val TITLE = "Required Permission"
        const val MESSAGE = "Please grant Microphone permission to record audio"
        const val POSITIVE_BUTTON_TEXT = "Go To Setting"
        const val NEGATIVE_BUTTON_TEXT = "Cancel"
        const val STOP = "Search"
    }
}
