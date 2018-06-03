package com.livefront.model.network

import junit.framework.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class ResultTests {

    @Test
    fun getPosterPathTests() {
        val results = createResults(4)
        results.forEach {
            if(it.getFullPosterPathW185() == null || it.getFullPosterPathW500() == null) {
                fail("Poster path is null when it shouldn't be")
            }
        }

        val result = Result()
        if(result.getFullPosterPathW500() != null || result.getFullPosterPathW185() != null) {
            fail("Poster path wasn't null when it should be")
        }
    }

    /**
     * Creates an array of dummy [Result] for testing purposes
     *
     * @param amount - the amount of items to create
     *
     * @return an ArrayList containing Result
     */
    private fun createResults(amount: Int): ArrayList<Result> {
        val toReturn = arrayListOf<Result>()

        for (i in 1..amount) {
            val result = Result(
                    voteAverage = 1.0,
                    voteCount = 3,
                    video = false,
                    overview = "Overview",
                    posterPath = "valid poster path"
            )

            toReturn.add(result)
        }

        return toReturn
    }
}