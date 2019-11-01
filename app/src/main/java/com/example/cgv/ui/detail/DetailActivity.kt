package com.example.cgv.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.example.cgv.R
import com.example.cgv.model.Movie
import com.example.cgv.ui.ticket.TicketActivity
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_second.*
import java.io.Serializable


class DetailActivity : YouTubeBaseActivity() {
    private lateinit var movie: Movie

    private lateinit var adapter: ScreenshotAdapter

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        if (Build.VERSION.SDK_INT in 19..20) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
        adapter = ScreenshotAdapter()
        movie = intent.extras?.getSerializable("item") as Movie
        tvTitle.text = movie.movieName
        tvType.text = movie.category + " - " + movie.timeInMinute
        tvContent.text = movie.description
        rvScreenshot.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvScreenshot.adapter = adapter
        ivClose.setOnClickListener {
            onBackPressed()
        }
        adapter.setDatalist(movie.screenshots)
        youtubeView.initialize(
            "AIzaSyCpJKzMOX2GPENhpNkGWsJftaPFq9XvB0U",
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    p0: YouTubePlayer.Provider?,
                    p1: YouTubePlayer?,
                    p2: Boolean
                ) {
                    if (!p2) {
                        p1!!.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                        //load the video
                        p1.loadVideo(movie.trailerURL)
                    }
                }

                override fun onInitializationFailure(
                    p0: YouTubePlayer.Provider?,
                    p1: YouTubeInitializationResult?
                ) {
                }

            })

        tvMore.setOnClickListener {
            if (tvContent.isExpanded) {
                tvContent.collapse()
                tvMore.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0,
                    R.drawable.ic_expand_more, 0
                )
            } else {
                tvContent.expand()
                tvMore.setCompoundDrawablesWithIntrinsicBounds(
                    0, 0,
                    R.drawable.ic_expand_less, 0
                )
            }
        }

        Glide.with(this)
            .load((intent.extras?.getSerializable("item") as Movie).imageURL)
            .apply(bitmapTransform(BlurTransformation(10, 1)))
            .into(ivBackground)

        btBuyTicket.setOnClickListener {
            val intent = Intent(this, TicketActivity::class.java)
            intent.putExtra("item", movie as Serializable)
            startActivity(intent)
        }
    }

    private fun setWindowFlag(bit: Int, on: Boolean) {
        val win = window

        val winParams = win.attributes

        if (on) {
            winParams.flags = winParams.flags or bit
        } else {
            winParams.flags = winParams.flags and bit.inv()
        }
        win.attributes = winParams
    }
}