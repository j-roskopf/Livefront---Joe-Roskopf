package com.livefront.network

import android.arch.lifecycle.MutableLiveData
import android.support.v7.widget.RecyclerView
import com.livefront.main.adapter.GenericMovieAdapter
import com.livefront.main.adapter.OnLoadMoreListener
import com.livefront.model.network.MovieResponse


/**
 * This interface defines the contract for what each movie call is responsible to do. At a high level,
 * each call is responsible for using the API to fetch movies, parse that response, and returning what should happen
 * when the user wants to load more (Currently this is done by the user scrolling to the end of the RecyclerView)
 */
interface MovieCallInterface {
    fun fetchMovies(page: Int, liveData: MutableLiveData<MovieResponse?>)

    fun parseResponse(response: MovieResponse, liveData: MutableLiveData<MovieResponse?>)

    fun provideOnLoadMoreListener(adapter: GenericMovieAdapter, recyclerView: RecyclerView, liveData: MutableLiveData<MovieResponse?>): OnLoadMoreListener
}