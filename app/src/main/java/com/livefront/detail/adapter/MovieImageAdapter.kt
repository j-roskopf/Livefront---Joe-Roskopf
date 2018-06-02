package com.livefront.detail.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.livefront.R
import com.livefront.model.network.image.MovieImage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.detail_movie_image_item.view.*

/**
 * Adapter class for displaying all of the images (posters and backdrops) associated with a movie
 */
class MovieImageAdapter(private val images: List<MovieImage>) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.detail_movie_image_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = images[position]

        if(imageContainsValidPath(image)) {
            val path = image.filePath ?: ""
            Picasso.get().load(image.getFullImageUrl(path)).error(R.drawable.no_image_available)
                    .placeholder(R.drawable.progress_animation).into(holder.movieDetailItemImage)
        } else {
            Picasso.get().load(R.drawable.no_image_available).error(R.drawable.no_image_available)
                    .placeholder(R.drawable.progress_animation).into(holder.movieDetailItemImage)
        }
    }

    /**
     * Given a [MovieImage], returns whether or not the MovieImage contains a valid file path
     *
     * @param image - the MovieImage containing the path
     *
     * @return boolean - t / f if we should use the image path
     */
    fun imageContainsValidPath(image: MovieImage): Boolean {
        return image.filePath != null
    }

    /**
     * Returns the item count
     *
     * @return size of data set
     */
    override fun getItemCount(): Int {
        return images.size
    }

}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val movieDetailItemImage = view.movieDetailItemImage as ImageView
}