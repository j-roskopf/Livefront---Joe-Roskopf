package com.livefront.main.ui

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.livefront.BaseActivity
import com.livefront.R
import com.livefront.detail.ui.DetailActivity
import com.livefront.main.adapter.GenericMovieAdapter
import com.livefront.main.adapter.ItemInteractionListener
import com.livefront.model.network.MovieResponse
import com.livefront.model.network.Result
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

private const val TAG = "MainActivity"
class MainActivity : BaseActivity(), ItemInteractionListener {

    private val upcomingGenericMovieAdapter: GenericMovieAdapter by lazy { GenericMovieAdapter(this) }
    private val mostPopularAllTimeGenericMovieAdapter: GenericMovieAdapter by lazy { GenericMovieAdapter(this) }
    private val theatreMovieGenericAdapter: GenericMovieAdapter by lazy { GenericMovieAdapter(this) }

    @Inject
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerViews()

        fetchData()
    }

    /**
     * Starts off API calls + observers for our 3 RecyclerViews
     */
    private fun fetchData() {

        //fetch upcoming
        mainViewModel.fetchMoviesFromCallType(upcomingGenericMovieAdapter.getAndIncrementPage(), CallType.Upcoming)
        mainViewModel.upcomingMovies.observe(this as LifecycleOwner, Observer { movieResponse ->
            displayData(
                    movieResponse = movieResponse,
                    adapter = upcomingGenericMovieAdapter,
                    recyclerView = mainViewUpcomingRecyclerView,
                    callType = CallType.Upcoming
            )
        })

        //fetch most popular all time
        mainViewModel.fetchMoviesFromCallType(mostPopularAllTimeGenericMovieAdapter.getAndIncrementPage(), CallType.MostPopularAllTime)
        mainViewModel.mostPopularAllTimeMovies.observe(this as LifecycleOwner, Observer { movieResponse ->
            displayData(
                    movieResponse = movieResponse,
                    adapter = mostPopularAllTimeGenericMovieAdapter,
                    recyclerView = mainViewMostPopularAllTimeRecyclerView,
                    callType = CallType.MostPopularAllTime
            )
        })

        //fetch movies currently in theatre
        mainViewModel.fetchMoviesFromCallType(theatreMovieGenericAdapter.getAndIncrementPage(), CallType.Theatre)
        mainViewModel.theatreMovies.observe(this as LifecycleOwner, Observer { movieResponse ->
            displayData(
                    movieResponse = movieResponse,
                    adapter = theatreMovieGenericAdapter,
                    recyclerView = mainViewTheatreMoviesRecyclerView,
                    callType = CallType.Theatre
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

        //movies in theatre
        val theatreLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mainViewTheatreMoviesRecyclerView.layoutManager = theatreLayoutManager
        mainViewTheatreMoviesRecyclerView.adapter = theatreMovieGenericAdapter
    }

    private fun displayData(movieResponse: MovieResponse?, adapter: GenericMovieAdapter, recyclerView: RecyclerView, callType: CallType) {
        if(movieResponse != null) {
            movieResponse.results?.let {
                val currentAdapterSize = adapter.itemCount
                updateAdapterWithResponse(movieResponse, adapter)
                adapter.setOnLoadMoreListener(mainViewModel.getLoadMoreListenerFromType(adapter, recyclerView, callType))
                adapter.notifyItemInserted(currentAdapterSize)
            }
        } else {
            Snackbar.make(mainBaseLayout, R.string.generic_error_message, Snackbar.LENGTH_LONG).setAction(R.string.try_again, {
                fetchData()
            }).show()
            Log.e(TAG, "We tried displaying some data in $TAG and it was bad. Check previous Log.e calls for API errors")
        }
    }

    private fun updateAdapterWithResponse(movieResponse: MovieResponse, adapter: GenericMovieAdapter) {
        adapter.removeLoadingItem()
        adapter.isLoading = false
        adapter.results = movieResponse.results ?: listOf()
        adapter.totalAmountOfPages = movieResponse.totalPages ?: 0
    }

    /**
     * Callback for item click. Sends the user to the DetailScreen
     *
     * @param result - the clicked on result
     * @param image - the shared view between the main and the detail screen
     */
    override fun onItemClicked(result: Result, image: ImageView) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_BUNDLE_RESULT, result)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, image as View, getString(R.string.movieDetailImageTransitionName))
        startActivity(intent, options.toBundle())
    }
}
