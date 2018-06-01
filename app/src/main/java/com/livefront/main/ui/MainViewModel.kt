package com.livefront.main.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.v7.widget.RecyclerView
import com.livefront.main.adapter.GenericMovieAdapter
import com.livefront.main.adapter.OnLoadMoreListener
import com.livefront.model.network.MovieResponse
import com.livefront.network.MostPopularAllTimeCall
import com.livefront.network.MostPopularYearCall
import com.livefront.network.MovieService
import com.livefront.network.UpcomingMovieCall
import javax.inject.Inject

class MainViewModel @Inject constructor(movieService: MovieService) : ViewModel() {

    internal val upcomingMovies: MutableLiveData<MovieResponse?> = MutableLiveData()
    private val upcomingMovieCall = UpcomingMovieCall(movieService)

    internal val mostPopularAllTimeMovies: MutableLiveData<MovieResponse?> = MutableLiveData()
    private val mostPopularAllTimeCall = MostPopularAllTimeCall(movieService)

    internal val mostPopularCurrentYearMovies: MutableLiveData<MovieResponse?> = MutableLiveData()
    private val mostPopularCurrentYearCall = MostPopularYearCall(movieService)

    fun fetchMoviesFromCallType(page: Int, callType: CallType) {
        when(callType) {
            CallType.Upcoming -> upcomingMovieCall.fetchMovies(page, upcomingMovies)
            CallType.MostPopularAllTime -> mostPopularAllTimeCall.fetchMovies(page, mostPopularAllTimeMovies)
            CallType.MostPopularCurrentYear -> mostPopularCurrentYearCall.fetchMovies(page, mostPopularCurrentYearMovies)
        }
    }

    fun getLoadMoreListenerFromType(adapter: GenericMovieAdapter, recyclerView: RecyclerView, callType: CallType): OnLoadMoreListener {
        return when(callType) {
            CallType.Upcoming -> upcomingMovieCall.provideOnLoadMoreListener(adapter, recyclerView, upcomingMovies)
            CallType.MostPopularAllTime -> mostPopularAllTimeCall.provideOnLoadMoreListener(adapter, recyclerView, mostPopularAllTimeMovies)
            CallType.MostPopularCurrentYear -> mostPopularCurrentYearCall.provideOnLoadMoreListener(adapter, recyclerView, mostPopularCurrentYearMovies)
        }
    }

}

enum class CallType {
    Upcoming,
    MostPopularAllTime,
    MostPopularCurrentYear
}