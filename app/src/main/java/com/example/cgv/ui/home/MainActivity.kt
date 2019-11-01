package com.example.cgv.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.example.cgv.R
import com.example.cgv.model.HomeInfo
import com.example.cgv.model.Movie
import com.example.cgv.model.Resource
import com.example.cgv.ui.detail.DetailActivity
import com.example.cgv.ui.history.HistoryTransactionActivity
import com.example.cgv.ui.login.LogInActivity
import com.example.cgv.ui.schedule.SchedulerByMovie
import com.example.cgv.ui.schedule.TicketByTheater
import com.example.cgv.ui.signup.SignUpActivity
import com.example.cgv.ui.ticket.TicketActivity
import com.example.cgv.viewmodel.HomeViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable


class MainActivity : AppCompatActivity() {
    private lateinit var adapter: MovieAdapter

    private lateinit var animation: Animation

    private lateinit var viewModel: HomeViewModel

    private var listNowMovie = MutableLiveData<List<Movie>>()

    private var listSoonMovie = MutableLiveData<List<Movie>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        initView()

        addListener()

        fetchData()

        viewModel.getHomeInfo()
    }

    private fun fetchData() {
        viewModel.homeInfoLiveData.observe(this,
            Observer<Resource<HomeInfo>> { t ->
                when (t?.status) {
                    Resource.SUCCESS -> {
                        t.data?.let {
                            listNowMovie.value = it.currentShowingMovies as MutableList<Movie>
                            listSoonMovie.value = it.comingSoonMovies as MutableList<Movie>
                        }
                    }
                    Resource.LOADING -> {

                    }
                    Resource.ERROR -> {

                    }
                }
            })

        listNowMovie.observe(this, object : Observer<List<Movie>> {
            override fun onChanged(t: List<Movie>?) {
                if (tlHome.selectedTabPosition == 0) {
                    t?.apply {
                        cacheMovieNow.addAll(this)
                        adapter.setData(this)
                    }
                    vpHome.currentItem = 1000 / 2
                }
            }

        })

        listSoonMovie.observe(this, object : Observer<List<Movie>> {
            override fun onChanged(t: List<Movie>?) {
                if (tlHome.selectedTabPosition == 1) {
                    t?.apply {
                        adapter.setData(this)
                    }
                    vpHome.currentItem = 1000 / 2
                }
            }
        })
    }

    @SuppressLint("RtlHardcoded")
    private fun addListener() {
        adapter = MovieAdapter()
        adapter.setPagerAdapterListener(object : MovieAdapter.PagerAdapterListener {
            override fun onClick(movie: Movie) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("item", movie as Serializable)
                startActivity(intent)
            }
        })
        vpHome.adapter = adapter

        vpHome.currentItem = 10000 / 2

        ivDrawer.setOnClickListener {
            layoutDrawer.openDrawer(Gravity.LEFT)
        }

        btnMovie.setOnClickListener {
            btnTheater.isEnabled = false
            btnMovie.isEnabled = false
            val intent = Intent(this, SchedulerByMovie::class.java)
            intent.putExtra("item", cacheMovieNow as Serializable)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        btnTheater.setOnClickListener {
            btnTheater.isEnabled = false
            btnMovie.isEnabled = false
            val intent = Intent(this, TicketByTheater::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }

        btnHistory.setOnClickListener {
            val intent = Intent(this, HistoryTransactionActivity::class.java)
            startActivity(intent)
        }
        layoutDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        changeInfo(adapter.getItem(10000 / 2))

        animation()

        tlHome.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                p0?.let {
                    when (p0.position) {
                        0 -> {
                            changeInfo(null)
                            listNowMovie.value?.apply {
                                adapter.setData(this)
                                vpHome.adapter = adapter
                            }
                        }
                        1 -> {
                            changeInfo(null)
                            listSoonMovie.value?.apply {
                                adapter.setData(this)
                                vpHome.adapter = adapter
                            }
                        }
                    }
                    vpHome.currentItem = 1000 / 2
                }
            }
        })

        vpHome.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                changeInfo(adapter.getItem(position))
                animation()
            }

        })
        btBuyTicket.setOnClickListener {
            val intent = Intent(this, TicketActivity::class.java)
            intent.putExtra("item", adapter.getItem(vpHome.currentItem))
            startActivity(intent)
        }
    }

    private fun initView() {
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
        animation = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.home_animation
        )
    }

    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    @SuppressLint("SetTextI18n")
    fun changeInfo(movie: Movie?) {
        movie?.let {
            tvName.text = it.movieName
            btnTime.text = it.category + " - " + it.timeInMinute + " '"
            btBuyTicket.isEnabled = true
        } ?: kotlin.run {
            tvName.text = ""
            btnTime.text = ""
            btBuyTicket.isEnabled = false
        }
    }

    fun animation() {
        ctContent.startAnimation(animation)
    }

    override fun onResume() {
        super.onResume()
        btnTheater.isEnabled = true
        btnMovie.isEnabled = true
    }

    companion object {
        var cacheMovieNow = mutableListOf<Movie>()
    }
}
