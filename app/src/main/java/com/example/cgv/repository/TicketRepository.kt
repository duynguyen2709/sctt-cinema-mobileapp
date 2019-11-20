package com.example.cgv.repository

import androidx.lifecycle.MutableLiveData
import com.example.cgv.api.RetrofitClient
import com.example.cgv.api.Service
import com.example.cgv.model.Resource
import com.example.cgv.model.Response
import com.example.cgv.model.ShowTimes
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class TicketRepository {
    val showtimeLiveData = MutableLiveData<Resource<ShowTimes>>()
    private val service = RetrofitClient.youtubeInstance.create(Service::class.java)
    private var ticketDisposable: Disposable? = null

    fun getShowTimes(type: Int, id: String, date: String){
        ticketDisposable?.dispose()
        service.getListShowTimes(type, id, date)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : io.reactivex.Observer<Response<ShowTimes>> {
                override fun onComplete() {
                    ticketDisposable?.dispose()
                }

                override fun onSubscribe(d: Disposable) {
                    ticketDisposable = d
                }

                override fun onNext(t: Response<ShowTimes>) {
                    showtimeLiveData.value = Resource.success(t.data, null)
                }

                override fun onError(e: Throwable) {
                    showtimeLiveData.value = Resource.error(null, null)
                }

            })
    }
}