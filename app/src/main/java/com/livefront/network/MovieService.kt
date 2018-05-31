package com.livefront.network

import com.livefront.BuildConfig
import com.livefront.model.network.MovieResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET("discover/movie?api_key=${BuildConfig.API_KEY}")
    fun getUpcomingMovies(@Query("primary_release_date.gte") primaryReleaseDate: String): Observable<MovieResponse>
}
