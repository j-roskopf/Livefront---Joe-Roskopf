package com.livefront.network

import android.arch.lifecycle.MutableLiveData
import android.support.v7.widget.RecyclerView
import com.livefront.android.extensions.getCurrentYear
import com.livefront.main.adapter.GenericMovieAdapter
import com.livefront.main.adapter.OnLoadMoreListener
import com.livefront.model.network.MovieResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class MostPopularYearCall @Inject constructor(private val movieService: MovieService): MovieCallInterface {

    /**
     * Fetches most popular movies of the current year! This method is the start of the chain that will eventually update
     * our [MutableLiveData] object with the [MovieResponse] that we received from the API
     *
     * @param page - the current page to fetch results for (Note - page starts at 1)
     */
    override fun fetchMovies(page: Int, liveData: MutableLiveData<MovieResponse?>) {
        movieService.getMostPopuarMovieCurrentYear(Calendar.getInstance().getCurrentYear(), page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    parseResponse(response, liveData)
                }, {
                    liveData.value = null
                })
    }

    /**
     * Parses the [MovieResponse] we received from the API for most popular movies of the current year.
     *
     * This method is responsible for grabbing the data we received from the API and
     * appending it to the data we currently have
     *
     * @param response - the [MovieResponse] received from the API
     */
    override fun parseResponse(response: MovieResponse, liveData: MutableLiveData<MovieResponse?>) {
        //Grab what we currently have (or initialize to new objects if what we have is null)
        //which will happen the first time around
        var currentMovieResponse = liveData.value ?: MovieResponse()
        var currentResults = currentMovieResponse.results ?: listOf()
        response.results?.let {
            //assuming we have good data, append it to what we currently have and post it to our LiveData
            currentResults += it
            currentMovieResponse = response
            currentMovieResponse.results = currentResults
            liveData.postValue(currentMovieResponse)
        }
    }

    /**
     * Returns the implementation of [OnLoadMoreListener] for . If our adapter can load more items,
     * this method triggers the call to get the next page of results
     *
     * @param adapter - the adapter for our RecyclerView
     * @param recyclerView - the RecyclerView that is the recipient of the movies that will be fetched from the API
     */
    override fun provideOnLoadMoreListener(adapter: GenericMovieAdapter, recyclerView: RecyclerView, liveData: MutableLiveData<MovieResponse?>): OnLoadMoreListener {
        return object : OnLoadMoreListener {
            override fun loadMore() {
                //Post on recyclerView to avoid calling update to adapter while RV is still computing scrolls / layouts
                recyclerView.post {
                    if(adapter.canAddMoreItems()) {
                        adapter.addLoadingItem()
                        adapter.notifyItemInserted(adapter.itemCount)
                        fetchMovies(adapter.getAndIcrementPage(), liveData)
                    }
                }
            }
        }
    }

}