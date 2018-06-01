package com.livefront.model.network

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.livefront.BuildConfig
import kotlinx.android.parcel.Parcelize

private const val WIDTH_185_POSTER_QUALIFIER = "w185"

@Parcelize
data class Result(
        @SerializedName("vote_count")
        @Expose
        var voteCount: Int? = null,
        @SerializedName("id")
        @Expose
        var id: Int? = null,
        @SerializedName("video")
        @Expose
        var video: Boolean? = null,
        @SerializedName("vote_average")
        @Expose
        var voteAverage: Double? = null,
        @SerializedName("title")
        @Expose
        var title: String? = null,
        @SerializedName("popularity")
        @Expose
        var popularity: Double? = null,
        @SerializedName("poster_path")
        @Expose
        var posterPath: String? = null,
        @SerializedName("original_language")
        @Expose
        var originalLanguage: String? = null,
        @SerializedName("original_title")
        @Expose
        var originalTitle: String? = null,
        @SerializedName("genre_ids")
        @Expose
        var genreIds: List<Int>? = null,
        @SerializedName("backdrop_path")
        @Expose
        var backdropPath: String? = null,
        @SerializedName("adult")
        @Expose
        var adult: Boolean? = null,
        @SerializedName("overview")
        @Expose
        var overview: String? = null,
        @SerializedName("release_date")
        @Expose
        var releaseDate: String? = null
) : Parcelable {

    internal fun getFullPosterPathW185(): String? {
        posterPath?.let {
            return BuildConfig.BASE_POSTER_PATH.plus(WIDTH_185_POSTER_QUALIFIER).plus(posterPath)
        }
        return null
    }
}