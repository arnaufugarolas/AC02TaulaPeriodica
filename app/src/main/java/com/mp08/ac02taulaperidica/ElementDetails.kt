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
    private lateinit var element: Element

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_element_details)

        loadElementDetails()
    }


    private fun loadElementDetails() {
        element = getElementDetails(intent.getStringExtra("element"))
        val favorite = findViewById<ImageView>(R.id.IVEDFavorite)

        if (element.favorite) {
            favorite.setImageResource(R.drawable.ic_baseline_bookmark_24)
        } else favorite.setImageResource(R.drawable.ic_baseline_bookmark_border_24)

        favorite.setOnClickListener() {
            element.favorite = !element.favorite
            if (element.favorite) {
                favorite.setImageResource(R.drawable.ic_baseline_bookmark_24)
                Snackbar.make(it, "Added to favorites", Snackbar.LENGTH_SHORT).show()
            } else {
                favorite.setImageResource(R.drawable.ic_baseline_bookmark_border_24)
                Snackbar.make(it, "Removed from favorites", Snackbar.LENGTH_SHORT).show()
            }
        }

        val image = findViewById<ImageView>(R.id.IVEDImage)
        setImageToImageView(image, element.image?.url.toString())

        findViewById<TextView>(R.id.TVEDSymbol).text = element.symbol
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

    private fun loadFailed(target: ImageView, URL: String, errorImageView: ImageView) {
        errorImageView.visibility = ImageView.VISIBLE

        Snackbar.make(
            errorImageView,
            "Error loading the image",
            Snackbar.LENGTH_SHORT
        ).setBackgroundTint(getColor(R.color.pantone_1625_c))
            .show()

        errorImageView.setOnClickListener {
            errorImageView.visibility = ImageView.GONE
            setImageToImageView(target, URL)
        }
    }


    private fun setImageToImageView(imageView: ImageView, URL: String) {
        val progressBar = findViewById<ProgressBar>(R.id.PBElementDetails)
        val errorImage = findViewById<ImageView>(R.id.IVEDLoadError)

        progressBar.visibility = ProgressBar.VISIBLE
        errorImage.visibility = ImageView.GONE

        Snackbar.make(
            progressBar,
            "Loading image...",
            Snackbar.LENGTH_SHORT
        ).show()

        Glide.with(this)
            .load(URL)
            .listener((object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    progressBar.visibility = ProgressBar.GONE
                    loadFailed(imageView, URL, errorImage)
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