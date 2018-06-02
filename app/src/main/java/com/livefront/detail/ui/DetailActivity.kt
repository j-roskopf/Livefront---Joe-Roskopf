package com.livefront.detail.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.livefront.R
import com.livefront.model.network.Result
import com.livefront.model.network.detail.DetailResponse
import com.livefront.network.MovieService
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.detail_result_content.*
import javax.inject.Inject


class DetailActivity : AppCompatActivity() {

    @Inject
    lateinit var movieService: MovieService

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val result = intent.extras[EXTRA_BUNDLE_RESULT] as? Result

        result?.let {
            displayResultFromOverview(it)

            movieService.getDetailsForMovie(it.id.toString()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ response ->
                displayResultsFromDetail(response)
            }, {
                Log.d("D","detailDebug - bad ${it.message}")
            })
        }

        this.detailToolbar.title = result?.title
    }

    private fun displayResultsFromDetail(response: DetailResponse?) {
        //genres, budget, spoken lang, runtime
        response?.let {
            var languages = ""
            it.spokenLanguages?.forEach { languages += it.name + " " }
            detailLanguageBody.text = languages

            var genres = ""
            it.genres?.forEach { genres += it.name + " " }
            detailGenreBody.text = genres

            if(it.budget != 0) {
                detailBudgetBody.text = it.budget.toString()
            } else {
                detailBudgetBody.text = "No budget provided"
            }

            if(it.revenue != 0) {
                detailRevenueBody.text = it.revenue.toString()
            } else {
                detailRevenueBody.text = "No revenue provided"
            }

            detailRuntimeBody.text = getString(R.string.details_runtime, it.runtime.toString())

            detailGroup.visibility = View.VISIBLE
            detailProgressBar.visibility = View.GONE
        }

    }

    private fun displayResultFromOverview(result: Result) {
        //overview, release date, rating, FROM DETAIL genres, budget, spoken lang, runtime,
        detailOverviewBody.text = result.overview
        detailsRatingBar.rating = result.voteAverage?.toFloat() ?: 0.0f
        detailsVoteCountBody.text = getString(R.string.detail_vote_count, result.voteCount.toString())
        detailsReleaseOverview.text = result.releaseDate
        Picasso.get().load(result.getFullPosterPathW500()).into(detailViewImage)
    }


    companion object {
        const val EXTRA_BUNDLE_RESULT = "result"
    }
}
