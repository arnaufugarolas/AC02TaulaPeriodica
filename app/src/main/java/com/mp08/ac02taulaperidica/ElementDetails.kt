package com.mp08.ac02taulaperidica

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.mp08.ac02taulaperidica.dataClass.Element


class ElementDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_element_details)

        val element = getElementDetails(intent.getStringExtra("element"))

        val symbol = findViewById<TextView>(R.id.TVEDSymbol)
        val favorite = findViewById<ImageView>(R.id.IVEDFavorite)

        if (element.favorite) {
            favorite.setImageResource(R.drawable.ic_baseline_bookmark_24)
        } else {
            favorite.setImageResource(R.drawable.ic_baseline_bookmark_border_24)
        }

        val image = findViewById<ImageView>(R.id.IVEDImage)
        setImageToImageView(image, element.image?.url.toString())

        symbol.text = element.symbol
        supportActionBar?.title = element.name
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.element_details_menu, menu)
        return true
    }

    private fun getElementDetails(element: String?): Element {
        val gson = Gson()
        return gson.fromJson(element, Element::class.java)
    }

    private fun setImageToImageView(imageView: ImageView, URL: String) {
        val progressBar = findViewById<ProgressBar>(R.id.PBElementDetails)
        progressBar.visibility = ProgressBar.VISIBLE


        Glide.with(this)
            .load(URL)
            .timeout(1000)
            .listener((object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = ProgressBar.GONE
                    Snackbar.make(
                        findViewById(R.id.IVEDImage),
                        "Error loading the image",
                        Snackbar.LENGTH_LONG
                    ).setBackgroundTint(getColor(R.color.pantone_1625_c))
                        .show()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = ProgressBar.GONE
                    return false
                }
            }))
            .into(imageView)
    }

}