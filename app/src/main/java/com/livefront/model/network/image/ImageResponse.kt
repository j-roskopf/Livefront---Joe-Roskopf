package com.livefront.model.network.image

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImageResponse(
        @SerializedName("id")
        @Expose
        var id: Int? = null,
        @SerializedName("backdrops")
        @Expose
        var backdrops: List<Backdrop>? = null,
        @SerializedName("posters")
        @Expose
        var posters: List<Poster>? = null
)