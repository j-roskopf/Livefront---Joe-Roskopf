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

private const val VIEW_TYPE_ITEM = 0
private const val VIEW_TYPE_LOADING = 1

class GenericMovieAdapter(private val onItemInteractionListener: ItemInteractionListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var currentPage = 1
    var totalAmountOfPages = 1
    private var results: List<Result?> = listOf()
    private var onLoadMoreListener: OnLoadMoreListener? = null
    internal var isLoading: Boolean = false

    override fun getItemViewType(position: Int): Int {
        return if (results[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
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

    override fun getItemCount(): Int {
        return results.size
    }

    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener
    }

    fun addLoadingItem() {
        val loadingItem: Result? = null
        results += loadingItem
        notifyItemInserted(itemCount)
    }

    fun removeLoadingItem() {
        results.dropLastWhile {
            it == null
        }
        notifyItemRemoved(itemCount)
    }

    fun canAddMoreItems(): Boolean {
        return currentPage < totalAmountOfPages
    }

    fun getAndIncrementPage(): Int {
        return currentPage++
    }

    fun setResults(results: List<Result?>) {
        this.results = results
    }

}

class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val movieItemPosterImage = view.movieItemPosterImage as ImageView

    internal fun setProperties(result: Result?, onItemInteractionListener: ItemInteractionListener?) {
        if (result?.getFullPosterPathW185() != null) {
            Picasso.get()
                    .load(result.getFullPosterPathW500())
                    .error(R.drawable.no_image_available)
                    .placeholder(R.drawable.progress_animation)
                    .into(movieItemPosterImage)
        } else {
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

interface ItemInteractionListener {
    fun onItemClicked(result: Result, imageView: ImageView)
}