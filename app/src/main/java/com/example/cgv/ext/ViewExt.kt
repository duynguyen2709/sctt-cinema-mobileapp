package com.example.cgv.ext

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.Transformation
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.cgv.R

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.inVisible() {
    this.visibility = View.INVISIBLE
}

fun View.disable() {
    this.isEnabled = false
}

fun View.enable() {
    this.isEnabled = true
}
fun View.isVisible() = this.visibility == View.VISIBLE

fun View.expandOrCollapse(): Boolean =
    if (!isVisible()) {
        expand()
        true
    } else {
        collapse()
        false
    }

fun expandOrCollapse(view: View, isExpand: Boolean): Boolean =
    if (isExpand) {
        view.expand()
        true
    } else {
        view.collapse()
        false
    }


fun View.expand() {
    val matchParentMeasureSpec =
        View.MeasureSpec.makeMeasureSpec((this.parent as View).width, View.MeasureSpec.EXACTLY)
    val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    measure(matchParentMeasureSpec, wrapContentMeasureSpec)
    val targetHeight = measuredHeight

    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
    this.layoutParams.height = 1
    val a = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            layoutParams.height = if (interpolatedTime == 1f)
                WindowManager.LayoutParams.WRAP_CONTENT
            else
                (targetHeight * interpolatedTime).toInt()
            requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // 1dp/ms
    a.duration = (targetHeight / this.context.resources.displayMetrics.density).toInt().toLong()
    this.startAnimation(a)
    this.visibility = View.VISIBLE
}

fun View.collapse() {
    val a = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            if (interpolatedTime == 1f) {
                gone()
            } else {
                layoutParams.height = measuredHeight - (measuredHeight * interpolatedTime).toInt()
                requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // 1dp/ms
    a.duration = (measuredHeight / context.resources.displayMetrics.density).toLong()
    startAnimation(a)
}

fun <T> ViewGroup.addViewExt(
    @LayoutRes layoutItem: Int, itemList: List<T>,
    margin: Int = 16, parentIndex: Int,
    listener: (viewBinding: ViewDataBinding?, item: T, parentIndex: Int, position: Int) -> Unit
) {
    this.removeAllViews()
    val param = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    param.marginStart = margin + 32
    val childGroup = LinearLayout(context)
    childGroup.orientation = LinearLayout.VERTICAL
    childGroup.layoutParams = param
    itemList.forEachIndexed { index, item ->
        val inflate = LayoutInflater.from(context)
        val viewBinding = DataBindingUtil.inflate<ViewDataBinding>(inflate, layoutItem, this, false)
        viewBinding.root.apply {
            listener.invoke(viewBinding, item, parentIndex, index)
            childGroup.addView(viewBinding.root)
        }
    }
    this.addView(childGroup)
}

fun <T> ViewGroup.addViewExt(
    @LayoutRes layoutItem: Int, itemList: List<T>,
    margin: Int = 16, parentIndex: Int, position: Int,
    listener: (viewBinding: ViewDataBinding?, item: T, parentIndex: Int, position: Int, lastIndex: Int) -> Unit
) {
    this.removeAllViews()
    val param = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
    param.marginStart = margin + 32
    val childGroup = LinearLayout(context)
    childGroup.orientation = LinearLayout.VERTICAL
    childGroup.layoutParams = param
    itemList.forEachIndexed { index, item ->
        val inflate = LayoutInflater.from(context)
        val viewBinding = DataBindingUtil.inflate<ViewDataBinding>(inflate, layoutItem, this, false)
        viewBinding.root.apply {
            listener.invoke(viewBinding, item, parentIndex, position, index)
            childGroup.addView(viewBinding.root)
        }
    }
    this.addView(childGroup)
}

//fun View.runAnimationAlpha(isShow: Boolean) {
//    val animation = AnimationUtils.loadAnimation(
//        this.context,
//        if (isShow) R.anim.anim_alpha_show else R.anim.anim_alpha_hide
//    )
//    this.doOnLayout {
//        startAnimation(animation)
//        animation.setAnimationListener(object : Animation.AnimationListener {
//            override fun onAnimationRepeat(animation: Animation?) {
//            }
//
//            override fun onAnimationEnd(animation: Animation?) {
//                if (isShow) it.visible() else {
//                    it.inVisible()
//                }
//            }
//
//            override fun onAnimationStart(animation: Animation?) {
//            }
//
//        })
//    }
//}