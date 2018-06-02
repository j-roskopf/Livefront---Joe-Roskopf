package com.livefront.main.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.livefront.R
import com.livefront.model.network.Result
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.generic_movie_result_row_item.view.*
import kotlinx.android.synthetic.main.loading_item.view.*

const val VIEW_TYPE_ITEM = 0
const val VIEW_TYPE_LOADING = 1

class GenericMovieAdapter(private val onItemInteractionListener: ItemInteractionListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //current page loaded for this adapter
    internal var currentPage = 1

    //max amount of pages available
    internal var totalAmountOfPages = 1

    //data source for our adapter
    internal var results: List<Result?> = listOf()

    //interface implementation to trigger when we want to load more items
    private var onLoadMoreListener: OnLoadMoreListener? = null

    //status variable for if we are currently loading
    internal var isLoading: Boolean = false

    /**
     * In order to trigger a 'loading' item, we just add a null item to our data set.
     *
     * So if we detect a null item, return a [VIEW_TYPE_LOADING], else a [VIEW_TYPE_ITEM]
     *
     * @param position - current position in adapter
     *
     * @return view type
     */
    override fun getItemViewType(position: Int): Int {
        return if (results[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //We detect we are at the 'end' of our list and should load more if 3 conditions are met.
        //1 - the position we are loading is the second to last item in our list
        //2. - we are not currently loading items
        //3 - we have more items to load
        if (position >= itemCount - 1 && !isLoading && canAddMoreItems()) {
            isLoading = true
            onLoadMoreListener?.loadMore()
        }
        when (holder) {
            is MovieViewHolder -> holder.setProperties(results[position], onItemInteractionListener)
            is LoadingViewHolder -> holder.setIndeterminate()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.generic_movie_result_row_item, parent, false)
            MovieViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.loading_item, parent, false)
            LoadingViewHolder(view)
        }
    }

    /**
     * Returns size of our data set
     *
     * @return size of data set
     */
    override fun getItemCount(): Int {
        return results.size
    }

    /**
     * Setter for on [OnLoadMoreListener]
     */
    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener
    }

    /**
     * Adds a null item to our data set.
     *
     * This method is responsible for notifying the adapter that the data set has changed
     *
     * @param notifyAdapter - whether or not we should alert the adapter of the data set changed.
     *                        this is useful for testing purposes
     */
    fun addLoadingItem(notifyAdapter: Boolean = true) {
        val loadingItem: Result? = null
        results += loadingItem
        if(notifyAdapter) {
            notifyItemInserted(itemCount)
        }
    }

    /**
     * Removes the loading item.
     * This is done by continually dropping the last item until we find the fir non-null one
     *
     *  @param notifyAdapter - whether or not we should alert the adapter of the data set changed.
     *                        this is useful for testing purposes
     */
    fun removeLoadingItem(notifyAdapter: Boolean = true) {
        results = results.dropLastWhile {
            it == null
        }

        if(notifyAdapter) {
            notifyItemRemoved(itemCount)
        }
    }

    /**
     * Helper method to determine if we can load more pages i.e - current page is in the range of 1 to total amount of pages - 1
     *
     * @return whether or not we can add more items
     */
    fun canAddMoreItems(): Boolean {
        return currentPage in 1..(totalAmountOfPages - 1)
    }

    /**
     * Returns the current page while also incrementing it for next use
     *
     * @return current page
     */
    fun getAndIncrementPage(): Int {
        return currentPage++
    }

}

class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val movieItemPosterImage = view.movieItemPosterImage as ImageView

    internal fun setProperties(result: Result?, onItemInteractionListener: ItemInteractionListener?) {
        //if we have a valid poster path, use that to set the image
        if (result?.getFullPosterPathW185() != null) {
            Picasso.get()
                    .load(result.getFullPosterPathW500())
                    .error(R.drawable.no_image_available)
                    .placeholder(R.drawable.progress_animation)
                    .into(movieItemPosterImage)
        } else {
            //otherwise fall back to our placeholder
            Picasso.get()
                    .load(R.drawable.no_image_available)
                    .into(movieItemPosterImage)
        }

        itemView.setOnClickListener {
            result?.let {
                onItemInteractionListener?.onItemClicked(it, movieItemPosterImage)
            }
        }
    }
}

class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val loadingProgressBar = view.loadingItemProgressBar as ProgressBar

    internal fun setIndeterminate() {
        loadingProgressBar.isIndeterminate = true
    }
}

/**
 * Simple interface for what happens when an item is clicked in our generic adapter
 */
interface ItemInteractionListener {

    /**
     * When an item is clicked
     *
     * @param result - the model describing the item in our adapter
     * @param imageView - the shared ImageView to transition between the main and detail view
     */
    fun onItemClicked(result: Result, imageView: ImageView)
}