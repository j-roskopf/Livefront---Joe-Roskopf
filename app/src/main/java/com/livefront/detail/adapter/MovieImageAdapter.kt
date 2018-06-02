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

        //set the image
        image.filePath?.let {
            Picasso.get().load(image.getFullImageUrl(it)).error(R.drawable.no_image_available)
                    .placeholder(R.drawable.progress_animation).into(holder.movieDetailItemImage)
        }
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