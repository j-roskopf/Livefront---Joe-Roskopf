package com.livefront.main.ui

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.livefront.R
import com.livefront.main.adapter.GenericMovieAdapter
import com.livefront.model.network.MovieResponse
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private val upcomingGenericMovieAdapter: GenericMovieAdapter by lazy { GenericMovieAdapter(this) }
    private val mostPopularAllTimeGenericMovieAdapter: GenericMovieAdapter by lazy { GenericMovieAdapter(this) }
    private val mostPopularYearGenericMovieAdapter: GenericMovieAdapter by lazy { GenericMovieAdapter(this) }

    @Inject
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerViews()

        //fetch upcoming
        mainViewModel.fetchMoviesFromCallType(upcomingGenericMovieAdapter.getAndIcrementPage(), CallType.Upcoming)
        mainViewModel.upcomingMovies.observe(this as LifecycleOwner, Observer { movieResponse ->
            displayData(
                    movieResponse = movieResponse,
                    adapter = upcomingGenericMovieAdapter,
                    recyclerView = mainViewUpcomingRecyclerView,
                    callType = CallType.Upcoming
            )
        })

        //fetch most popular all time
        mainViewModel.fetchMoviesFromCallType(mostPopularAllTimeGenericMovieAdapter.getAndIcrementPage(), CallType.MostPopularAllTime)
        mainViewModel.mostPopularAllTimeMovies.observe(this as LifecycleOwner, Observer { movieResponse ->
            displayData(
                    movieResponse = movieResponse,
                    adapter = mostPopularAllTimeGenericMovieAdapter,
                    recyclerView = mainViewMostPopularAllTimeRecyclerView,
                    callType = CallType.MostPopularAllTime
            )
        })

        //fetch most popular current year
        mainViewModel.fetchMoviesFromCallType(mostPopularAllTimeGenericMovieAdapter.getAndIcrementPage(), CallType.MostPopularCurrentYear)
        mainViewModel.mostPopularCurrentYearMovies.observe(this as LifecycleOwner, Observer { movieResponse ->
            displayData(
                    movieResponse = movieResponse,
                    adapter = mostPopularYearGenericMovieAdapter,
                    recyclerView = mainViewMostPopularCurrentYearRecyclerView,
                    callType = CallType.MostPopularCurrentYear
            )
        })
    }

    /**
     * Gives each one of the RecyclerViews their LayoutManagers and Adapters.
     *
     * We don't want to do this when the data comes in to the RecyclerView otherwise the position that the user
     * was currently on in the RecyclerView would be reset. So it is sufficient to just set the LayoutManager / Adapter
     * once and just notify the Adapter when data is inserted
     */
    private fun setupRecyclerViews() {
        //Upcoming
        val upcomingLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mainViewUpcomingRecyclerView.layoutManager = upcomingLayoutManager
        mainViewUpcomingRecyclerView.adapter = upcomingGenericMovieAdapter

        //Most popular all time
        val mostPopularAllTimeLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mainViewMostPopularAllTimeRecyclerView.layoutManager = mostPopularAllTimeLayoutManager
        mainViewMostPopularAllTimeRecyclerView.adapter = mostPopularAllTimeGenericMovieAdapter

        //most popular current year
        val mostPopularYearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mainViewMostPopularCurrentYearRecyclerView.layoutManager = mostPopularYearLayoutManager
        mainViewMostPopularCurrentYearRecyclerView.adapter = mostPopularYearGenericMovieAdapter
    }

    private fun displayData(movieResponse: MovieResponse?, adapter: GenericMovieAdapter, recyclerView: RecyclerView, callType: CallType) {
        movieResponse?.results?.let {
            val currentAdapterSize = adapter.itemCount
            updateAdapterWithResponse(movieResponse, adapter)
            adapter.setOnLoadMoreListener(mainViewModel.getLoadMoreListenerFromType(adapter, recyclerView, callType))
            adapter.notifyItemInserted(currentAdapterSize)
        }
    }

    private fun updateAdapterWithResponse(movieResponse: MovieResponse, adapter: GenericMovieAdapter) {
        adapter.removeLoadingItem()
        adapter.isLoading = false
        adapter.setResults(movieResponse.results ?: listOf())
        adapter.totalAmountOfPages = movieResponse.totalPages ?: 0
    }

}
