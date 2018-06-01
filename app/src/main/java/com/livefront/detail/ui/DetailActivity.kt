package com.livefront.detail.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.livefront.R
import com.livefront.model.network.Result
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*


class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val result = intent.extras[EXTRA_BUNDLE_RESULT] as? Result
        Picasso.get().load(result?.getFullPosterPathW185()).into(detailViewImage)
    }

    companion object {
        const val EXTRA_BUNDLE_RESULT = "result"
    }
}
