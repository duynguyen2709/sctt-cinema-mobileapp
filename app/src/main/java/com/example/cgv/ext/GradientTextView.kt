package com.example.cgv.ext

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet
import android.widget.TextView


class GradientTextView : TextView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        //Setting the gradient if layout is changed
        if (changed) {
            paint.shader = LinearGradient(
                0f, 0f, width.toFloat(), height.toFloat(),
                Color.parseColor("#6799d4"),
                Color.parseColor("#4252a0"),
                Shader.TileMode.CLAMP
            )
        }
    }
}