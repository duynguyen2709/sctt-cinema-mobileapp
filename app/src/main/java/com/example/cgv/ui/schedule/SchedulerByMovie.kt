package com.example.cgv.ui.schedule

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.cgv.R
import com.example.cgv.model.Movie
import com.example.cgv.model.Theater
import com.example.cgv.ui.ticket.TicketActivity
import kotlinx.android.synthetic.main.activity_ticketbymovie.*
import kotlinx.android.synthetic.main.layout_item_date.view.*
import kotlinx.android.synthetic.main.layout_item_movie.view.*
import java.io.Serializable

class SchedulerByMovie : AppCompatActivity() {
    private val adapter = SchedulerByMovieAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticketbymovie)
        val bundle = intent.extras?.getSerializable("item") as List<Movie>
        if (bundle.isNotEmpty()) {
            lvMovie.layoutManager = LinearLayoutManager(this)
            lvMovie.adapter = adapter
            adapter.setData(bundle)
            adapter.setListener(object : SchedulerByMovieAdapter.ClickListener{
                override fun onClick(item: Movie) {
                    val intent = Intent(this@SchedulerByMovie, TicketActivity::class.java)
                    intent.putExtra("item", item as Serializable)
                    startActivity(intent)
                }

            })
        }

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}

class SchedulerByMovieAdapter :
    RecyclerView.Adapter<SchedulerByMovieAdapter.SchedulerByMovieViewHolder>() {
    private val items = mutableListOf<Movie>()

    private var listener: ClickListener? = null

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SchedulerByMovieViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchedulerByMovieViewHolder {
        return SchedulerByMovieViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_item_movie, parent, false)
        )
    }

    fun setData(list: List<Movie>) {
        items.clear()
        if (list.isNotEmpty()) {
            items.addAll(list)
        }
        notifyDataSetChanged()
    }

    fun setListener(listener: ClickListener){
        this.listener = listener
    }

    inner class SchedulerByMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                items[adapterPosition].let { item ->
                    listener?.onClick(item)
                }
                notifyDataSetChanged()
            }
        }
        @SuppressLint("SetTextI18n")
        fun bind(item: Movie) {
            Glide.with(itemView.context)
                .load(item.imageURL)
                .override(80, 100)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(itemView.ivAvatar)
            itemView.tvName.text = item.movieName
            itemView.tvTime.text = item.timeInMinute.toString() + "'"
            itemView.tvDate.text = item.dateFrom
        }
    }

    interface ClickListener {
        fun onClick(item: Movie)
    }
}