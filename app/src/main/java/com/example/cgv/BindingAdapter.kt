package com.example.cgv

import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import java.text.DecimalFormat

object BindingAdapterTextView {
    @JvmStatic
    @BindingAdapter("bindPrice")
    fun setPrice( tv :AppCompatTextView, price:Long) {
        var s = String.format("%,d", price)
        s+=" đ"
        tv.text = s
    }
}