package com.mp08.ac02taulaperidica

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.mp08.ac02taulaperidica.dataClass.Element
import com.mp08.ac02taulaperidica.dataClass.PeriodicTable

class ElementDetails : AppCompatActivity() {
    private lateinit var element: Element
    private lateinit var loadingImageProgressBar: ProgressBar
    private lateinit var errorImageView: ImageView

    private var gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_element_details)
        init()
    }

    private fun init() {
        element = gson.fromJson(intent.getStringExtra("element"), Element::class.java)
        loadElementDetails()
    }

    private fun loadElementDetails() {
        setElementData()
        setupElementImage()
        setupElementFavorite()
    }

    private fun setElementData() {
        supportActionBar?.title = element.name

        findViewById<TextView>(R.id.TVElementSymbol).text = element.symbol
        findViewById<TextView>(R.id.TVElementNameData).text = element.name
        findViewById<TextView>(R.id.TVElementAppearanceData).text = element.appearance
        findViewById<TextView>(R.id.TVElementAtomicMassData).text =
            element.atomicMass.toString()
        findViewById<TextView>(R.id.TVElementBoilData).text = element.boil.toString()
        findViewById<TextView>(R.id.TVElementCategoryData).text = element.category
        findViewById<TextView>(R.id.TVElementDensityData).text = element.density.toString()
        findViewById<TextView>(R.id.TVElementDiscoveredByData).text = element.discoveredBy
        findViewById<TextView>(R.id.TVElementMeltData).text = element.melt.toString()
        findViewById<TextView>(R.id.TVElementMolarHeatData).text = element.molarHeat.toString()
        findViewById<TextView>(R.id.TVElementNamedByData).text = element.namedBy
        findViewById<TextView>(R.id.TVElementNumberData).text = element.number.toString()
        findViewById<TextView>(R.id.TVElementPeriodData).text = element.period.toString()
        findViewById<TextView>(R.id.TVElementXPositionData).text = element.xpos.toString()
        findViewById<TextView>(R.id.TVElementYPositionData).text = element.ypos.toString()
        findViewById<TextView>(R.id.TVElementPhaseData).text = element.phase
        findViewById<TextView>(R.id.TVElementShellsData).text = element.shells.toString()
        findViewById<TextView>(R.id.TVElementElectronConfigurationData).text =
            element.electronConfiguration
        findViewById<TextView>(R.id.TVElementElectronConfigurationSemanticData).text =
            element.electronConfigurationSemantic
        findViewById<TextView>(R.id.TVElementElectronAffinityData).text =
            element.electronAffinity.toString()
        findViewById<TextView>(R.id.TVElementElectronegativityPaulingData).text =
            element.electronegativityPauling.toString()
        findViewById<TextView>(R.id.TVElementIonizationEnergiesData).text =
            element.ionizationEnergies.toString()
        findViewById<TextView>(R.id.TVElementSummaryData).text = element.summary
    }

    private fun setupElementImage() {
        val image = findViewById<ImageView>(R.id.IVElementImage)
        setImageToImageView(image, element.image?.url.toString())
        image.contentDescription =
            "${element.image?.title.toString()}\n\n${element.image?.attribution.toString()}"
    }

    private fun setImageToImageView(imageView: ImageView, URL: String) {
        loadingImageProgressBar = findViewById(R.id.PBElementDetails)
        errorImageView = findViewById(R.id.IVElementLoadError)
        val loadingImageSnackBar: Snackbar = Snackbar.make(
            loadingImageProgressBar, "Loading image...", Snackbar.LENGTH_INDEFINITE
        )

        loadingImageProgressBar.visibility = ProgressBar.VISIBLE
        errorImageView.visibility = ImageView.GONE
        loadingImageSnackBar.show()

        Glide.with(this)
            .load(URL)
            .listener((object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return loadFailed(imageView, URL, loadingImageSnackBar)
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return resourceReady(loadingImageSnackBar)
                }
            })).into(imageView)
    }

    private fun loadFailed(
        target: ImageView,
        URL: String,
        loadingImageSnackBar: Snackbar
    ): Boolean {
        resourceReady(loadingImageSnackBar)
        errorImageView.visibility = ImageView.VISIBLE

        Snackbar.make(
            errorImageView, "Error loading the image", Snackbar.LENGTH_SHORT
        ).setBackgroundTint(getColor(R.color.pantone_1625_c)).show()

        errorImageView.setOnClickListener {
            it.visibility = ImageView.GONE
            setImageToImageView(target, URL)
        }
        return false
    }

    private fun resourceReady(loadingImageSnackBar: Snackbar): Boolean {
        loadingImageProgressBar.visibility = ProgressBar.GONE
        loadingImageSnackBar.dismiss()
        return false
    }

    private fun setupElementFavorite() {
        val favorite: ImageView = findViewById(R.id.IVElementFavorite)
        val isFavoriteDrawable =
            AppCompatResources.getDrawable(this, R.drawable.ic_baseline_bookmark_24)
        val notFavoriteDrawable =
            AppCompatResources.getDrawable(this, R.drawable.ic_baseline_bookmark_border_24)

        favorite.setImageDrawable(
            if (element.favorite) isFavoriteDrawable
            else notFavoriteDrawable
        )

        favorite.setOnClickListener {
            element.favorite = !element.favorite
            saveFavoritePreference(element)

            if (element.favorite) {
                favorite.setImageDrawable(isFavoriteDrawable)
                Snackbar.make(it, "Added to favorites", Snackbar.LENGTH_SHORT).show()
            } else {
                favorite.setImageDrawable(notFavoriteDrawable)
                Snackbar.make(it, "Removed from favorites", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveFavoritePreference(element: Element) {
        val sharedPreferences = getSharedPreferences(
            "PeriodicTablePreferences",
            MODE_PRIVATE
        )
        val editor = sharedPreferences.edit()
        val periodicTable = gson.fromJson(
            sharedPreferences.getString("periodicTable", null),
            PeriodicTable::class.java
        )
        periodicTable.elements[element.number!! - 1].favorite = element.favorite
        editor.putString("periodicTable", gson.toJson(periodicTable, PeriodicTable::class.java))
        editor.apply()
    }
}