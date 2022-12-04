package com.mp08.ac02taulaperidica

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.mp08.ac02taulaperidica.dataClass.Element
import com.mp08.ac02taulaperidica.dataClass.PeriodicTable

class MainActivity : AppCompatActivity() {
    private lateinit var periodicTableDefault: PeriodicTable
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewElements: MutableList<Element>
    private var filterMenuItems = mutableListOf<MenuItem>()
    private var shortBy = "Number"
    private var filters = mutableListOf<String>(
        "Non Metal",
        "Noble Gas",
        "Alkali",
        "Alkaline",
        "Metalloid",
        "Post",
        "Transition",
        "Lanthanoid",
        "Actinoid",
        "No Category"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        for (i in 0 until 11) {
            filterMenuItems.add(menu.getItem(1).subMenu?.getItem(i)!!)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.groupId == R.id.GFilter) changeFilter(item)
        else if (item.groupId == R.id.GOderBy) changeOrder(item)
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        setupRecycledView()
        setupSupportActionBar()
    }

    private fun setupRecycledView() {
        recyclerView = findViewById(R.id.RVElements)
        periodicTableDefault = loadPeriodicTable()
        recyclerViewElements = periodicTableDefault.elements.toMutableList()

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        setRecycledViewValues()
    }

    private fun setRecycledViewValues() {
        val adapter = ElementsAdapter(recyclerViewElements)
        recyclerView.adapter = adapter
    }

    private fun loadPeriodicTable(): PeriodicTable {
        val data =
            application.assets.open("PeriodicTableJSON.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        return gson.fromJson(data, PeriodicTable::class.java)
    }

    private fun setupSupportActionBar() {
        supportActionBar?.title = "Periodic Table"
        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun changeFilter(item: MenuItem) {
        item.isChecked = !item.isChecked

        if (item.itemId == R.id.MIAll) {
            if (item.isChecked) {
                filters = mutableListOf(
                    "Non Metal",
                    "Noble Gas",
                    "Alkali",
                    "Alkaline",
                    "Metalloid",
                    "Post",
                    "Transition",
                    "Lanthanoid",
                    "Actinoid",
                    "No Category"
                )
                // filtersMenuItems.forEach { (it as MenuItem).isChecked = true }
            }
        }

        if (item.isChecked) {
            filters.remove(item.title.toString())
            filterElements()
        } else {
            filters.add(item.title.toString())
            filterElements()
        }
    }

    private fun changeOrder(item: MenuItem) {
        item.isChecked = !item.isChecked
        shortBy = item.title.toString()
        shortElements()
    }

    private fun shortElements() {
        if (shortBy == "Number") recyclerViewElements.sortBy { it.number }
        else recyclerViewElements.sortBy { it.symbol }

        setRecycledViewValues()
    }

    private fun filterElements() {
        var elementsToFilter = periodicTableDefault.elements.toMutableList()
        val filtersToApply = filters.toMutableList()
        val filteredElements = mutableListOf<Element>()

        if (filtersToApply.contains("Favorite")) {
            filtersToApply.remove("Favorite")
            elementsToFilter = elementsToFilter.filter { it.favorite }.toMutableList()
        }

        for (category in filters)
            filteredElements.addAll(elementsToFilter.filter {
                getCategory(it.category.toString()) == category
            })

        recyclerViewElements = filteredElements.toMutableList()

        shortElements()
        setRecycledViewValues()
    }

    private fun getCategory(category: String): String {
        return when (category) {
            "diatomic nonmetal" -> "Non Metal"
            "noble gas" -> "Noble Gas"
            "alkali metal" -> "Alkali"
            "alkaline earth metal" -> "Alkaline"
            "metalloid" -> "Metalloid"
            "polyatomic nonmetal" -> "Non Metal"
            "post-transition metal" -> "Post"
            "transition metal" -> "Transition"
            "lanthanide" -> "Lanthanoid"
            "actinide" -> "Actinoid"
            else -> "No Category"
        }
    }
}
