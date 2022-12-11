package com.mp08.ac02taulaperidica

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.mp08.ac02taulaperidica.dataClass.Element
import com.mp08.ac02taulaperidica.dataClass.PeriodicTable

class MainActivity : AppCompatActivity() {
    private lateinit var periodicTable: PeriodicTable
    private lateinit var recyclerView: RecyclerView
    private lateinit var elementsToShow: MutableList<Element>
    private lateinit var filterMenuItems: MutableList<MenuItem>
    private lateinit var appliedFilters: MutableList<String>
    private var favorite = false
    private var shortBy = "Number"
    private var gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Debug", "onCreate")
        getPeriodicTable()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    override fun onStart() {
        Log.d("Debug", "onStart")
        checkPeriodicTable()
        super.onStart()
    }

    private fun checkPeriodicTable() {
        val oldPeriodicTable = periodicTable.elements.toMutableList()
        getPeriodicTable()
        val newPeriodicTable = periodicTable.elements.toMutableList()

        for (element in newPeriodicTable) {
            val elementIndex = newPeriodicTable.indexOf(element)
            if (elementIndex != -1) {
                val oldElement = oldPeriodicTable[elementIndex]
                if (element.favorite != oldElement.favorite) {
                    if (elementsToShow.contains(oldElement)) {
                        elementsToShow[elementsToShow.indexOf(oldElement)] = element
                        recyclerView.adapter?.notifyItemChanged(elementsToShow.indexOf(element))
                    }
                }
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(
            "appliedFilters", gson.toJson(appliedFilters.toTypedArray(), Array<String>::class.java)
        )
        outState.putBoolean("favorite", favorite)
        outState.putString("shortBy", shortBy)

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        appliedFilters = gson.fromJson(
            savedInstanceState.getString("appliedFilters"), Array<String>::class.java
        ).toMutableList()
        favorite = savedInstanceState.getBoolean("favorite")
        shortBy = savedInstanceState.getString("shortBy").toString()
        filterElements()

        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        setupFilterMenu(menu.findItem(R.id.MIFilter).subMenu!!)
        setupOrderMenu(menu.findItem(R.id.MIOrderBy).subMenu!!)
        menu.findItem(R.id.MIGame).setOnMenuItemClickListener {
            startActivity(Intent(this, GameActivity::class.java))
            true
        }
        return true
    }

    private fun getPeriodicTable() {
        val sharedPreferences = getSharedPreferences("PeriodicTablePreferences", MODE_PRIVATE)
        val persistentPeriodicTable = sharedPreferences.getString("periodicTable", null)
        if (persistentPeriodicTable != null) {
            Log.d("Debug", "PeriodicTable restored")
            periodicTable = gson.fromJson(persistentPeriodicTable, PeriodicTable::class.java)
        } else {
            Log.d("Debug", "PeriodicTable created")
            periodicTable = loadPeriodicTable()
            savePeriodicTableOnPreferences()
        }
    }

    private fun savePeriodicTableOnPreferences() {
        Log.d("Debug", "saveSharedPreferences")
        val sharedPreferences = getSharedPreferences("PeriodicTablePreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("periodicTable", gson.toJson(periodicTable))
        editor.apply()
    }

    private fun init() {
        Log.d("Debug", "Init")
        setupRecycledView()
        setupSupportActionBar()
    }

    private fun setupRecycledView() {
        recyclerView = findViewById(R.id.RVElements)
        if (!::elementsToShow.isInitialized) {
            elementsToShow = periodicTable.elements.toMutableList()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.setItemViewCacheSize(10);
        setRecycledViewValues()
    }

    private fun setRecycledViewValues() {
        val adapter = ElementsAdapter(this, elementsToShow)
        recyclerView.adapter = adapter
    }

    private fun loadPeriodicTable(): PeriodicTable {
        val data =
            application.assets.open("PeriodicTableJSON.json").bufferedReader().use { it.readText() }
        return gson.fromJson(data, PeriodicTable::class.java)
    }

    private fun setupSupportActionBar() {
        supportActionBar?.title = "Periodic Table"
        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun setupFilterMenu(subMenu: SubMenu) {
        val allFiltersSwitch = subMenu.findItem(R.id.MIAll)
        val favoriteSwitch = subMenu.findItem(R.id.MIFavorite)
        filterMenuItems = mutableListOf(
            subMenu.findItem(R.id.MINonMetal),
            subMenu.findItem(R.id.MINobleGas),
            subMenu.findItem(R.id.MIAlkali),
            subMenu.findItem(R.id.MIAlkaline),
            subMenu.findItem(R.id.MIMetalloid),
            subMenu.findItem(R.id.MIPostTransition),
            subMenu.findItem(R.id.MITransition),
            subMenu.findItem(R.id.MILanthanoid),
            subMenu.findItem(R.id.MIActinoid),
            subMenu.findItem(R.id.MINoCategory)
        )

        if (::appliedFilters.isInitialized) {
            for (filter in appliedFilters) {
                filterMenuItems.find { it.title == filter }?.isChecked = true
            }
            if (appliedFilters.size == filterMenuItems.size) {
                allFiltersSwitch.isChecked = true
            }
        } else {
            for (filter in filterMenuItems) {
                filter.isChecked = true
            }
            allFiltersSwitch.isChecked = true
            appliedFilters = mutableListOf(
                "Non Metal",
                "Noble Gas",
                "Alkali",
                "Alkaline",
                "Metalloid",
                "Post-Transition",
                "Transition",
                "Lanthanoid",
                "Actinoid",
                "No Category"
            )
        }

        favoriteSwitch.isChecked = favorite

        favoriteSwitch.setOnMenuItemClickListener {
            changeFilters(it)
            true
        }

        allFiltersSwitch.setOnMenuItemClickListener {
            changeFilters(it)
            true
        }

        filterMenuItems.forEach { menuItem ->
            menuItem.setOnMenuItemClickListener {
                var allChecked = true

                changeFilters(it)

                for (i in 0 until filterMenuItems.size) {
                    if (!filterMenuItems[i].isChecked) {
                        allChecked = false
                        break
                    }
                }

                allFiltersSwitch.isChecked = allChecked

                true
            }
        }
    }

    private fun setupOrderMenu(subMenu: SubMenu) {
        subMenu.findItem(R.id.MINumber).setOnMenuItemClickListener {
            it.isChecked = !it.isChecked
            shortBy = it.title.toString()
            shortElements()
            true
        }

        subMenu.findItem(R.id.MISymbol).setOnMenuItemClickListener {
            it.isChecked = !it.isChecked
            shortBy = it.title.toString()
            shortElements()
            true
        }
    }

    private fun changeFilters(item: MenuItem) {
        item.isChecked = !item.isChecked

        if (item.itemId == R.id.MIFavorite) {
            favorite = item.isChecked
        } else if (item.itemId == R.id.MIAll) {
            if (item.isChecked) {
                appliedFilters = mutableListOf(
                    "Non Metal",
                    "Noble Gas",
                    "Alkali",
                    "Alkaline",
                    "Metalloid",
                    "Post-Transition",
                    "Transition",
                    "Lanthanoid",
                    "Actinoid",
                    "No Category"
                )
                filterMenuItems.forEach { it.isChecked = true }
            } else {
                appliedFilters = mutableListOf()
                filterMenuItems.forEach { it.isChecked = false }
            }
        } else {
            if (item.isChecked) appliedFilters.add(item.title.toString())
            else appliedFilters.remove(item.title.toString())
        }

        filterElements()
    }

    private fun filterElements() {
        var elementsToFilter = periodicTable.elements.toMutableList()
        val filteredElements = mutableListOf<Element>()
        if (favorite) elementsToFilter = elementsToFilter.filter { it.favorite }.toMutableList()

        for (category in appliedFilters) filteredElements.addAll(elementsToFilter.filter {
            getCategory(it.category.toString()) == category
        })

        elementsToShow = filteredElements.toMutableList()

        shortElements()
    }

    private fun shortElements() {
        if (shortBy == "Number") elementsToShow.sortBy { it.number }
        else elementsToShow.sortBy { it.symbol }

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
            "post-transition metal" -> "Post-Transition"
            "transition metal" -> "Transition"
            "lanthanide" -> "Lanthanoid"
            "actinide" -> "Actinoid"
            else -> "No Category"
        }
    }
}
