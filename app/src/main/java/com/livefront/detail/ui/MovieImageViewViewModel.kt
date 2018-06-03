package com.livefront.detail.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.livefront.model.network.image.ImageResponse
import com.livefront.model.network.image.MovieImage
import com.livefront.network.MovieService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MovieImageViewViewModel @Inject constructor(private val movieService: MovieService) : ViewModel() {

    //LiveData to be observed by the activity to be notificed when the API updates
    internal val imageResponse: MutableLiveData<ImageResponse?> = MutableLiveData()

    /**
     * Given an ID, fetch the images for our movie.
     *
     * This method is responsible for updating our LiveData after the API returns
     *
     * @param id - the ID of the Movie
     */
    internal fun getImagesForMovie(id: String) {
        movieService.getImagesForMovie(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    imageResponse.postValue(response)
                }, {
                    imageResponse.postValue(null)
                })
    }

    /**
     * Given an image response, return an ArrayList containing all of the posters and all of the backdrops contained inside the [ImageResponse]
     *
     * @param imageResponse - the response from the API
     *
     * @return An ArrayList of [MovieImage] that is an amalgamation of the posters and backdrops contained inside our response
     */
    fun getImagesFromResponse(imageResponse: ImageResponse?): ArrayList<MovieImage> {
        val toReturn = arrayListOf<MovieImage>()

        //get all the backdrops
        imageResponse?.backdrops?.forEach {
            val movieImage = MovieImage(
                    width = it.width,
                    height = it.height,
                    filePath = it.filePath,
                    aspectRatio = it.aspectRatio
            )

            toReturn.add(movieImage)
        }

        //get all the posters
        imageResponse?.posters?.forEach {
            val movieImage = MovieImage(
                    width = it.width,
                    height = it.height,
                    filePath = it.filePath,
                    aspectRatio = it.aspectRatio
            )

            toReturn.add(movieImage)
        }

        return toReturn
    }
}