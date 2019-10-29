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

class AuthenticationRepository {
    val userInfoLiveData = MutableLiveData<Resource<AuthenticationResponse>>()
    private val service = RetrofitClient.youtubeInstance.create(Service::class.java)
    private var userInfoDisposable: Disposable? = null
    fun postSignUp(param:User) {
        userInfoDisposable?.dispose()
        service.postSignUp(param)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<AuthenticationResponse>> {
                override fun onComplete() {
                    userInfoDisposable?.dispose()
                }

                override fun onSubscribe(d: Disposable) {
                    userInfoDisposable = d
                }

                override fun onNext(t: Response<AuthenticationResponse>) {
                    if (t.returnCode == 1) {
                        userInfoLiveData.value = Resource.success(t.data, null)
                    }
                }

                override fun onError(e: Throwable) {
                    Log.d("DuyError", e.toString())
                    userInfoLiveData.value = Resource.error(null, null)
                }

            })
    }

    fun postLogIn(param:LoginParam) {
        userInfoDisposable?.dispose()
        service.postLogin(param)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<AuthenticationResponse>> {
                override fun onComplete() {
                    userInfoDisposable?.dispose()
                }

                override fun onSubscribe(d: Disposable) {
                    userInfoDisposable = d
                }

                override fun onNext(t: Response<AuthenticationResponse>) {
                    if (t.returnCode == 1) {
                        userInfoLiveData.value = Resource.success(t.data, null)
                    }
                }

                override fun onError(e: Throwable) {
                    Log.d("DuyError", e.toString())
                    userInfoLiveData.value = Resource.error(null, null)
                }

            })
    }
}
