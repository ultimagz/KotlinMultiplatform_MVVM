/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.jarroyo.sharedcode.viewModel

import com.jarroyo.sharedcode.base.Response
import com.jarroyo.sharedcode.di.KodeinInjector
import com.jarroyo.sharedcode.domain.usecase.counter.GetCounterRequest
import com.jarroyo.sharedcode.domain.usecase.counter.GetCounterUseCase
import com.jarroyo.sharedcode.utils.coroutines.launchSilent
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import org.kodein.di.erased.instance
import kotlin.coroutines.CoroutineContext

class CounterViewModel(
) : ViewModel() {
    var mGetCounterLiveData = MutableLiveData<GetCounterState>(LoadingGetCounterState())

    private val mGetCounterUseCase by KodeinInjector.instance<GetCounterUseCase>()
    private val coroutineContext by KodeinInjector.instance<CoroutineContext>()
    private var job: Job = Job()
    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
    }


    /**
     * GET COUNTER
     */
    fun getCounter() = launchSilent(coroutineContext, exceptionHandler, job) {
        mGetCounterLiveData.postValue(LoadingGetCounterState())

        //Logger.d("COUNTER VIEWMODEL", "my message")
        val request = GetCounterRequest()
        val response = mGetCounterUseCase.execute(request)
        processSaveUserResponse(response)
    }

    fun processSaveUserResponse(response: Response<Int>){
        if (response is Response.Success) {
            mGetCounterLiveData.postValue(
                SuccessGetCounterState(
                    response
                )
            )
        } else if (response is Response.Error) {
            mGetCounterLiveData.postValue(
                SuccessGetCounterState(
                    response
                )
            )
        }
    }

}