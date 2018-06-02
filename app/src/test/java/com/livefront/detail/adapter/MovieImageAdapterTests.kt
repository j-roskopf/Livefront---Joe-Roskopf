package com.livefront.detail.adapter

import com.livefront.model.network.image.MovieImage
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class MovieImageAdapterTests {

    private val amountOfImages = 100

    private lateinit var movieImageAdapter: MovieImageAdapter

    @Before
    fun setup() {
        movieImageAdapter = MovieImageAdapter(createMovieImages(amountOfImages))
    }

    @Test
    fun getItemCountTest() {
        /**
         * Verifies the adapters item count matches the amount of items it started with
         */
        assertEquals(movieImageAdapter.itemCount, amountOfImages)
    }

    @Test
    fun imageContainsValidPathTest() {
        /**
         * Verify that if an image is valid, we will use it in our adapter. Otherwise fallback to an error image
         */
        val random = Random()

        val movieImageWithValidPath = MovieImage(
                width = random.nextInt(100),
                height = random.nextInt(100),
                aspectRatio = random.nextDouble(),
                filePath = "dummy file path that is very valid"
        )

        val movieImageWithNoValidPath = MovieImage(
                width = random.nextInt(100),
                height = random.nextInt(100),
                aspectRatio = random.nextDouble(),
                filePath = null
        )

        assertEquals(true, movieImageAdapter.imageContainsValidPath(movieImageWithValidPath))

        assertEquals(false, movieImageAdapter.imageContainsValidPath(movieImageWithNoValidPath))
    }

    /**
     * Creates an array of dummy [MovieImage] for testing purposes
     *
     * @param amount - the amount of items to create
     *
     * @return an ArrayList containing MovieImages
     */
    private fun createMovieImages(amount: Int): ArrayList<MovieImage> {
        val toReturn = arrayListOf<MovieImage>()

        val random = Random()

        for (i in 1..amount) {
            val movieImage = MovieImage(
                    width = random.nextInt(100),
                    height = random.nextInt(100),
                    aspectRatio = random.nextDouble(),
                    filePath = "dummy file path"
            )

            toReturn.add(movieImage)
        }

        return toReturn
    }
}