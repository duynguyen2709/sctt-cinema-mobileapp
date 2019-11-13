package com.example.cgv.ui.home

import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.cgv.R
import com.example.cgv.model.Movie
import com.github.islamkhsh.CardSliderAdapter
import kotlinx.android.synthetic.main.item_card_content.view.*


class MovieAdapter : CardSliderAdapter<Movie>() {

    private var listener: PagerAdapterListener? = null

    override fun bindView(position: Int, itemContentView: View, item: Movie?) {
        item?.run {
            val requestOptions = RequestOptions()
            requestOptions.placeholder(R.drawable.load)
            requestOptions.error(R.drawable.ic_error)

            Glide.with(itemContentView)
                .setDefaultRequestOptions(requestOptions)
                .load(imageURL).into(itemContentView.movie_poster)

            itemContentView.setOnClickListener {
                listener?.onClick(item)
            }
        }
    }

    override fun getItemContentLayout(position: Int) = R.layout.item_card_content

    fun setPagerAdapterListener(listener: PagerAdapterListener) {
        this.listener = listener
    }

    interface PagerAdapterListener {
        fun onClick(movie: Movie)
    }
}