package com.example.cgv.ui.room

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cgv.BaseActivity
import com.example.cgv.CoreApplication
import com.example.cgv.R
import com.example.cgv.databinding.ActivityReserveSeatBinding
import com.example.cgv.databinding.LayoutPopUpBinding
import com.example.cgv.ext.SpacesItemDecoration
import com.example.cgv.model.*
import com.example.cgv.ui.history.DetailHistoryActivity
import com.example.cgv.ui.login.LogInActivity
import com.example.cgv.util.PopUp
import kotlinx.android.synthetic.main.activity_reserve_seat.*
import java.util.stream.Collector
import java.util.stream.Collectors

class RoomActivity :BaseActivity<ActivityReserveSeatBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_reserve_seat
    var seatAdapter = SeatAdapter(this,this::listener)
    private var showTimeID:String?=null
    private var seatCodes:ArrayList<String> = arrayListOf()
    private var totalPrice:String=""
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
        seatMap.adapter = seatAdapter
        viewBinding.footer.btnBuyTicket.setOnClickListener {
            if(CoreApplication.instance.user!=null) {
                (this as? BaseActivity<*>)?.showPopup(
                    PopUp(
                        R.layout.layout_pop_up,
                        messageQueue = this::popEvent
                    )
                )
            }else{
                val intent = Intent(this, LogInActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun bindViewModel() {
        super.bindViewModel()
        viewModel.listSeat.observe(this, Observer {
            when (it?.status) {
                Resource.SUCCESS -> {
                    seatAdapter.setBasePrice(it.data?.basePrice?:0)
                    seatAdapter.setVIPPrice(it.data?.VIPPrice?:0)
                    seatAdapter.setList(it.data?.seats?.stream()?.flatMap { it -> it.stream() }?.collect(
                        Collectors.toList()) as ArrayList<Seat>)
                    viewBinding.seatMap.addItemDecoration(SpacesItemDecoration(10,10))
                }
                Resource.LOADING -> {

                }
                Resource.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        viewModel.createTicketLiveData.observe(this, Observer {
            when (it?.status) {
                Resource.SUCCESS -> {
                    val intent= Intent(this, DetailHistoryActivity::class.java)
                    intent.putExtra("TICKET",it.data)
                    startActivity(intent)
                    finish()
                }
                Resource.LOADING -> {

                }
                Resource.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnOk->{
                closePopup()
                viewModel.createTicket(CreateTicketParam(email = CoreApplication.instance.user?.email,seatCodes = seatCodes,showtimeID = showTimeID?.toInt()!!))

            }
            R.id.btnCancel->{
                closePopup()
            }
        }
    }

    private fun popEvent(popupBinding: ViewDataBinding?, dialog: Dialog?) {
        (popupBinding as? LayoutPopUpBinding)?.apply {
            left = "Cancel"
            right="Ok"
            btnCancel.setOnClickListener(this@RoomActivity::onClick)
            btnOk.setOnClickListener(this@RoomActivity::onClick)
            title = "Purchasing for :\n" +
                    "• Seat: ${seatCodes.toListString()}\n"+
                    "• Cost: $totalPrice"
        }
    }

    fun listener(seatCount: Int,price:String,seatCode:String) {
        viewBinding.footer.tvSeatNums.text = "$seatCount Seat(s)\n\n$price"
        if(seatCount>seatCodes.size){
            seatCodes.add(seatCode)
        }
        else {
            seatCodes.remove(seatCode)
        }
        totalPrice=price
    }
}
fun ArrayList<String>.toListString():String{
    var res=""
    this.forEach { res+= "$it, " }
    res=res.substring(0,res.length-2)
    return res
}