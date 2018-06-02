package com.livefront.model.network.image

import com.livefront.BuildConfig
import com.livefront.model.network.Result

/**
 * This class id kind of redundant, but I want a way for the adapter to take a list of only 1 type,
 * but the API returns a list of backdrops and a list of posters, which both contain the same properties.
 *
 * So this is just a dummy class that have the properties we need from [Backdrop] and [Poster]
 */
data class MovieImage(var aspectRatio: Double? = null, var filePath: String? = null, var width: Int? = null, var height: Int? = null) {
    fun getFullImageUrl(path: String): String {
        return BuildConfig.BASE_POSTER_PATH.plus(Result.WIDTH_500_POSTER_QUALIFIER).plus(path)
    }
}