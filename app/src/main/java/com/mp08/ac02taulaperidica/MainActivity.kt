package com.mp08.ac02taulaperidica

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.mp08.ac02taulaperidica.dataClass.PeriodicTable


class MainActivity : AppCompatActivity() {
    private lateinit var periodicTable: PeriodicTable
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Periodic Table"

        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        periodicTable = getPeriodicTable()
        recyclerView = findViewById(R.id.RVElements)

        setupRecycledView()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
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


