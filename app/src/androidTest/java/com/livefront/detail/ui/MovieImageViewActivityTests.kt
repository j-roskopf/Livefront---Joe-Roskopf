package com.livefront.detail.ui

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.ViewAssertion
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.view.View
import com.livefront.EspressoTestingIdlingResource
import com.livefront.R
import junit.framework.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieImageViewActivityTests {

    @get:Rule
    var activityRule: IntentsTestRule<MovieImageViewActivity> = object : IntentsTestRule<MovieImageViewActivity>(MovieImageViewActivity::class.java) {
        override fun getActivityIntent(): Intent {
            val targetContext = InstrumentationRegistry.getInstrumentation()
                    .targetContext
            val result = Intent(targetContext, MovieImageViewActivity::class.java)
            result.putExtra("movie_id", "383498") //deadpool 2
            return result
        }
    }

    @Test
    fun verifyResultsReturned() {
        /**
         * Verifies results return successfully from the API
         */
        val espressoTestingIdlingResource = EspressoTestingIdlingResource()
        IdlingRegistry.getInstance().register(espressoTestingIdlingResource)

        //make sure our recycler view has results
        Espresso.onView(ViewMatchers.withId(R.id.movieDetailImageRecyclerView)).check(com.livefront.main.ui.RecyclerViewItemCountAssertion(0))

        IdlingRegistry.getInstance().unregister(espressoTestingIdlingResource)
    }

    class RecyclerViewItemCountAssertion(private val expectedCount: Int) : ViewAssertion {

        override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
            if (noViewFoundException != null) {
                throw noViewFoundException
            }

            val recyclerView = view as RecyclerView
            val adapter = recyclerView.adapter

            Assert.assertTrue("Adapter should not be null", adapter != null)
            Assert.assertTrue("Adapter item count should be greater than 0", adapter.itemCount > expectedCount)

        }
    }
}