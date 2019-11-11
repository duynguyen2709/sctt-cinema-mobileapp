package com.example.cgv.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.cgv.R
import kotlinx.android.synthetic.main.layout_item_screenshot.view.*

class ScreenshotAdapter : RecyclerView.Adapter<ScreenshotAdapter.ScreenshotViewHolder>() {
    private val items = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenshotViewHolder {
        return ScreenshotViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_item_screenshot, parent, false)
        )
    }

    fun setDatalist(list: List<String>?) {
        items.clear()
        if (!list.isNullOrEmpty()) {
            items.addAll(list)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ScreenshotViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ScreenshotViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val img = itemView.ivScreenshot
        fun bind(item: String) {
            Glide.with(itemView)
                .load(item)
                .placeholder(R.drawable.load)
                .error(R.drawable.error_image)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(img)
        }
    }

}