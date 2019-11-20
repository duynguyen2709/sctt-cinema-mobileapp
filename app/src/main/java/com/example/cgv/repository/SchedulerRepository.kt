package com.example.cgv.repository

import androidx.lifecycle.MutableLiveData
import com.example.cgv.api.RetrofitClient
import com.example.cgv.api.Service
import com.example.cgv.model.Resource
import com.example.cgv.model.Response
import com.example.cgv.model.Theater
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SchedulerRepository {
    val theaterLiveData = MutableLiveData<Resource<Map<String, List<Theater>>>>()
    private val service = RetrofitClient.youtubeInstance.create(Service::class.java)
    private var schedulerDisposable: Disposable? = null

    fun getListTheater() {
        schedulerDisposable?.dispose()
        service.getListTheater()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : io.reactivex.Observer<Response<Map<String, List<Theater>>>> {
                override fun onComplete() {
                    schedulerDisposable?.dispose()
                }

                override fun onSubscribe(d: Disposable) {
                    schedulerDisposable = d
                }

                override fun onNext(t: Response<Map<String, List<Theater>>>) {
                    theaterLiveData.value = Resource.success(t.data, null)
                }

                override fun onError(e: Throwable) {
                    theaterLiveData.value = Resource.error(null, null)
                }

            })
    }
}