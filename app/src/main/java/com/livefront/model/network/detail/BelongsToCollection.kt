package com.livefront.model.network.detail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class BelongsToCollection(
        @SerializedName("id")
        @Expose
        var id: Int? = null,
        @SerializedName("name")
        @Expose
        var name: String? = null,
        @SerializedName("poster_path")
        @Expose
        var posterPath: String? = null,
        @SerializedName("backdrop_path")
        @Expose
        var backdropPath: String? = null
)