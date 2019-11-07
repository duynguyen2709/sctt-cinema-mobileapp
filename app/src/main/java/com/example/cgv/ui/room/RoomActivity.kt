package com.example.cgv.ui.room

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cgv.BaseActivity
import com.example.cgv.CoreApplication
import com.example.cgv.R
import com.example.cgv.databinding.ActivityReserveSeatBinding
import com.example.cgv.ext.SpacesItemDecoration
import com.example.cgv.model.Resource
import com.example.cgv.model.Seat
import com.example.cgv.model.Ticket
import kotlinx.android.synthetic.main.activity_reserve_seat.*
import java.util.stream.Collector
import java.util.stream.Collectors

class RoomActivity :BaseActivity<ActivityReserveSeatBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_reserve_seat
    var seatAdapter = SeatAdapter(this,this::listener)
    private var showTimeID:String?=null
    private lateinit var viewModel: RoomTheaterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showTimeID = intent.getStringExtra("SHOW_TIME_ID")
        viewModel = ViewModelProviders.of(this).get(RoomTheaterViewModel::class.java)
        viewModel.getSeatMap(showTimeID!!)
    }

    override fun bindView() {
        super.bindView()
        viewBinding.seatMap.layoutManager = GridLayoutManager(this, 10)
//        viewBinding.seatMap.addItemDecoration(SpacesItemDecoration(10))
        seatMap.adapter = seatAdapter
    }

    override fun bindViewModel() {
        super.bindViewModel()
        viewModel.listSeat.observe(this, Observer {
            when (it?.status) {
                Resource.SUCCESS -> {
                    seatAdapter.setList(it.data?.seats?.stream()?.flatMap { it -> it.stream() }?.collect(
                        Collectors.toList()) as ArrayList<Seat>)
                }
                Resource.LOADING -> {

                }
                Resource.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    fun listener(seatCount: Int,price:String) {
        viewBinding.footer.tvSeatNums.text = "$seatCount Seat(s)\n$price"
    }
}