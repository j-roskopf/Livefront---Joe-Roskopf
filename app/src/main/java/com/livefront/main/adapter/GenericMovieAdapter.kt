package com.livefront.main.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.livefront.R
import com.livefront.model.network.Result
import kotlinx.android.synthetic.main.generic_movie_result_row_item.view.*
import kotlinx.android.synthetic.main.loading_item.view.*


private const val VIEW_TYPE_ITEM = 0
private const val VIEW_TYPE_LOADING = 1

class GenericMovieAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var currentPage = 1
    var totalAmountOfPages = 1
    private var results: List<Result?> = listOf()
    private var onLoadMoreListener: OnLoadMoreListener? = null
    internal var isLoading: Boolean = false


    override fun getItemViewType(position: Int): Int {
        return if (results[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("D","debugDebug - $position ${itemCount - 1} $isLoading ${canAddMoreItems()}")
        if (position >= itemCount - 1 && !isLoading && canAddMoreItems()) {
            isLoading = true
            onLoadMoreListener?.loadMore()
        }
        when (holder) {
            is MovieViewHolder -> holder.setProperties(results[position])
            is LoadingViewHolder -> holder.setIndeterminate()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(context).inflate(R.layout.generic_movie_result_row_item, parent, false)
            MovieViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.loading_item, parent, false)
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


    fun getAndIcrementPage(): Int {
        return currentPage++
    }

    fun setResults(results: List<Result?>) {
        this.results = results
    }

    fun canAddMoreItems(): Boolean {
       return currentPage < totalAmountOfPages
    }
}

class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val movieItemPosterImage = view.movieItemPosterImage as SimpleDraweeView

    internal fun setProperties(result: Result?) {
        if (result?.getFullPosterPathW185() != null) {
            movieItemPosterImage.setImageURI(result.getFullPosterPathW185())
        } else {
            val imageRequest = ImageRequestBuilder.newBuilderWithResourceId(R.drawable.no_image_available).build()
            movieItemPosterImage.setImageRequest(imageRequest)
        }
    }
}

class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val loadingProgressBar = view.loadingItemProgressBar as ProgressBar

    internal fun setIndeterminate() {
        loadingProgressBar.isIndeterminate = true
    }
}