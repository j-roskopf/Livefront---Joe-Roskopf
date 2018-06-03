package com.livefront.detail.ui

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.widget.Toast
import com.livefront.BaseActivity
import com.livefront.R
import com.livefront.detail.adapter.MovieImageAdapter
import com.livefront.model.network.image.ImageResponse
import kotlinx.android.synthetic.main.activity_movie_image_view.*
import javax.inject.Inject

private const val MOVIE_IMAGE_TAG = "MovieImageViewActivity"

class MovieImageViewActivity : BaseActivity() {

    @Inject
    lateinit var movieImageViewModel: MovieImageViewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_image_view)

        //set back button enabled in toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val id = intent.extras[EXTRA_BUNDLE_MOVIE_ID] as? String

        //If we have the id from the movie to display
        if (id != null) {
            //fetch the images of the movie
            movieImageViewModel.getImagesForMovie(id)

            //and observe the changes
            movieImageViewModel.imageResponse.observe(this as LifecycleOwner, Observer { imageResponse ->
                displayImages(imageResponse)
            })
        } else {
            //otherwise if the movie did not have a valid id, display and error and back out
            Toast.makeText(this, R.string.error_toast, Toast.LENGTH_SHORT).show()
            Log.e(MOVIE_IMAGE_TAG, "We weren't able to view the images on a movie")
            finish()
        }

    }

    /**
     * Handles the back button being pressed in the toolbar
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     * Once the API has returned with a response, collect all of the posters and backdrops and display it in our RecyclerView
     *
     * @param imageResponse - the response from the API
     */
    private fun displayImages(imageResponse: ImageResponse?) {
        // Create an instance of the GreedoLayoutManager and pass it to the RecyclerView
        val imageAdapter = MovieImageAdapter(movieImageViewModel.getImagesFromResponse(imageResponse))
        val layoutManager = GridLayoutManager(this, 2)

        movieDetailImageRecyclerView.layoutManager = layoutManager
        movieDetailImageRecyclerView.adapter = imageAdapter

    }


    companion object {
        const val EXTRA_BUNDLE_MOVIE_ID = "movie_id"
    }
}
