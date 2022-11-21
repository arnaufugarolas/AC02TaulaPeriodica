package com.mp08.ac02taulaperidica

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.mp08.ac02taulaperidica.dataClass.Element
import com.mp08.ac02taulaperidica.dataClass.PeriodicTable


class MainActivity : AppCompatActivity() {
    private lateinit var periodicTable: PeriodicTable
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Periodic Table"

        // mostramos el icono
        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        periodicTable = getPeriodicTable()
        recyclerView = findViewById(R.id.RVElements)

        main()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    fun main () {
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


