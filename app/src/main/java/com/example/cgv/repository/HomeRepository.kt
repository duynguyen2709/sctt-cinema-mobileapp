package com.example.cgv.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.cgv.api.RetrofitClient
import com.example.cgv.api.Service
import com.example.cgv.model.HomeInfo
import com.example.cgv.model.Resource
import com.example.cgv.model.Response
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class HomeRepository {
    val homeInfoLiveData = MutableLiveData<Resource<HomeInfo>>()
    private val service = RetrofitClient.youtubeInstance.create(Service::class.java)
    private var homeInfoDisposable: Disposable? = null
    fun getHomeInfo() {
        homeInfoDisposable?.dispose()
        service.getHomeInfo()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<HomeInfo>> {
                override fun onComplete() {
                    homeInfoDisposable?.dispose()
                }

                override fun onSubscribe(d: Disposable) {
                    homeInfoDisposable = d
                }

                override fun onNext(t: Response<HomeInfo>) {
                    if (t.returnCode == 1) {
                        homeInfoLiveData.value = Resource.success(t.data, null)
                    }
                }

                override fun onError(e: Throwable) {
                    Log.d("DuyError", e.toString())
                    homeInfoLiveData.value = Resource.error(null, null)
                }

            })
    }
}