package com.example.cgv.repository

import androidx.lifecycle.MutableLiveData
import com.example.cgv.api.RetrofitClient
import com.example.cgv.api.Service
import com.example.cgv.model.Resource
import com.example.cgv.model.Response
import com.example.cgv.model.Seat
import com.example.cgv.model.SeatMapResponse
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class RoomTheaterRepository {
    val seatLiveData = MutableLiveData<Resource<SeatMapResponse>>()
    private val service = RetrofitClient.youtubeInstance.create(Service::class.java)
    private var schedulerDisposable: Disposable? = null

    fun getSeatMap(showTimeID: String) {
        schedulerDisposable?.dispose()
        service.getSeatMap(showTimeID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<SeatMapResponse>> {
                override fun onComplete() {
                    schedulerDisposable?.dispose()
                }

                override fun onSubscribe(d: Disposable) {
                    schedulerDisposable = d
                }

                override fun onNext(t: Response<SeatMapResponse>) {
                    if (t.returnCode == 1) {
                        seatLiveData.value = Resource.success(t.data, null)
                    }
                }

                override fun onError(e: Throwable) {
                    seatLiveData.value = Resource.error(null, null)
                }

            })
    }
}