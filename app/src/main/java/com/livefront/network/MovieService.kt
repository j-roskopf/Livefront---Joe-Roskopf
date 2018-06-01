package com.livefront.network

import com.livefront.BuildConfig
import com.livefront.model.network.MovieResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    /**
     * Get upcoming movies
     *
     * We define this by movies with a release date today or beyond
     *
     * Default sorting
     */
    @GET("discover/movie?api_key=${BuildConfig.API_KEY}")
    fun getUpcomingMovies(@Query("primary_release_date.gte") primaryReleaseDate: String, @Query("page") page: Int): Observable<MovieResponse>

    /**
     * Get the most popular movies of all time
     *
     * We define this by movies with the highest user rating score released any date
     *
     * Sorted by vote average descending
     */
    @GET("discover/movie?sort_by=popularity.desc&api_key=${BuildConfig.API_KEY}")
    fun getAllTimeMostPopularMovies(@Query("page") page: Int): Observable<MovieResponse>

    /**
     * Get the most popular movies of the current year
     *
     * We define this by any movies with the highest user rating score with a release date of this year
     *
     * Sorted by vote average descending
     */
    @GET("discover/movie?sort_by=vote_average.desc&api_key=${BuildConfig.API_KEY}")
    fun getMostPopuarMovieCurrentYear(@Query("primary_release_year") primaryReleaseYear: String, @Query("page") page: Int): Observable<MovieResponse>

    /**
     * Get the movies currently in theatre
     *
     * We define this by any movie released before today and after ~3 weeks ago
     *
     * Default sorting
     */
    @GET("discover/movie?api_key=${BuildConfig.API_KEY}")
    fun getMoviesCurrentlyInTheatre(@Query("primary_release_date.gte") primaryReleaseYearAfter: String,
                                    @Query("primary_release_date.lte") primaryReleaseYearBefore: String,
                                    @Query("page") page: Int): Observable<MovieResponse>
}