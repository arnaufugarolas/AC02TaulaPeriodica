package com.mp08.ac02taulaperidica

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.mp08.ac02taulaperidica.dataClass.PeriodicTable


class MainActivity : AppCompatActivity() {
    private lateinit var periodicTable: PeriodicTable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        periodicTable = getPeriodicTable()

        main()
    }

    fun main () {
        val recyclerView = findViewById<View>(R.id.RVElements) as RecyclerView
    }

    private fun getPeriodicTable(): PeriodicTable {
        val data = application.assets.open("PeriodicTableJSON.json").bufferedReader().use { it.readText() }
        val gson = Gson()
        return gson.fromJson(data, PeriodicTable::class.java)
    }

}


