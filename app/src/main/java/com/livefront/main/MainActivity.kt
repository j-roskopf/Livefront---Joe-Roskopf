package com.livefront.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.livefront.R
import com.livefront.network.MovieService
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.experimental.async
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var movieService: MovieService

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        async {
            movieService.getUpcomingMovies("2018-05-31")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Log.d("D","responseDebug - all good! " + it.results?.size)
                    }, {
                        Log.d("D","responseDebug - bad! ${it.localizedMessage}")
                    })
        }

    }
}
