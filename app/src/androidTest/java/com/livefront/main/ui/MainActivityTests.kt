package com.livefront.main.ui

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingRegistry
import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.ViewAssertion
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.view.View
import com.livefront.EspressoTestingIdlingResource
import com.livefront.R
import com.livefront.detail.ui.DetailActivity
import junit.framework.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    var activityRule: IntentsTestRule<MainActivity> = IntentsTestRule(
            MainActivity::class.java)

    @Test
    fun verifyResultsReturned() {
        /**
         * Verifies results return successfully from the API
         */
        val espressoTestingIdlingResource = EspressoTestingIdlingResource()
        IdlingRegistry.getInstance().register(espressoTestingIdlingResource)

        //make sure our 3 recycler views have results
        onView(withId(R.id.mainViewMostPopularAllTimeRecyclerView)).check(RecyclerViewItemCountAssertion(0))
        onView(withId(R.id.mainViewUpcomingRecyclerView)).check(RecyclerViewItemCountAssertion(0))
        onView(withId(R.id.mainViewTheatreMoviesRecyclerView)).check(RecyclerViewItemCountAssertion(0))

        IdlingRegistry.getInstance().unregister(espressoTestingIdlingResource)
    }

    @Test
    fun verifyClickingOnItemStartsDetailIntent() {
        /**
         * Verifies the [DetailActivity] is started correctly after clicking on an item
         */
        val espressoTestingIdlingResource = EspressoTestingIdlingResource()
        IdlingRegistry.getInstance().register(espressoTestingIdlingResource)

        onView(withId(R.id.mainViewUpcomingRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(3, click()))

        //Thread.sleep(2000)

        intended(hasComponent(DetailActivity::class.java.name))

        IdlingRegistry.getInstance().unregister(espressoTestingIdlingResource)
    }

}

class RecyclerViewItemCountAssertion(private val expectedCount: Int) : ViewAssertion {

    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter

        assertTrue("Adapter should not be null", adapter != null)
        assertTrue("Adapter item count should be greater than 0", adapter.itemCount > expectedCount)

    }
}