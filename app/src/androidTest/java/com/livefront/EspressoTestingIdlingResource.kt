package com.livefront

import android.app.Activity
import android.support.test.espresso.IdlingResource
import android.support.test.espresso.core.internal.deps.guava.collect.Iterables
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import android.support.test.runner.lifecycle.Stage
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.livefront.detail.ui.DetailActivity
import com.livefront.detail.ui.MovieImageViewActivity
import com.livefront.main.ui.MainActivity


class EspressoTestingIdlingResource : IdlingResource {
    private var resourceCallback: IdlingResource.ResourceCallback? = null
    private var isIdle: Boolean = false

    private val currentActivity: Activity?
        get() {
            val activity = arrayOfNulls<Activity>(1)
            val activities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
            activity[0] = Iterables.getOnlyElement(activities)
            return activity[0]
        }

    override fun getName(): String {
        return EspressoTestingIdlingResource::class.java.name
    }

    override fun isIdleNow(): Boolean {
        if (isIdle) return true

        val activity = currentActivity ?: return false

        idlingCheck(activity)

        if (isIdle) {
            resourceCallback!!.onTransitionToIdle()
        }
        return isIdle
    }

    override fun registerIdleTransitionCallback(
            resourceCallback: IdlingResource.ResourceCallback) {
        this.resourceCallback = resourceCallback
    }

    private fun idlingCheck(activity: Activity) {
        when (activity) {
            is MainActivity -> verifyMainActivityIdle(activity)
            is DetailActivity -> verifyDetailActivityIdle(activity)
            is MovieImageViewActivity -> verifyMovieImageViewActivity(activity)
        }
    }

    /**
     * Verifies that the DetailActivity is idle by checking if the Revenue Body is displayed.
     *
     * This is only displayed once the API has returned and given valid results.
     */
    private fun verifyDetailActivityIdle(activity: DetailActivity) {
        isIdle = (activity.findViewById<TextView>(R.id.detailRevenueBody).visibility == View.VISIBLE)
    }

    /**
     * Verifies that MovieImageViewActivity is idle by checking if the RecyclerView has a valid item count
     */
    private fun verifyMovieImageViewActivity(activity: MovieImageViewActivity) {
        isIdle = (activity.findViewById<RecyclerView>(R.id.movieDetailImageRecyclerView).adapter?.itemCount ?: 0 > 0)
    }

    /**
     * Verifies that MainActivity is idle by checking if all 3 of our RecyclerViews have a valid item count
     */
    private fun verifyMainActivityIdle(activity: MainActivity) {
        isIdle = (activity.findViewById<RecyclerView>(R.id.mainViewMostPopularAllTimeRecyclerView).adapter?.itemCount ?: 0 > 0 &&
                activity.findViewById<RecyclerView>(R.id.mainViewTheatreMoviesRecyclerView).adapter?.itemCount ?: 0 > 0 &&
                activity.findViewById<RecyclerView>(R.id.mainViewUpcomingRecyclerView).adapter?.itemCount ?: 0 > 0)
    }
}