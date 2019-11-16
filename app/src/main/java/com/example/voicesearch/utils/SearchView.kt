package com.example.voicesearch.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import com.example.voicesearch.R

class SearchView : AppCompatEditText {

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {

        this.isLongClickable = false

        this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_accent, 0,0 , 0)
        this.hint = "Search"
        this.setPadding(
            getPx(context, 16),
            getPx(context, 16),
            getPx(context, 16),
            getPx(context, 16)
        )
        this.imeOptions = EditorInfo.IME_ACTION_SEARCH
        this.inputType = EditorInfo.TYPE_TEXT_FLAG_CAP_SENTENCES
        this.setTextColor(Color.parseColor("#30384B"))
        this.setHintTextColor(Color.parseColor("#30384B"))


        this.setBackgroundResource(R.drawable.search_view_bg)

        val f = TextView::class.java.getDeclaredField("mCursorDrawableRes")
        f.isAccessible = true
        f.set(this, R.drawable.color_cursor)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.elevation = getPx(context, 4).toFloat()
        }

        this.setOnTouchListener(onTouchListener)

        this.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (onSearchButtonClickListener != null) {
                    this@SearchView.clearFocus()
                    hideKeyboard(context = context)
                    onSearchButtonClickListener!!.onClick(this)
                }
            }
            true
        }

        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                handler?.postDelayed({
                    if (onSearchButtonClickListener != null) {
                        onSearchButtonClickListener!!.onClick(this@SearchView)
                    }
                }, 600)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                handler?.removeCallbacksAndMessages(null)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun getPx(dp: Int): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).toInt()

    private var onSearchButtonClickListener: View.OnClickListener? = null

    fun setOnSearchButtonClickListener(onClearButtonClickListener: View.OnClickListener) {
        this.onSearchButtonClickListener = onClearButtonClickListener
    }

    private fun getPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    private val onTouchListener = OnTouchListener { _, event ->
        if (event?.action == MotionEvent.ACTION_UP) {
            if (this@SearchView.width - event.x < getPx(40)) {
                if (onSearchButtonClickListener != null) {
                    hideKeyboard(context = context)
                    this@SearchView.clearFocus()
                    onSearchButtonClickListener!!.onClick(this)
                    if (handler != null) handler.removeCallbacksAndMessages(null)
                }
                return@OnTouchListener true
            }
        }
        false
    }

    fun hideKeyboard(context: Context) {

        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        val token = this.windowToken
        imm.hideSoftInputFromWindow(token, 0)
    }

    fun performSearchButtonClick() {

        if (onSearchButtonClickListener != null) {
            this@SearchView.clearFocus()
            hideKeyboard(context = context)
            onSearchButtonClickListener!!.onClick(this)
        }
    }

    fun onDetach() {
        onSearchButtonClickListener = null
        if (handler != null) handler.removeCallbacksAndMessages(null)
    }

    fun clearText() {
        super.setText(null)
        if (handler != null) handler.removeCallbacksAndMessages(null)
        performSearchButtonClick()
    }
}
