package com.mp08.ac02taulaperidica

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.mp08.ac02taulaperidica.dataClass.PeriodicTable

class GameActivity : AppCompatActivity() {
    private lateinit var periodicTable: PeriodicTable
    private var gameTopScore: Int = -1
    private var gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!::periodicTable.isInitialized) restoreSharedPreferences()
        setContentView(R.layout.activity_game)
        init()
    }

    override fun onStart() {
        if (!::periodicTable.isInitialized) restoreSharedPreferences()
        super.onStart()
    }

    private fun init() {
        if (!::periodicTable.isInitialized) loadPeriodicTable()
        findViewById<TextView>(R.id.TVMaxPoints).text = gameTopScore.toString()
    }

    private fun loadPeriodicTable(): PeriodicTable {
        val data =
            application.assets.open("PeriodicTableJSON.json").bufferedReader().use { it.readText() }
        return gson.fromJson(data, PeriodicTable::class.java)
    }

    private fun restoreSharedPreferences() {
        val sharedPreferences = getSharedPreferences("sharedPreferences", MODE_PRIVATE)
        val persistentPeriodicTable = sharedPreferences.getString("periodicTable", null)
        gameTopScore = sharedPreferences.getInt("topScore", 0)
        if (persistentPeriodicTable != null) {
            periodicTable = gson.fromJson(persistentPeriodicTable, PeriodicTable::class.java)
        }

    }
}