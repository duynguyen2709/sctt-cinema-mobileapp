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
import com.example.cgv.CoreApplication
import com.example.cgv.R
import com.example.cgv.model.HomeInfo
import com.example.cgv.model.Movie
import com.example.cgv.model.Resource
import com.example.cgv.ui.detail.DetailActivity
import com.example.cgv.ui.history.HistoryTransactionActivity
import com.example.cgv.ui.login.LogInActivity
import com.example.cgv.ui.schedule.SchedulerByMovie
import com.example.cgv.ui.schedule.SchedulerByThearter
import com.example.cgv.ui.signup.SignUpActivity
import com.example.cgv.ui.ticket.TicketActivity
import com.example.cgv.viewmodel.HomeViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_drawer_left_main.*
import java.io.Serializable


class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(p0: View?) {
        if (isEnableClick) {
            when (p0?.id) {
                R.id.btBuyTicket -> {
                    val intent = Intent(this, TicketActivity::class.java)
                    intent.putExtra("item", adapter.getItem(vpHome.currentItem))
                    startActivity(intent)
                }
                R.id.btnHistory -> {
                    val intent = Intent(this, HistoryTransactionActivity::class.java)
                    startActivity(intent)
                }
                R.id.btnLogin -> {
                    val intent = Intent(this, LogInActivity::class.java)
                    startActivity(intent)
                }
                R.id.btnMovie -> {
                    val intent = Intent(this, SchedulerByMovie::class.java)
                    intent.putExtra("item", nowMovie as Serializable)
                    startActivity(intent)
                }
                R.id.btnTheater -> {
                    val intent = Intent(this, SchedulerByThearter::class.java)
                    startActivity(intent)
                }
                R.id.btnSignUp -> {
                    val intent = Intent(this, SignUpActivity::class.java)
                    startActivity(intent)
                }
                R.id.btnLogOut->{
                    CoreApplication.instance.clearUser()
                    onResume()
                }
            }
            isEnableClick = false
        }
    }

    private lateinit var adapter: MovieAdapter

    private lateinit var animation: Animation

    private lateinit var viewModel: HomeViewModel

    private var listNowMovie = MutableLiveData<List<Movie>>()

    private var listSoonMovie = MutableLiveData<List<Movie>>()

    private var isEnableClick = true

    private var cachePosition0 = 10000 / 2

    private var cachePosition1 = 10000 / 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        initView()

        addListener()

        fetchData()

        viewModel.getHomeInfo()

        if(CoreApplication.instance.user!=null){
            btnSignUp.visibility=View.INVISIBLE
        }
    }


    private fun fetchData() {
        viewModel.homeInfoLiveData.observe(this,
            Observer<Resource<HomeInfo>> { t ->
                when (t?.status) {
                    Resource.SUCCESS -> {
                        layoutResult.visibility = View.VISIBLE
                        layoutSwipe.isRefreshing = false
                        t.data?.let {
                            listNowMovie.value = it.currentShowingMovies as MutableList<Movie>
                            listSoonMovie.value = it.comingSoonMovies as MutableList<Movie>
                            nowMovie.clear()
                            nowMovie.addAll(it.currentShowingMovies)
                            nowMovie.addAll(it.comingSoonMovies)
                        } ?: kotlin.run {
                            layoutResult.visibility = View.INVISIBLE
                        }
                    }
                    Resource.LOADING -> {

                    }
                    Resource.ERROR -> {
                        layoutResult.visibility = View.INVISIBLE
                        layoutSwipe.isRefreshing = false
                        listNowMovie.value = mutableListOf()
                        listSoonMovie.value = mutableListOf()
                        nowMovie.clear()
                    }
                }
            })

        listNowMovie.observe(this,
            Observer<List<Movie>> { t ->
                if (tlHome.selectedTabPosition == 0) {
                    t?.apply {
                        if (isNotEmpty())
                            adapter.setData(this)
                        else
                            layoutResult.visibility = View.INVISIBLE
                    } ?: kotlin.run {
                        layoutResult.visibility = View.INVISIBLE
                    }

                    vpHome.adapter = adapter

                    vpHome.currentItem = 1000 / 2
                    cachePosition0 = 1000 / 2
                }
            })

        listSoonMovie.observe(this,
            Observer<List<Movie>> { t ->
                if (tlHome.selectedTabPosition == 1) {
                    t?.let {
                        if (it.isNotEmpty())
                            adapter.setData(it)
                        else
                            layoutResult.visibility = View.INVISIBLE
                    } ?: kotlin.run {
                        layoutResult.visibility = View.INVISIBLE
                    }

                    vpHome.adapter = adapter

                    vpHome.currentItem = 1000 / 2
                    cachePosition1 = 1000 / 2
                }
            })
    }

    @SuppressLint("RtlHardcoded")
    private fun addListener() {
        adapter = MovieAdapter()

        adapter.setPagerAdapterListener(object : MovieAdapter.PagerAdapterListener {
            override fun onClick(movie: Movie) {
                if (isEnableClick) {
                    val intent = Intent(this@MainActivity, DetailActivity::class.java)
                    intent.putExtra("item", movie as Serializable)
                    startActivity(intent)
                    isEnableClick = false
                }
            }
        })

        layoutSwipe.setOnRefreshListener {
            viewModel.getHomeInfo()

            cachePosition0 = 1000 / 2

            cachePosition1 = 1000 / 2
        }

        vpHome.adapter = adapter

        vpHome.currentItem = 10000 / 2

        cachePosition0 = 1000 / 2

        cachePosition1 = 1000 / 2

        ivDrawer.setOnClickListener {
            layoutDrawer.openDrawer(Gravity.LEFT)
        }

        btnMovie.setOnClickListener(this)

        btnTheater.setOnClickListener(this)

        btnSignUp.setOnClickListener(this)

        btnLogin.setOnClickListener(this)

        btnHistory.setOnClickListener(this)

        btnLogOut.setOnClickListener(this)

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
                                if (isNullOrEmpty())
                                    layoutResult.visibility = View.INVISIBLE
                                else
                                    layoutResult.visibility = View.VISIBLE
                                vpHome.adapter = adapter
                            }
                            vpHome.currentItem = cachePosition0
                        }
                        1 -> {
                            changeInfo(null)
                            listSoonMovie.value?.apply {
                                adapter.setData(this)
                                if (isNullOrEmpty())
                                    layoutResult.visibility = View.INVISIBLE
                                else
                                    layoutResult.visibility = View.VISIBLE
                                vpHome.adapter = adapter
                            }
                            vpHome.currentItem = cachePosition1
                        }
                    }
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
                if (tlHome.selectedTabPosition == 0)
                    cachePosition0 = position
                else cachePosition1 = position
            }

        })

        btBuyTicket.setOnClickListener(this)
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
        isEnableClick = true
        if (nowMovie.isNullOrEmpty()) {
            viewModel.getHomeInfo()
        }
        if(CoreApplication.instance.user!=null){
            btnSignUp.visibility=View.GONE
            btnLogin.visibility=View.GONE
            btnLogOut.visibility=View.VISIBLE
            btnHistory.visibility=View.VISIBLE
        }
        else{
            btnSignUp.visibility=View.VISIBLE
            btnLogin.visibility=View.VISIBLE
            btnLogOut.visibility=View.GONE
            btnHistory.visibility=View.GONE
        }
    }

    companion object {
        var nowMovie: MutableList<Movie> = mutableListOf()
    }
}
