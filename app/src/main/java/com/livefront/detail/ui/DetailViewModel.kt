package com.livefront.detail.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.livefront.model.network.detail.DetailResponse
import com.livefront.network.MovieService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailViewModel @Inject constructor(private val movieService: MovieService) : ViewModel() {

    internal val detailResponse: MutableLiveData<DetailResponse?> = MutableLiveData()

    internal fun getMovieDetails(id: String) {
        movieService.getDetailsForMovie(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    detailResponse.postValue(response)
                }, {
                    detailResponse.postValue(null)
                })
    }
}