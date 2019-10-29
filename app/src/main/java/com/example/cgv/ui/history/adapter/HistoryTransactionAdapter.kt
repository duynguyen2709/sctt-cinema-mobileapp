package com.example.cgv.ui.history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.cgv.R
import com.example.cgv.databinding.LayoutTransactionHistoryBinding
import com.example.cgv.model.Ticket

class HistoryTransactionAdapter(val listener:((item: Ticket?, position: Int?) -> Unit)? = null): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var data = ArrayList<Ticket>()

    fun addData(_data:ArrayList<Ticket>){
        data=_data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<LayoutTransactionHistoryBinding>(LayoutInflater.from(parent.context),  R.layout.layout_transaction_history, parent,false)
        return TransactionViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? TransactionViewHolder)?.bind(data[position])
    }

    inner class TransactionViewHolder(private val binding: LayoutTransactionHistoryBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: Ticket?){
            binding.ticket=item
            binding.root.setOnClickListener {
                listener?.invoke(item, null)
            }
        }

    }

}