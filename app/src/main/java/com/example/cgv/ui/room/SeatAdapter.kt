package com.example.cgv.ui.room

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cgv.R
import com.example.cgv.model.Seat
import com.example.cgv.model.StatusSeat
import com.google.android.youtube.player.internal.s

class SeatAdapter(val context: Context,val listener:((ticketCount:Int, totalPrice:String) -> Unit)? = null) : RecyclerView.Adapter<SeatAdapter.ItemView>() {

    var listSeat: ArrayList<Seat> = ArrayList()
    private var seatSelectionCount = 0
    private var totalPrice:Long  = 0
    private var basePrice: Long = 0
    private var VIPPrice: Long = 0

    fun setBasePrice(basePrice:Long){
        this.basePrice=basePrice
    }

    fun setVIPPrice(VIPPrice:Long){
        this.VIPPrice=VIPPrice
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.seat_item, parent, false)
        return ItemView(view)
    }

    override fun getItemCount(): Int {
        return listSeat.size
    }

    override fun onBindViewHolder(holder: ItemView, position: Int) {
        val item = listSeat[position]
        holder.bind(item)
        //holder.itemView.setOnClickListener { listener(item) }
    }

    fun setList(newList: ArrayList<Seat>) {
        listSeat = newList
        notifyDataSetChanged()
    }

    inner class ItemView(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: Seat) {
            if (item.seatCode != "") {
                itemView.findViewById<TextView>(R.id.name).text = item.seatCode
                if (item.status == StatusSeat.AVAILABLE.value) {
                    itemView.background = context.getDrawable(R.drawable.background_normal_seat)
                    if (item.seatType == 2) {
                        itemView.background = context.getDrawable(R.drawable.background_vip_seat)
                    }
                }
            } else {
                itemView.setBackgroundColor(Color.TRANSPARENT)
            }
            itemView.setOnClickListener {
                when(item.status){
                    StatusSeat.AVAILABLE.value->{
                        itemView.background = context.getDrawable(R.drawable.background_selected_seat)
                        itemView.findViewById<TextView>(R.id.name).text = ""
                        item.status=StatusSeat.RESERVED.value
                        seatSelectionCount++
                        totalPrice += if(item.seatType==1){
                            basePrice
                        } else{
                            VIPPrice
                        }
                        if (totalPrice != 0.toLong()) {
                            var s = String.format("%,d", totalPrice)
                            s += " đ"
                            listener?.invoke(seatSelectionCount, s)
                        }
                        else{
                            listener?.invoke(seatSelectionCount, "")
                        }
                    }
                    StatusSeat.RESERVED.value->{
                        itemView.background = context.getDrawable(R.drawable.background_normal_seat)
                        if (item.seatType == 2) {
                            itemView.background = context.getDrawable(R.drawable.background_vip_seat)
                        }
                        itemView.findViewById<TextView>(R.id.name).text = item.seatCode
                        item.status=StatusSeat.AVAILABLE.value
                        seatSelectionCount--
                        totalPrice -= if(item.seatType==1){
                            basePrice
                        } else{
                            VIPPrice
                        }
                        if (totalPrice != 0.toLong()) {
                            var s = String.format("%,d", totalPrice)
                            s += " đ"
                            listener?.invoke(seatSelectionCount, s)
                        }
                        else{
                            listener?.invoke(seatSelectionCount, "")
                        }

                    }
                }

            }
        }

    }

}