package com.mp08.ac02taulaperidica

import android.graphics.drawable.Drawable
import android.os.Bundle
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
import com.mp08.ac02taulaperidica.dataClass.PeriodicTable

class ElementDetails : AppCompatActivity() {
    private lateinit var element: Element
    private var gson: Gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_element_details)
        element = getElementDetails(intent.getStringExtra("element"))
        loadElementDetails()
    }

    private fun loadElementDetails() {
        val image = findViewById<ImageView>(R.id.IVElementImage)
        val favorite: ImageView = findViewById(R.id.IVElementFavorite)
        val symbol: TextView = findViewById(R.id.TVElementSymbol)
        val name: TextView = findViewById(R.id.TVElementNameData)
        val appearance: TextView = findViewById(R.id.TVElementAppearanceData)
        val atomicMass: TextView = findViewById(R.id.TVElementAtomicMassData)
        val boil: TextView = findViewById(R.id.TVElementBoilData)
        val category: TextView = findViewById(R.id.TVElementCategoryData)
        val density: TextView = findViewById(R.id.TVElementDensityData)
        val discoveredBy: TextView = findViewById(R.id.TVElementDiscoveredByData)
        val melt: TextView = findViewById(R.id.TVElementMeltData)
        val molarHeat: TextView = findViewById(R.id.TVElementMolarHeatData)
        val namedBy: TextView = findViewById(R.id.TVElementNamedByData)
        val number: TextView = findViewById(R.id.TVElementNumberData)
        val period: TextView = findViewById(R.id.TVElementPeriodData)
        val xPosition: TextView = findViewById(R.id.TVElementXPositionData)
        val yPosition: TextView = findViewById(R.id.TVElementYPositionData)
        val phase: TextView = findViewById(R.id.TVElementPhaseData)
        val shells: TextView = findViewById(R.id.TVElementShellsData)
        val electronConfiguration: TextView = findViewById(R.id.TVElementElectronConfigurationData)
        val electronConfigurationSemantic: TextView =
            findViewById(R.id.TVElementElectronConfigurationSemanticData)
        val electronAffinity: TextView = findViewById(R.id.TVElementElectronAffinityData)
        val electronegativityPauling: TextView =
            findViewById(R.id.TVElementElectronegativityPaulingData)
        val ionizationEnergies: TextView = findViewById(R.id.TVElementIonizationEnergiesData)
        val summary: TextView = findViewById(R.id.TVElementSummaryData)

        supportActionBar?.title = element.name
        setImageToImageView(image, element.image?.url.toString())
        image.contentDescription =
            "${element.image?.title.toString()}\n\n${element.image?.attribution.toString()}"
        favorite.setImageResource(
            if (element.favorite) R.drawable.ic_baseline_bookmark_24
            else R.drawable.ic_baseline_bookmark_border_24
        )

        symbol.text = element.symbol
        name.text = element.name
        atomicMass.text = element.atomicMass.toString()
        appearance.text = element.appearance
        boil.text = element.boil.toString()
        category.text = element.category
        density.text = element.density.toString()
        discoveredBy.text = element.discoveredBy
        melt.text = element.melt.toString()
        molarHeat.text = element.molarHeat.toString()
        namedBy.text = element.namedBy
        number.text = element.number.toString()
        period.text = element.period.toString()
        xPosition.text = element.xpos.toString()
        yPosition.text = element.ypos.toString()
        phase.text = element.phase
        shells.text = element.shells.toString()
        electronConfiguration.text = element.electronConfiguration
        electronConfigurationSemantic.text = element.electronConfigurationSemantic
        electronAffinity.text = element.electronAffinity.toString()
        electronegativityPauling.text = element.electronegativityPauling.toString()
        ionizationEnergies.text = element.ionizationEnergies.toString()
        summary.text = element.summary

        favorite.setOnClickListener {
            element.favorite = !element.favorite
            saveFavoritePreference(element)

            if (element.favorite) {
                favorite.setImageResource(R.drawable.ic_baseline_bookmark_24)
                Snackbar.make(it, "Added to favorites", Snackbar.LENGTH_SHORT).show()
            } else {
                favorite.setImageResource(R.drawable.ic_baseline_bookmark_border_24)
                Snackbar.make(it, "Removed from favorites", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveFavoritePreference(element: Element) {
        val sharedPreferences = getSharedPreferences(
            "PeriodicTablePreferences",
            MODE_PRIVATE
        )
        val persistentPeriodicTable = sharedPreferences.getString("periodicTable", null)
        val periodicTable = gson.fromJson(persistentPeriodicTable, PeriodicTable::class.java)
        periodicTable.elements[element.number!! - 1].favorite = element.favorite
        val editor = sharedPreferences.edit()
        editor.putString("periodicTable", gson.toJson(periodicTable, PeriodicTable::class.java))
        editor.apply()
    }

    private fun getElementDetails(element: String?): Element {
        return gson.fromJson(element, Element::class.java)
    }

    private fun setImageToImageView(imageView: ImageView, URL: String) {
        val progressBar = findViewById<ProgressBar>(R.id.PBElementDetails)
        val errorImage = findViewById<ImageView>(R.id.IVElementLoadError)
        val snackbarLoading: Snackbar = Snackbar.make(
            progressBar, "Loading image...", Snackbar.LENGTH_INDEFINITE
        )

        progressBar.visibility = ProgressBar.VISIBLE
        errorImage.visibility = ImageView.GONE
        snackbarLoading.show()

        Glide.with(this).load(URL).listener((object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean
            ): Boolean {
                return loadFailed(imageView, URL, errorImage, progressBar, snackbarLoading)
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                return resourceReady(progressBar, snackbarLoading)
            }
        })).into(imageView)
    }

    private fun resourceReady(progressBar: ProgressBar, snackbarLoading: Snackbar): Boolean {
        progressBar.visibility = ProgressBar.GONE
        snackbarLoading.dismiss()
        return false
    }

    private fun loadFailed(
        target: ImageView,
        URL: String,
        errorImageView: ImageView,
        progressBar: ProgressBar,
        snackbarLoading: Snackbar
    ): Boolean {
        resourceReady(progressBar, snackbarLoading)
        errorImageView.visibility = ImageView.VISIBLE

        Snackbar.make(
            errorImageView, "Error loading the image", Snackbar.LENGTH_SHORT
        ).setBackgroundTint(getColor(R.color.pantone_1625_c)).show()

        errorImageView.setOnClickListener {
            errorImageView.visibility = ImageView.GONE
            setImageToImageView(target, URL)
        }
        return false
    }
}