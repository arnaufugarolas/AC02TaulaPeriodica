package com.mp08.ac02taulaperidica

import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.mp08.ac02taulaperidica.dataClass.Element
import com.bumptech.glide.Glide.with as GlideWith


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

        GlideWith(this)
            .load(URL)
            .into(imageView)
    }
}
