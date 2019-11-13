package com.example.cgv.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.activity_ticket.view.*
import kotlin.math.abs


class SwipeLayoutCustom(context: Context, attributes: AttributeSet) : SwipeRefreshLayout(context, attributes) {
    private var mTouchSlop: Int = 0

    private var mPrevX: Float = 0.toFloat()

    init {
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    override fun canChildScrollUp(): Boolean {
        if (scrollView != null)
            return scrollView.canScrollVertically(-1)
        return false
    }

    @SuppressLint("Recycle")
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> mPrevX = MotionEvent.obtain(ev).x

            MotionEvent.ACTION_MOVE -> {
                val evX = ev.x
                val xDiff = abs(evX - mPrevX)

                if (xDiff > mTouchSlop) {
                    return false
                }
            }
        }

        return super.onInterceptTouchEvent(ev)

    }
}