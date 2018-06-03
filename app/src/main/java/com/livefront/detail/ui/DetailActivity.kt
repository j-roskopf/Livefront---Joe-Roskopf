package com.livefront.detail.ui

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.widget.Toast
import com.livefront.BaseActivity
import com.livefront.R
import com.livefront.model.network.Result
import com.livefront.model.network.detail.DetailResponse
import com.livefront.network.MovieService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.detail_result_content.*
import java.text.NumberFormat
import javax.inject.Inject

private const val DETAIL_TAG = "Detail"

class DetailActivity : BaseActivity() {

    //Movie service for communicating with the API
    @Inject
    lateinit var movieService: MovieService

    @Inject
    lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        //Avoids a double toolbar with our coordinator layout
        setSupportActionBar(toolbar)

        val result = intent.extras[EXTRA_BUNDLE_RESULT] as? Result

        //If our result is good
        if (result != null) {
            //display the relevant items to our UI
            displayResultFromOverview(result)

            //Update the toolbar to have the movie title
            this.detailToolbar.title = result.title

            //Fetch more details about the movie that we don't have from our first API call
            detailViewModel.getMovieDetails(result.id.toString())

            //And observe the changes
            detailViewModel.detailResponse.observe(this as LifecycleOwner, Observer { response ->
                if (response != null) {
                    displayResultsFromDetail(response)
                } else {
                    Snackbar.make(detailBaseLayout, R.string.detail_api_error, Snackbar.LENGTH_SHORT).show()
                }
            })

            //Handle the onclick for our FAB which will send the user to the image viewing Activity
            detailImageFab.setOnClickListener {
                val intent = Intent(this, MovieImageViewActivity::class.java)
                intent.putExtra(MovieImageViewActivity.EXTRA_BUNDLE_MOVIE_ID, result.id.toString())
                startActivity(intent)
            }
        } else {
            Toast.makeText(this, R.string.error_toast, Toast.LENGTH_SHORT).show()
            Log.e(DETAIL_TAG, "We weren't able to view the details on a movie")
            finish()
        }


    }

    /**
     * Updates the UI with the items contained in our [DetailResponse] object
     *
     * @param response - the response from the API containing details about the movie
     */
    private fun displayResultsFromDetail(response: DetailResponse) {
        //set languages
        var languages = ""
        response.spokenLanguages?.forEach { languages += it.name + " " }
        detailLanguageBody.text = languages

        //set genres
        var genres = ""
        response.genres?.forEach { genres += it.name + " " }
        detailGenreBody.text = genres

        //set the budget (handling if the API provided no value)
        if (response.budget != 0) {
            detailBudgetBody.text = NumberFormat.getCurrencyInstance().format(response.budget)
        } else {
            detailBudgetBody.text = getString(R.string.detail_no_budget)
        }

        //set the revenue (handling if the API provided no value)
        if (response.revenue != 0) {
            detailRevenueBody.text = NumberFormat.getCurrencyInstance().format(response.revenue)
        } else {
            detailRevenueBody.text = getString(R.string.detail_no_revenue)
        }

        //set the runtime progress
        detailRuntimeBody.text = getString(R.string.details_runtime, response.runtime.toString())

        //Now that our API has returned successfully, hide the progress and show the UI
        hideProgress()
    }

    /**
     * Once this activity starts, we already have some information about the movie that the APi has already returned to us,
     * so display what we have
     *
     * @param result - Result of the data we already have from the API
     */
    private fun displayResultFromOverview(result: Result) {
        //set overview
        detailOverviewBody.text = result.overview

        //set vote average in the rating bar
        detailsRatingBar.rating = result.voteAverage?.toFloat() ?: 0.0f

        //set vote count
        detailsVoteCountBody.text = getString(R.string.detail_vote_count, result.voteCount.toString())

        //set release date
        detailsReleaseOverview.text = result.releaseDate

        //load the image
        Picasso.get().load(result.getFullPosterPathW500()).error(R.drawable.no_image_available)
                .placeholder(R.drawable.progress_animation).into(detailViewImage)
    }

    /**
     * Hides the progress view and shows the detail view hidden before the API returns
     */
    private fun hideProgress() {
        detailGroup.visibility = View.VISIBLE
        detailProgressBar.visibility = View.GONE
    }

    companion object {
        const val EXTRA_BUNDLE_RESULT = "result"
    }
}
