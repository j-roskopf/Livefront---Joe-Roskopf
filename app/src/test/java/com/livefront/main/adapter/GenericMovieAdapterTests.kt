package com.livefront.main.adapter

import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.google.gson.Gson
import com.livefront.model.network.MovieResponse
import com.livefront.model.network.Result
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GenericMovieAdapterTests {

    private val onItemClickListener = object: ItemInteractionListener {
        override fun onItemClicked(result: Result, imageView: ImageView) {
            //nothing to do here
        }
    }

    private lateinit var adapter: GenericMovieAdapter

    @Before
    fun setup() {
        adapter = GenericMovieAdapter(onItemClickListener)
        adapter.results = getSampleMovieResponseFromJson().results!!
    }

    @Test
    fun isLoadingTest() {
        val holder = mock(RecyclerView.ViewHolder::class.java)
        var currentPosition = 1
        adapter.onBindViewHolder(holder, currentPosition)

        //we shouldn't be able to load more yet because as far as the adapter is aware, there is only 1 max page
        //and the current position is not at the end data set
        assertEquals(adapter.isLoading, false)

        val currentMaxPages = 100

        adapter.totalAmountOfPages = currentMaxPages
        adapter.onBindViewHolder(holder, currentPosition)

        //we shouldn't be able to load more yet because as far as the adapter is aware
        //the current position is not at the end data set,
        assertEquals(adapter.isLoading, false)

        currentPosition = getSampleMovieResponseFromJson().results?.size ?: 0
        adapter.onBindViewHolder(holder, currentPosition)

        //now we should be loading more because our current position is at the end of our data set size
        assertEquals(adapter.isLoading, true)
    }

    @Test
    fun getItemViewTypeTest() {
        /**
         * Verify we correctly identify null items as [VIEW_TYPE_LOADING] instead [VIEW_TYPE_ITEM]
         */
        val results = getSampleMovieResponseFromJson().results!!.toMutableList()
        results.add(0, null)
        adapter.results = results

        assertEquals(VIEW_TYPE_ITEM, adapter.getItemViewType(1))
        assertEquals(VIEW_TYPE_LOADING, adapter.getItemViewType(0))
    }

    @Test
    fun addLoadingItemTest() {
        val results = getSampleMovieResponseFromJson().results!!.toMutableList()
        adapter.results = results

        adapter.addLoadingItem(false)

        //verify only one item was added
        assertEquals(results.size, adapter.results.size - 1)

        //and verify the last item is null
        assert(adapter.results.last() == null)
    }

    @Test
    fun removeLoadingItemTests() {
        val results = getSampleMovieResponseFromJson().results!!.toMutableList()
        adapter.results = results
        adapter.addLoadingItem(false)

        //verify only one item was added
        assertEquals(results.size, adapter.results.size - 1)

        adapter.removeLoadingItem(false)

        //verify the adapter data set is back to normal
        assertEquals(results.size, adapter.results.size)
    }

    @Test
    fun canAddMoreItemsTests() {
        /**
         * Tests to verify our helper variables correctly determine if we can add more pages
         */
        adapter.currentPage = 0
        adapter.totalAmountOfPages = 1

        //because current page is 0
        assertEquals(false, adapter.canAddMoreItems())

        adapter.currentPage = 1
        adapter.totalAmountOfPages = 1

        //because current page and total amount are the same
        assertEquals(false, adapter.canAddMoreItems())

        adapter.currentPage = -1
        adapter.totalAmountOfPages = 1

        //because current page is less than 0
        assertEquals(false, adapter.canAddMoreItems())

        adapter.currentPage = 1
        adapter.totalAmountOfPages = 2

        assertEquals(true, adapter.canAddMoreItems())
    }

    @Test
    fun getAndIncrementPageTest() {
        /**
         * Tests to verify our current page is incremented when it is accessed
         */
        assertEquals(1, adapter.currentPage)

        adapter.getAndIncrementPage()

        assertEquals(2, adapter.currentPage)

        adapter.getAndIncrementPage()

        assertEquals(3, adapter.currentPage)
    }

    /**
     * Returns a sample MovieResponse object from sample JSON
     *
     * @return MovieResponse
     */
    private fun getSampleMovieResponseFromJson(): MovieResponse {
        val json = "{\"results\":[{\"vote_count\":49,\"id\":351286,\"video\":false,\"vote_average\":7.7,\"title\":\"Jurassic World: Fallen Kingdom\",\"popularity\":131.653538,\"poster_path\":\"\\/c9XxwwhPHdaImA2f1WEfEsbhaFB.jpg\",\"original_language\":\"en\",\"original_title\":\"Jurassic World: Fallen Kingdom\",\"genre_ids\":[28,12,878],\"backdrop_path\":\"\\/gBmrsugfWpiXRh13Vo3j0WW55qD.jpg\",\"adult\":false,\"overview\":\"A volcanic eruption threatens the remaining dinosaurs on the island of Isla Nublar, where the creatures have freely roamed for several years after the demise of an animal theme park known as Jurassic World. Claire Dearing, the former park manager, has now founded the Dinosaur Protection Group, an organization dedicated to protecting the dinosaurs. To help with her cause, Claire has recruited Owen Grady, a former dinosaur trainer who worked at the park, to prevent the extinction of the dinosaurs once again.\",\"release_date\":\"2018-06-06\"},{\"vote_count\":1226,\"id\":447332,\"video\":false,\"vote_average\":7.3,\"title\":\"A Quiet Place\",\"popularity\":67.98582,\"poster_path\":\"\\/nAU74GmpUk7t5iklEp3bufwDq4n.jpg\",\"original_language\":\"en\",\"original_title\":\"A Quiet Place\",\"genre_ids\":[18,27,53,878],\"backdrop_path\":\"\\/roYyPiQDQKmIKUEhO912693tSja.jpg\",\"adult\":false,\"overview\":\"A family is forced to live in silence while hiding from creatures that hunt by sound.\",\"release_date\":\"2018-04-05\"},{\"vote_count\":282,\"id\":407451,\"video\":false,\"vote_average\":5.2,\"title\":\"A Wrinkle in Time\",\"popularity\":56.923683,\"poster_path\":\"\\/yAcb58vipewa1BfNit2RjE6boXA.jpg\",\"original_language\":\"en\",\"original_title\":\"A Wrinkle in Time\",\"genre_ids\":[12,878,10751,14],\"backdrop_path\":\"\\/zqUaMojohr96itJYCE3W1NauTn7.jpg\",\"adult\":false,\"overview\":\"After the disappearance of her scientist father, three peculiar beings send Meg, her brother, and her friend to space in order to find him.\",\"release_date\":\"2018-03-08\"},{\"vote_count\":352,\"id\":449176,\"video\":false,\"vote_average\":8.4,\"title\":\"Love, Simon\",\"popularity\":45.54728,\"poster_path\":\"\\/5YUYg5q7QfC4IoNwNUtiwdiYKPr.jpg\",\"original_language\":\"en\",\"original_title\":\"Love, Simon\",\"genre_ids\":[35,18,10749],\"backdrop_path\":\"\\/rqMEeajyzSF4PpJtdCB816Iz8Vr.jpg\",\"adult\":false,\"overview\":\"Everyone deserves a great love story. But for seventeen-year old Simon Spier it's a little more complicated: he's yet to tell his family or friends he's gay and he doesn't know the identity of the anonymous classmate he's fallen for online.\",\"release_date\":\"2018-03-16\"},{\"vote_count\":110,\"id\":371608,\"video\":false,\"vote_average\":5.6,\"title\":\"The Strangers: Prey at Night\",\"popularity\":37.016298,\"poster_path\":\"\\/vdxLpPsZkPZdFrREp7eSeSzcimj.jpg\",\"original_language\":\"en\",\"original_title\":\"The Strangers: Prey at Night\",\"genre_ids\":[27,53],\"backdrop_path\":\"\\/aUtLuEvTI1Z0vItORUYho4UiU6z.jpg\",\"adult\":false,\"overview\":\"A family's road trip takes a dangerous turn when they arrive at a secluded mobile home park to stay with some relatives and find it mysteriously deserted. Under the cover of darkness, three masked psychopaths pay them a visit to test the family's every limit as they struggle to survive.\",\"release_date\":\"2018-03-07\"},{\"vote_count\":19,\"id\":411135,\"video\":false,\"vote_average\":5.6,\"title\":\"Future World\",\"popularity\":33.598085,\"poster_path\":\"\\/kMA0IalnEEa0PaHRUzzjpTu5xXQ.jpg\",\"original_language\":\"en\",\"original_title\":\"Future World\",\"genre_ids\":[878],\"backdrop_path\":\"\\/g29nhwmOiYElKW27hzohK6nSwpC.jpg\",\"adult\":false,\"overview\":\"A young boy searches a future world wasteland for a rumored cure for his dying mother.\",\"release_date\":\"2018-05-25\"},{\"vote_count\":107,\"id\":430040,\"video\":false,\"vote_average\":5.3,\"title\":\"The Hurricane Heist\",\"popularity\":29.788886,\"poster_path\":\"\\/rAmcj5IZcx59dhev3UnVDEGlImK.jpg\",\"original_language\":\"en\",\"original_title\":\"The Hurricane Heist\",\"genre_ids\":[28,80,53],\"backdrop_path\":\"\\/3ZQRLuXNTbewOHDfxF51fy74gh4.jpg\",\"adult\":false,\"overview\":\"Thieves attempt a massive heist against the U.S. Treasury as a Category 5 hurricane approaches one of its Mint facilities.\",\"release_date\":\"2018-02-01\"},{\"vote_count\":61,\"id\":465136,\"video\":false,\"vote_average\":6.9,\"title\":\"Every Day\",\"popularity\":29.788633,\"poster_path\":\"\\/4UnME3icxSspwL0UoGZNSyyp7Xs.jpg\",\"original_language\":\"en\",\"original_title\":\"Every Day\",\"genre_ids\":[18,14,10749],\"backdrop_path\":\"\\/hcoeD3oLP7t2dwMK7mnRAUkUxWS.jpg\",\"adult\":false,\"overview\":\"16-year old Rhiannon falls in love with a mysterious spirit named “A” that inhabits a different body every day. Feeling an unmatched connection, Rhiannon and “A” work each day to find each other, not knowing what the next day will bring.\",\"release_date\":\"2018-02-22\"},{\"vote_count\":168,\"id\":460019,\"video\":false,\"vote_average\":5.8,\"title\":\"Truth or Dare\",\"popularity\":27.950817,\"poster_path\":\"\\/zbvziwnZa91AJD78Si0hUb5JP5X.jpg\",\"original_language\":\"en\",\"original_title\":\"Truth or Dare\",\"genre_ids\":[53,27],\"backdrop_path\":\"\\/eQ5xu2pQ5Kergubto5PbbUzey28.jpg\",\"adult\":false,\"overview\":\"A harmless game of “Truth or Dare” among friends turns deadly when someone—or something—begins to punish those who tell a lie—or refuse the dare.\",\"release_date\":\"2018-04-12\"},{\"vote_count\":3,\"id\":402900,\"video\":false,\"vote_average\":10,\"title\":\"Ocean's 8\",\"popularity\":22.601284,\"poster_path\":\"\\/7caaqQjTkovwoZsEOPzgHvkzOWB.jpg\",\"original_language\":\"en\",\"original_title\":\"Ocean's 8\",\"genre_ids\":[80,35,28,53],\"backdrop_path\":\"\\/zBIfBeStaDdDdhBrSx8InhgFUVa.jpg\",\"adult\":false,\"overview\":\"Debbie Ocean, a criminal mastermind, gathers a crew of seven other female thieves to pull off the heist of the century at New York's annual Met Gala.\",\"release_date\":\"2018-06-07\"},{\"vote_count\":0,\"id\":260513,\"video\":false,\"vote_average\":0,\"title\":\"Incredibles 2\",\"popularity\":19.132312,\"poster_path\":\"\\/hL9Uz2vq93vi20oxZEBBaSs4w8U.jpg\",\"original_language\":\"en\",\"original_title\":\"Incredibles 2\",\"genre_ids\":[28,12,16,10751],\"backdrop_path\":\"\\/kqoBtMmiycbbhGLXGkKhL8SdaWB.jpg\",\"adult\":false,\"overview\":\"Elastigirl springs into action to save the day, while Mr. Incredible faces his greatest challenge yet – taking care of the problems of his three children.\",\"release_date\":\"2018-06-14\"},{\"vote_count\":11,\"id\":493922,\"video\":false,\"vote_average\":6.5,\"title\":\"Hereditary\",\"popularity\":18.801939,\"poster_path\":\"\\/xqX1KCs1LG26SxasiBAooF311OR.jpg\",\"original_language\":\"en\",\"original_title\":\"Hereditary\",\"genre_ids\":[27,18,9648,53],\"backdrop_path\":\"\\/3z0oRN1qKDIB5puUtSaFs3pJO4V.jpg\",\"adult\":false,\"overview\":\"After their reclusive grandmother passes away, the Graham family tries to escape the dark fate they've inherited.\",\"release_date\":\"2018-06-04\"},{\"vote_count\":31,\"id\":385332,\"video\":false,\"vote_average\":5.6,\"title\":\"Terminal\",\"popularity\":17.056431,\"poster_path\":\"\\/xi7pA3lL6Wb78p6Y6PgWoHDMjrM.jpg\",\"original_language\":\"en\",\"original_title\":\"Terminal\",\"genre_ids\":[53,18,80],\"backdrop_path\":\"\\/bwvxvrHH3nTbcwpEcJhA72kkI94.jpg\",\"adult\":false,\"overview\":\"In the dark heart of a sprawling, anonymous city, two assassins carrying out a sinister mission, a teacher battles a fatal illness, and an enigmatic janitor and a curious waitress leading dangerous double lives. Murderous consequences unravel in the dead of night as their lives all intertwine at the hands of a mysterious criminal mastermind hell-bent on revenge.\",\"release_date\":\"2018-05-11\"},{\"vote_count\":42,\"id\":400579,\"video\":false,\"vote_average\":7,\"title\":\"Tully\",\"popularity\":16.887808,\"poster_path\":\"\\/zWoIgYCAJlXMWp7VxNLK0AVYvSR.jpg\",\"original_language\":\"en\",\"original_title\":\"Tully\",\"genre_ids\":[35,18],\"backdrop_path\":\"\\/808PRFyIR04myiBwBuDlZ0DT2DS.jpg\",\"adult\":false,\"overview\":\"Marlo, a mother of three including a newborn, is gifted a night nanny by her brother. Hesitant to the extravagance at first, Marlo comes to form a bond with the thoughtful, surprising, and sometimes challenging nanny named Tully.\",\"release_date\":\"2018-05-04\"},{\"vote_count\":64,\"id\":467660,\"video\":false,\"vote_average\":6.3,\"title\":\"Unsane\",\"popularity\":14.865634,\"poster_path\":\"\\/1M1U5K4Vf1Cbtrjl9EkGNWpqahf.jpg\",\"original_language\":\"en\",\"original_title\":\"Unsane\",\"genre_ids\":[27,53],\"backdrop_path\":\"\\/5u0ImRdr8Z6uyDfmSWcThXEb8cl.jpg\",\"adult\":false,\"overview\":\"A woman is involuntarily committed to a mental institution amid uncertainty as to whether her greatest fear is real or delusional.\",\"release_date\":\"2018-03-23\"},{\"vote_count\":48,\"id\":460668,\"video\":false,\"vote_average\":6.6,\"title\":\"I Feel Pretty\",\"popularity\":14.416823,\"poster_path\":\"\\/bZe6x2fKtwVDsAvZQ9fnIJznBrc.jpg\",\"original_language\":\"en\",\"original_title\":\"I Feel Pretty\",\"genre_ids\":[35],\"backdrop_path\":\"\\/7XM7kxa3U2g8TO3OGSiETCroNRD.jpg\",\"adult\":false,\"overview\":\"A head injury causes a woman to develop an extraordinary amount of confidence and believe she's drop dead gorgeous.\",\"release_date\":\"2018-04-20\"},{\"vote_count\":190,\"id\":419478,\"video\":false,\"vote_average\":7.8,\"title\":\"Midnight Sun\",\"popularity\":14.152805,\"poster_path\":\"\\/zjbG42eFLbOF6e4rCZTKiMkEidD.jpg\",\"original_language\":\"en\",\"original_title\":\"Midnight Sun\",\"genre_ids\":[10749,18],\"backdrop_path\":\"\\/xiDuiG4go4TPPI2JDMzqyImttyL.jpg\",\"adult\":false,\"overview\":\"Katie, a 17-year-old, has been sheltered since childhood and confined to her house during the day by a rare disease that makes even the smallest amount of sunlight deadly. Fate intervenes when she meets Charlie and they embark on a summer romance.\",\"release_date\":\"2018-03-22\"},{\"vote_count\":1,\"id\":429300,\"video\":false,\"vote_average\":6.5,\"title\":\"Adrift\",\"popularity\":13.920139,\"poster_path\":\"\\/5gLDeADaETvwQlQow5szlyuhLbj.jpg\",\"original_language\":\"en\",\"original_title\":\"Adrift\",\"genre_ids\":[53,10749,12],\"backdrop_path\":\"\\/64jAqTJvrzEwncD3ARZdqYLcqbc.jpg\",\"adult\":false,\"overview\":\"A woman and her fiancé find themselves caught in a hurricane after departing Tahiti by boat. She wakes up after being knocked out, only to discover him missing, and the elements threatening.\",\"release_date\":\"2018-05-31\"},{\"vote_count\":439,\"id\":399174,\"video\":false,\"vote_average\":7.9,\"title\":\"Isle of Dogs\",\"popularity\":13.845064,\"poster_path\":\"\\/c0nUX6Q1ZB0P2t1Jo6EeFSVnOGQ.jpg\",\"original_language\":\"en\",\"original_title\":\"Isle of Dogs\",\"genre_ids\":[12,35,16],\"backdrop_path\":\"\\/5YtXsLG9ncjjFyGZjoeV31CGf01.jpg\",\"adult\":false,\"overview\":\"In the future, an outbreak of canine flu leads the mayor of a Japanese city to banish all dogs to an island that's a garbage dump. The outcasts must soon embark on an epic journey when a 12-year-old boy arrives on the island to find his beloved pet.\",\"release_date\":\"2018-03-23\"},{\"vote_count\":169,\"id\":423646,\"video\":false,\"vote_average\":7.1,\"title\":\"In the Fade\",\"popularity\":13.370552,\"poster_path\":\"\\/qaLbKxjHaOkY7lT6jmsS1lsRocy.jpg\",\"original_language\":\"de\",\"original_title\":\"Aus dem Nichts\",\"genre_ids\":[18,80],\"backdrop_path\":\"\\/en4eaSgqL18UYIRSp86fSi4iZvL.jpg\",\"adult\":false,\"overview\":\"Katja's life falls apart in the blink of an eye when two neo-Nazis kill her husband and 6-year-old son in a bomb attack. Her quest for justice soon pushes her to the edge as the two suspects stand trial for murder.\",\"release_date\":\"2017-11-23\"}],\"page\":1,\"total_results\":227,\"dates\":{\"maximum\":\"2018-06-28\",\"minimum\":\"2018-06-08\"},\"total_pages\":12}"
        return Gson().fromJson(json, MovieResponse::class.java)
    }
}