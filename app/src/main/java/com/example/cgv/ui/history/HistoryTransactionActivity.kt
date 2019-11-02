package com.example.cgv.ui.history

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cgv.BaseActivity
import com.example.cgv.CoreApplication
import com.example.cgv.R
import com.example.cgv.databinding.ActivityHistoryTransactionsBinding
import com.example.cgv.model.LoginParam
import com.example.cgv.model.Resource
import com.example.cgv.model.Ticket
import com.example.cgv.ui.history.adapter.HistoryTransactionAdapter
import com.example.cgv.viewmodel.AuthenticationViewModel
import com.example.cgv.viewmodel.UserViewModel

class HistoryTransactionActivity : BaseActivity<ActivityHistoryTransactionsBinding>(){
    override val layoutId: Int = R.layout.activity_history_transactions
    private lateinit var viewModel : UserViewModel
    private lateinit var adapter: HistoryTransactionAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        viewModel.getListHistory(CoreApplication.instance.user?.email?:"")
    }

    override fun bindView() {
        super.bindView()
        viewBinding.activity=this
        viewBinding.swipeContainer.isRefreshing=true
        viewBinding.swipeContainer.setOnRefreshListener {
            viewModel.getListHistory(CoreApplication.instance.user?.email?:"")
        }
        adapter= HistoryTransactionAdapter(this::clickEvent)
        viewBinding.rvHistoryTransaction.adapter=adapter
        viewBinding.rvHistoryTransaction.layoutManager= LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
    }

    private fun clickEvent(item: Ticket?, position:Int?) {
        val intent= Intent(this,DetailHistoryActivity::class.java)
        intent.putExtra("TICKET",item)
        startActivity(intent)
    }

    fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivBack -> {
                finish()
            }
        }
    }

    override fun bindViewModel() {
        super.bindViewModel()
        viewModel.historyLiveData.observe(this, Observer {
            when (it?.status) {
                Resource.SUCCESS -> {
                    it.data?.let { response ->
                        viewBinding.swipeContainer.isRefreshing=false
                        adapter.addData(ArrayList(response))
                    }
                }
                Resource.LOADING -> {

                }
                Resource.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}