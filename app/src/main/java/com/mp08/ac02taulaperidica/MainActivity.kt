package com.mp08.ac02taulaperidica

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.mp08.ac02taulaperidica.dataClass.PeriodicTable


class MainActivity : AppCompatActivity() {
    private lateinit var periodicTable: PeriodicTable
    private lateinit var recyclerView: RecyclerView
    private lateinit var filters: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Periodic Table"

        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        filters = mutableListOf()

        periodicTable = getPeriodicTable()
        recyclerView = findViewById(R.id.RVElements)

        setupRecycledView()
    }


    fun filterElements(category: String) {
        var filteredElements = periodicTable.elements.toMutableList()
        if (category != "") {
            filteredElements = if (category == "Favorite") {
                filteredElements.filter { it.favorite }.toMutableList()
            } else {
                filteredElements.filter { getCategory(it.category.toString()) == category }.toMutableList()
            }
        }

        val adapter = ElementsAdapter(filteredElements)
        recyclerView.adapter = adapter
    }

    fun getCategory(category: String): String {
        return when(category){
            "diatomic nonmetal" -> "NonMetal"
            "noble gas" -> "NobleGas"
            "alkali metal" -> "Alkali"
            "alkaline earth metal" -> "Alkaline"
            "metalloid" -> "Metalloid"
            "polyatomic nonmetal" -> "NonMetal"
            "post-transition metal" -> "Post"
            "transition metal" -> "Transition"
            "lanthanide" -> "Lanthanoid"
            "actinide" -> "Actinoid"
            else -> "NoCategory"
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.groupId != R.id.GFilter) return super.onOptionsItemSelected(item)

        if (item.isChecked) {
            filters.remove(item.title.toString())
            item.isChecked = false
            filterElements("")
        } else {
            filters.add(item.title.toString())
            item.isChecked = true
            filterElements(item.title.toString())
        }



        return super.onOptionsItemSelected(item)
    }

    private fun setupRecycledView () {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        val adapter = ElementsAdapter(periodicTable.elements)
        recyclerView.adapter = adapter
    }

    private fun getPeriodicTable(): PeriodicTable {
        val data = application.assets.open("PeriodicTableJSON.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        return gson.fromJson(data, PeriodicTable::class.java)
    }
}


