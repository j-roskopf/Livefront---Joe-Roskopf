package com.livefront.network

import com.livefront.BuildConfig
import com.livefront.model.network.MovieResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET("discover/movie?api_key=${BuildConfig.API_KEY}")
    fun getUpcomingMovies(@Query("primary_release_date.gte") primaryReleaseDate: String, @Query("page") page: Int): Observable<MovieResponse>

    @GET("discover/movie?sort_by=popularity.desc&api_key=${BuildConfig.API_KEY}")
    fun getAllTimeMostPopularMovies(@Query("page") page: Int): Observable<MovieResponse>

    @GET("discover/movie?sort_by=vote_average.desc&api_key=${BuildConfig.API_KEY}")
    fun getMostPopuarMovieCurrentYear(@Query("primary_release_year") primaryReleaseYear: String, @Query("page") page: Int): Observable<MovieResponse>
}
