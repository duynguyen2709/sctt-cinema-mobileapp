package com.example.cgv.ui.ticket

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cgv.R
import com.example.cgv.databinding.ActivityTicketBinding
import com.example.cgv.model.*
import com.example.cgv.ui.room.RoomActivity
import kotlinx.android.synthetic.main.activity_ticket.*
import kotlinx.android.synthetic.main.layout_item_date.view.*
import kotlinx.android.synthetic.main.layout_item_detail_scheduler.view.*
import kotlinx.android.synthetic.main.layout_item_scheduler.view.*
import kotlinx.android.synthetic.main.layout_item_showtime.view.*
import java.time.LocalDateTime

class TicketActivity : AppCompatActivity() {
    private lateinit var viewModel: TicketViewModel

    private val adapterDate = DateAdapter()

    private val adapterScheduler = SchedulerAdapter()

    private var isEnableClick: Boolean = true

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityTicketBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_ticket
        )

        binding.flagResult = StateView.Error

        val bundle = intent.extras?.getSerializable("item")

        viewModel = ViewModelProviders.of(this).get(TicketViewModel::class.java)

        val currentDate = LocalDateTime.now()

        tvDate.text = convertDateTimeToString(currentDate)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        adapterDate.setData(createListDate(currentDate))

        schedulerList.layoutManager = LinearLayoutManager(this)
        schedulerList.adapter = adapterScheduler
        dateList.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        dateList.adapter = adapterDate

        layoutSwipe.setOnRefreshListener {
            viewModel.refreshListShowTimes()
        }

        if (bundle is Theater) {
            adapterDate.setListener(object : DateAdapter.ClickListener {
                override fun onClick(item: LocalDateTime) {
                    viewModel.getListShowTimes(
                        THEATER,
                        bundle.theaterId.toString(),
                        item.year.toString() +  if (item.monthValue.toString().length > 1) item.monthValue.toString() else "0" + item.monthValue.toString() +
                                if (item.dayOfMonth.toString().length > 1) item.dayOfMonth.toString() else "0" + item.dayOfMonth.toString()
                    )

                    tvDate.text = convertDateTimeToString(item)
                }

            })
            toolbar.title = bundle.theaterName
            viewModel.getListShowTimes(
                THEATER,
                bundle.theaterId.toString(),
                currentDate.year.toString() + if (currentDate.monthValue.toString().length > 1) currentDate.monthValue.toString() else "0" + currentDate.monthValue.toString() +
                        if (currentDate.dayOfMonth.toString().length > 1) currentDate.dayOfMonth.toString() else "0" + currentDate.dayOfMonth.toString()
            )
        }

        if (bundle is Movie) {
            adapterDate.setListener(object : DateAdapter.ClickListener {
                override fun onClick(item: LocalDateTime) {
                    viewModel.getListShowTimes(
                        MOVIE,
                        bundle.movieID.toString(),
                        item.year.toString() + if (item.monthValue.toString().length > 1) item.monthValue.toString() else "0" + item.monthValue.toString() +
                                if (item.dayOfMonth.toString().length > 1) item.dayOfMonth.toString() else "0" + item.dayOfMonth.toString()
                    )

                    tvDate.text = convertDateTimeToString(item)
                }

            })
            toolbar.title = bundle.movieName
            Log.d("MOVIE", bundle.movieID.toString())
            viewModel.getListShowTimes(
                MOVIE,
                bundle.movieID.toString(),
                currentDate.year.toString() + if (currentDate.monthValue.toString().length > 1) currentDate.monthValue.toString() else "0" + currentDate.monthValue.toString() +
                        if (currentDate.dayOfMonth.toString().length > 1) currentDate.dayOfMonth.toString() else "0" + currentDate.dayOfMonth.toString()
            )
        }

        viewModel.listShowTimes.observe(this,
            Observer<Resource<ShowTimes>> { t ->
                when (t.status) {
                    Resource.LOADING -> {
                        binding.flagResult = StateView.Loading
                        layoutSwipe.isRefreshing = false
                    }
                    Resource.SUCCESS -> {
                        layoutSwipe.isRefreshing = false
                        binding.flagResult = StateView.Success
                        if (t.data?.showTimes.isNullOrEmpty()) {
                            binding.flagResult = StateView.Empty
                        } else {
                            binding.flagResult = StateView.Success
                            adapterScheduler.setData(t.data?.showTimes)
                            adapterScheduler.setListener(object : SchedulerAdapter.ClickListener {
                                override fun onClick(item: ShowTime) {
                                    if(isEnableClick) {
                                        isEnableClick = false
                                        val intent =
                                            Intent(this@TicketActivity, RoomActivity::class.java)
                                        intent.putExtra("SHOW_TIME_ID", item.showtimeID)
                                        startActivity(intent)
                                    }
                                }
                            })
                        }
                    }
                    Resource.ERROR -> {
                        layoutSwipe.isRefreshing = false
                        binding.flagResult = StateView.Error
                    }
                }
            })
    }

    override fun onResume() {
        super.onResume()
        isEnableClick = true
    }

    @SuppressLint("NewApi")
    fun createListDate(nowDay: LocalDateTime): List<LocalDateTime> {
        val list = mutableListOf<LocalDateTime>()
        for (i in 0 until 14) {
            list.add(nowDay.plusDays(i.toLong()))
        }
        return list
    }

    @SuppressLint("DefaultLocale", "NewApi")
    fun convertDateTimeToString(dateTime: LocalDateTime): String {
        return dateTime.dayOfMonth.toString() + " - " + dateTime.monthValue + " - " + dateTime.year
    }

    companion object {
        const val MOVIE = 1
        const val THEATER = 2
    }
}

class DateAdapter : RecyclerView.Adapter<DateAdapter.DateViewHolder>() {
    private val items: MutableList<LocalDateTime> = ArrayList()

    private var positionNow: Int = 0

    private var listener: ClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        return DateViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_item_date, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setData(list: List<LocalDateTime>?) {
        items.clear()
        if (!list.isNullOrEmpty()) {
            items.addAll(list)
        }
        notifyDataSetChanged()
    }

    fun setListener(listener: ClickListener) {
        this.listener = listener
    }

    inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                if (adapterPosition >= 0) {
                    items[adapterPosition].let { item ->
                        positionNow = adapterPosition
                        listener?.onClick(item)
                        itemView.tvDayNumber.background =
                            itemView.context.getDrawable(R.drawable.circle_text)
                    }
                    notifyDataSetChanged()
                }
            }
        }

        @SuppressLint("DefaultLocale", "NewApi")
        fun bind(holder: LocalDateTime) {
            if (adapterPosition == 0) {
                itemView.tvDayNumber.text = holder.dayOfMonth.toString()
                itemView.tvDayText.text = "Now"
            } else {
                itemView.tvDayNumber.text = holder.dayOfMonth.toString()
                itemView.tvDayText.text = holder.dayOfWeek.toString().toLowerCase().capitalize()
            }

            if (positionNow == adapterPosition) {
                itemView.tvDayNumber.background =
                    itemView.context.getDrawable(R.drawable.circle_text)
            } else {
                itemView.tvDayNumber.background =
                    itemView.context.getDrawable(R.drawable.circle_unselect_text)
            }

        }
    }

    interface ClickListener {
        fun onClick(item: LocalDateTime)
    }
}

class SchedulerAdapter : RecyclerView.Adapter<SchedulerAdapter.SchedulerViewHolder>() {
    private val items = hashMapOf<String, Map<String, List<ShowTime>>>()

    private val listKey = mutableListOf<String>()

    private val listValue = mutableListOf<Map<String, List<ShowTime>>>()

    private var listener: ClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchedulerViewHolder {
        return SchedulerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_item_scheduler, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SchedulerViewHolder, position: Int) {
        holder.bind(listKey[position], listValue[position])
    }

    fun setListener(listener: ClickListener) {
        this.listener = listener
    }

    fun setData(map: Map<String, Map<String, List<ShowTime>>>?) {
        items.clear()
        listKey.clear()
        listValue.clear()
        if (!map.isNullOrEmpty()) {
            items.putAll(map)
            listKey.addAll(items.keys)
            listValue.addAll(items.values)
        }
        notifyDataSetChanged()
    }

    inner class SchedulerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(key: String, value: Map<String, List<ShowTime>>) {
            itemView.tvName.text = key
            val adapter = DetailSchedulerAdapter()
            itemView.detailSchedulerList.layoutManager =
                LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)
            itemView.detailSchedulerList.adapter = adapter
            adapter.setListener(object : DetailSchedulerAdapter.ClickListener {
                override fun onClick(item: ShowTime) {
                    listener?.onClick(item)
                }

            })
            adapter.setData(value)
        }
    }

    interface ClickListener {
        fun onClick(item: ShowTime)
    }
}

class DetailSchedulerAdapter :
    RecyclerView.Adapter<DetailSchedulerAdapter.DetailSchedulerViewHolder>() {
    private val items = hashMapOf<String, List<ShowTime>>()

    private val listKey = mutableListOf<String>()

    private val listValue = mutableListOf<List<ShowTime>>()

    private var listener: ClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailSchedulerViewHolder {
        return DetailSchedulerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_item_detail_scheduler, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: DetailSchedulerViewHolder, position: Int) {
        holder.bind(listKey[position], listValue[position])
    }

    fun setData(map: Map<String, List<ShowTime>>) {
        items.clear()
        listKey.clear()
        listValue.clear()
        if (!map.isNullOrEmpty()) {
            items.putAll(map)
            listKey.addAll(items.keys)
            listValue.addAll(items.values)
        }
        notifyDataSetChanged()
    }

    fun setListener(listener: ClickListener) {
        this.listener = listener
    }

    inner class DetailSchedulerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(key: String, value: List<ShowTime>) {
            itemView.tvTypeRoom.text = key
            val adapter = ShowTimeAdapter()
            adapter.setData(value)
            adapter.setListener(object : ShowTimeAdapter.ClickListener {
                override fun onClick(item: ShowTime) {
                    listener?.onClick(item)
                }

            })
            itemView.showTimesList.layoutManager =
                LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
            itemView.showTimesList.adapter = adapter
        }
    }

    interface ClickListener {
        fun onClick(item: ShowTime)
    }
}

class ShowTimeAdapter : RecyclerView.Adapter<ShowTimeAdapter.ShowTimeViewHolder>() {
    private val items = mutableListOf<ShowTime>()

    private var listener: ClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowTimeViewHolder {
        return ShowTimeViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_item_showtime, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ShowTimeViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setListener(listener: ClickListener) {
        this.listener = listener
    }

    fun setData(map: List<ShowTime>) {
        items.clear()
        if (!map.isNullOrEmpty()) {
            items.addAll(map)
        }
        notifyDataSetChanged()
    }

    inner class ShowTimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ShowTime) {
            itemView.btnTime.text = item.startTime
            itemView.btnTime.setOnClickListener {
                listener?.onClick(item)
            }
        }
    }

    interface ClickListener {
        fun onClick(item: ShowTime)
    }
}
