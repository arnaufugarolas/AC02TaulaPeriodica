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
    private lateinit var categoriesMenuItems: MutableList<MenuItem>
    private lateinit var phaseMenuItems: MutableList<MenuItem>
    private lateinit var appliedCategories: MutableList<String>
    private lateinit var appliedPhases: MutableList<String>
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
            "appliedFilters",
            gson.toJson(appliedCategories.toTypedArray(), Array<String>::class.java)
        )
        outState.putBoolean("favorite", favorite)
        outState.putString("shortBy", shortBy)

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        appliedCategories = gson.fromJson(
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
        recyclerView.setItemViewCacheSize(10)
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
        updateActionBarName()
        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun updateActionBarName() {
        if (elementsToShow.size == periodicTable.elements.size) {
            supportActionBar?.title = "Periodic Table"
        } else {
            supportActionBar?.title =
                "Showing: (${elementsToShow.size}/${periodicTable.elements.size})"
        }
    }

    private fun setupFilterMenu(subMenu: SubMenu) {
        val allCategoriesSwitch = subMenu.findItem(R.id.MIAllCategories)
        val allPhasesSwitch = subMenu.findItem(R.id.MIAllPhases)
        val favoriteSwitch = subMenu.findItem(R.id.MIFavorite)

        categoriesMenuItems = mutableListOf(
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

        phaseMenuItems = mutableListOf(
            subMenu.findItem(R.id.MIGas),
            subMenu.findItem(R.id.MILiquid),
            subMenu.findItem(R.id.MISolid)
        )

        if (::appliedPhases.isInitialized) {
            for (phase in appliedPhases) {
                phaseMenuItems.find { it.title == phase }?.isChecked = true
            }
            if (appliedPhases.size == phaseMenuItems.size) {
                allPhasesSwitch.isChecked = true
            }
        } else {
            for (phase in phaseMenuItems) phase.isChecked = true
            allPhasesSwitch.isChecked = true
            appliedPhases = mutableListOf(
                "Gas",
                "Liquid",
                "Solid"
            )
        }


        if (::appliedCategories.isInitialized) {
            for (filter in appliedCategories) {
                categoriesMenuItems.find { it.title == filter }?.isChecked = true
            }
            if (appliedCategories.size == categoriesMenuItems.size) {
                allCategoriesSwitch.isChecked = true
            }
        } else {
            for (filter in categoriesMenuItems) {
                filter.isChecked = true
            }
            allCategoriesSwitch.isChecked = true
            appliedCategories = mutableListOf(
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

        allCategoriesSwitch.setOnMenuItemClickListener {
            changeFilters(it)
            true
        }

        allPhasesSwitch.setOnMenuItemClickListener {
            changeFilters(it)
            true
        }

        categoriesMenuItems.forEach { menuItem ->
            menuItem.setOnMenuItemClickListener {
                var allChecked = true

                changeFilters(it)

                for (i in 0 until categoriesMenuItems.size) {
                    if (!categoriesMenuItems[i].isChecked) {
                        allChecked = false
                        break
                    }
                }

                allCategoriesSwitch.isChecked = allChecked

                true
            }
        }

        phaseMenuItems.forEach { menuItem ->
            menuItem.setOnMenuItemClickListener {
                var allChecked = true

                changeFilters(it)

                for (i in 0 until phaseMenuItems.size) {
                    if (!phaseMenuItems[i].isChecked) {
                        allChecked = false
                        break
                    }
                }

                allPhasesSwitch.isChecked = allChecked

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
        } else if (item.itemId == R.id.MIAllCategories) {
            if (item.isChecked) {
                appliedCategories = mutableListOf(
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
                categoriesMenuItems.forEach { it.isChecked = true }
            } else {
                appliedCategories = mutableListOf()
                categoriesMenuItems.forEach { it.isChecked = false }
            }
        } else if (item.itemId == R.id.MIAllPhases) {
            if (item.isChecked) {
                appliedPhases = mutableListOf(
                    "Gas",
                    "Liquid",
                    "Solid"
                )
                phaseMenuItems.forEach { it.isChecked = true }
            } else {
                appliedPhases = mutableListOf()
                phaseMenuItems.forEach { it.isChecked = false }
            }
        } else if (item.groupId == R.id.GCategories) {
            if (item.isChecked) appliedCategories.add(item.title.toString())
            else appliedCategories.remove(item.title.toString())
        } else if (item.groupId == R.id.GPhases) {
            if (item.isChecked) appliedPhases.add(item.title.toString())
            else appliedPhases.remove(item.title.toString())
        }

        filterElements()
    }

    private fun filterElements() {
        var elementsToFilter = periodicTable.elements.toMutableList()
        val phaseFilteredElements = mutableListOf<Element>()
        val categoryFilteredElements = mutableListOf<Element>()
        if (favorite) elementsToFilter = elementsToFilter.filter { it.favorite }.toMutableList()

        for (phase in appliedPhases) {
            phaseFilteredElements.addAll(elementsToFilter.filter { it.phase == phase })
        }

        for (category in appliedCategories) {
            categoryFilteredElements.addAll(phaseFilteredElements.filter {
                getCategory(it.category.toString()) == category
            })
        }

        elementsToShow = categoryFilteredElements.toMutableList()
        updateActionBarName()
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
