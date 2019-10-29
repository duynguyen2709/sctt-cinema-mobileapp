package com.example.cgv.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.cgv.api.RetrofitClient
import com.example.cgv.api.Service
import com.example.cgv.model.*
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class UserRepository {
    val historyTransactionLiveData = MutableLiveData<Resource<List<Ticket>>>()
    private val service = RetrofitClient.youtubeInstance.create(Service::class.java)
    private var historyTransactionDisposable: Disposable? = null
    fun getListHistory(email:String) {
        historyTransactionDisposable?.dispose()
        service.getHistoryTransaction(email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<List<Ticket>>> {
                override fun onComplete() {
                    historyTransactionDisposable?.dispose()
                }

                override fun onSubscribe(d: Disposable) {
                    historyTransactionDisposable = d
                }

                override fun onNext(t: Response<List<Ticket>>) {
                    if (t.returnCode == 1) {
                        historyTransactionLiveData.value = Resource.success(t.data, null)
                    }
                }

                override fun onError(e: Throwable) {
                    Log.d("DuyError", e.toString())
                    historyTransactionLiveData.value = Resource.error(null, null)
                }

            })
    }

}
