package com.livefront.detail.ui

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import com.google.gson.Gson
import com.livefront.model.network.detail.DetailResponse
import com.livefront.network.MovieService
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.annotations.NonNull
import io.reactivex.annotations.Nullable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit


@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTests {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var mockedMovieService: MovieService
    private var observer = null

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        mockedMovieService = mock(MovieService::class.java)
        detailViewModel = DetailViewModel(mockedMovieService)

        mockSchedulers()
    }

    private fun mockSchedulers() {
        //Use custom Scheduler as the default schedulers are not mocked
        val immediate = object : Scheduler() {
            override fun scheduleDirect(@NonNull run: Runnable, delay: Long, @NonNull unit: TimeUnit): Disposable {
                // this prevents StackOverflowErrors when scheduling with a delay
                return super.scheduleDirect(run, 0, unit)
            }

            override fun createWorker(): Scheduler.Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
            }
        }

        RxJavaPlugins.setInitIoSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { scheduler -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }
    }

    @Test
    fun getMovieDetailsSuccessfullyTests() {
        /**
         * Tests a successful API call to get movie details
         */
        val id = "id"

        Mockito.`when`(mockedMovieService.getDetailsForMovie(id)).thenReturn(
                Observable.just(createResponseObjectFromSampleJson())
        )

        detailViewModel.getMovieDetails(id)

        val response = blockingObserve(detailViewModel.detailResponse)

        if (response == null) {
            fail("LiveData was null after a successful API call")
        }
    }

    @Test
    fun getMovieDetailsFailedTests() {
        /**
         * Tests an API call that fails
         */
        val id = "id"

        Mockito.`when`(mockedMovieService.getDetailsForMovie(id)).thenReturn(
                Observable.create {
                    //simulate some kind of error happening during the API call.
                    //Such as the API is down, or the user is not connected.
                    //For testing purposes I'm dividing by 0, a classic no-no
                    1 / (1 - 1)
                }
        )

        detailViewModel.getMovieDetails(id)

        val response = blockingObserve(detailViewModel.detailResponse)

        if (response != null) {
            fail("LiveData had a value after a failed API call")
        }

    }

    /**
     * Helper function to blockingly observe live data changes
     *
     * borrowed from here https://stackoverflow.com/questions/44270688/unit-testing-room-and-livedata
     */
    private fun <T> blockingObserve(liveData: LiveData<T>): T? {
        val data = arrayOfNulls<Any>(1)
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(@Nullable o: T?) {
                data[0] = o
                latch.countDown()
                liveData.removeObserver(this)
            }
        }
        liveData.observeForever(observer)
        latch.await(2, TimeUnit.SECONDS)
        //noinspection unchecked
        return data[0] as T
    }

    /**
     * Given a sample JSON string, returns a [DetailResponse] object
     *
     * @return DetailResponse
     */
    private fun createResponseObjectFromSampleJson(): DetailResponse {
        val sampleJson = "{\"adult\":false,\"backdrop_path\":\"/3P52oz9HPQWxcwHOwxtyrVV1LKi.jpg\",\"belongs_to_collection\":{\"id\":448150,\"name\":\"Deadpool Collection\",\"poster_path\":\"/30c5jO7YEXuF8KiWXLg9m28GWDA.jpg\",\"backdrop_path\":\"/hBQOWY8qWXJVFAc8yLTh1teIu43.jpg\"},\"budget\":110000000,\"genres\":[{\"id\":28,\"name\":\"Action\"},{\"id\":35,\"name\":\"Comedy\"},{\"id\":878,\"name\":\"Science Fiction\"}],\"homepage\":\"https://www.foxmovies.com/movies/deadpool-2\",\"id\":383498,\"imdb_id\":\"tt5463162\",\"original_language\":\"en\",\"original_title\":\"Deadpool 2\",\"overview\":\"Wisecracking mercenary Deadpool battles the evil and powerful Cable and other bad guys to save a boy's life.\",\"popularity\":339.090843,\"poster_path\":\"/to0spRl1CMDvyUbOnbb4fTk3VAd.jpg\",\"production_companies\":[{\"id\":28788,\"logo_path\":null,\"name\":\"Genre Films\",\"origin_country\":\"US\"},{\"id\":31076,\"logo_path\":null,\"name\":\"Simon Kinberg Productions\",\"origin_country\":\"\"},{\"id\":431,\"logo_path\":null,\"name\":\"The Donners' Company\",\"origin_country\":\"US\"},{\"id\":104228,\"logo_path\":null,\"name\":\"Maximum Effort\",\"origin_country\":\"\"},{\"id\":7505,\"logo_path\":\"/837VMM4wOkODc1idNxGT0KQJlej.png\",\"name\":\"Marvel Entertainment\",\"origin_country\":\"US\"},{\"id\":25,\"logo_path\":\"/qZCc1lty5FzX30aOCVRBLzaVmcp.png\",\"name\":\"20th Century Fox\",\"origin_country\":\"US\"}],\"production_countries\":[{\"iso_3166_1\":\"CA\",\"name\":\"Canada\"},{\"iso_3166_1\":\"US\",\"name\":\"United States of America\"}],\"release_date\":\"2018-05-15\",\"revenue\":502989378,\"runtime\":119,\"spoken_languages\":[{\"iso_639_1\":\"en\",\"name\":\"English\"}],\"status\":\"Released\",\"tagline\":\"Prepare for the Second Coming.\",\"title\":\"Deadpool 2\",\"video\":false,\"vote_average\":7.9,\"vote_count\":1610}"
        val gson = Gson()

        return gson.fromJson(sampleJson, DetailResponse::class.java)
    }
}