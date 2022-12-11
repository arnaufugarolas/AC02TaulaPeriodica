package com.mp08.ac02taulaperidica

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.mp08.ac02taulaperidica.dataClass.Element
import com.mp08.ac02taulaperidica.dataClass.PeriodicTable

class GameActivity : AppCompatActivity() {
    private lateinit var periodicTable: PeriodicTable
    private lateinit var topScoreTextView: TextView
    private lateinit var actualScoreTextView: TextView
    private lateinit var elementSymbolTextView: TextView
    private lateinit var gameLostTextView: TextView
    private lateinit var solidButton: Button
    private lateinit var liquidButton: Button
    private lateinit var restartButton: Button
    private lateinit var gasButton: Button

    private var actualElement: Element? = null
    private var gameTopScore: Int = -1
    private var gameActualScore: Int = -1
    private var gson = Gson()
    private var gameState = "Waiting"

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        gameActualScore = savedInstanceState.getInt("gameScore")
        gameTopScore = savedInstanceState.getInt("gameTopScore")
        actualElement =
            gson.fromJson(savedInstanceState.getString("actualElement"), Element::class.java)
        gameState = savedInstanceState.getString("gameState").toString()
        init()
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("gameTopScore", gameTopScore)
        outState.putInt("gameScore", gameActualScore)
        outState.putString("actualElement", gson.toJson(actualElement, Element::class.java))
        outState.putString("gameState", gameState)
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        if (savedInstanceState == null) init()
    }

    private fun init() {
        setupInterface()
        loadPeriodicTable()
        setupButtonsListener()
        setupGame()
    }

    private fun setupInterface() {
        topScoreTextView = findViewById(R.id.TVGameMaxScoreData)
        actualScoreTextView = findViewById(R.id.TVGameActualScoreData)
        elementSymbolTextView = findViewById(R.id.TVGameElementSymbol)
        gameLostTextView = findViewById(R.id.TVGameLostLabel)
        solidButton = findViewById(R.id.BGameAnswerSolid)
        liquidButton = findViewById(R.id.BGameAnswerLiquid)
        gasButton = findViewById(R.id.BGameAnswerGas)
        restartButton = findViewById(R.id.BGameRestart)

        loadTopScoreFromPreferences()
        updateScoreUI()
    }

    private fun loadTopScoreFromPreferences() {
        val sharedPreferences = getSharedPreferences("PeriodicTableGamePreferences", MODE_PRIVATE)
        gameTopScore = sharedPreferences.getInt("topScore", -1)
    }

    private fun updateScoreUI() {
        actualScoreTextView.text = gameActualScore.toString()
        topScoreTextView.text = gameTopScore.toString()
    }

    private fun loadPeriodicTable() {
        val data = application.assets.open("PeriodicTableJSON.json")
            .bufferedReader()
            .use { it.readText() }

        periodicTable = gson.fromJson(data, PeriodicTable::class.java)
    }

    private fun setupButtonsListener() {
        solidButton.setOnClickListener {
            onClickPhaseButton("Solid")
        }

        liquidButton.setOnClickListener {
            onClickPhaseButton("Liquid")
        }

        restartButton.setOnClickListener {
            onClickRestartButton()
        }

        gasButton.setOnClickListener {
            onClickPhaseButton("Gas")
        }
    }

    private fun onClickPhaseButton(phase: String) {
        if (gameState == "Playing") {
            if (actualElement!!.phase == phase) onCorrectAnswer()
            else onWrongAnswer()
            game()
        }
    }

    private fun onCorrectAnswer() {
        setGameScore(gameActualScore + 1)
        actualElement = null
        gameState = "Waiting"
    }

    private fun setGameScore(score: Int) {
        gameActualScore = score
        if (gameActualScore > gameTopScore) {
            gameTopScore = gameActualScore
            saveTopScoreOnPreferences()
        }
        updateScoreUI()
    }

    private fun saveTopScoreOnPreferences() {
        val sharedPreferences = getSharedPreferences("PeriodicTableGamePreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("topScore", gameTopScore)
        editor.apply()
    }

    private fun onWrongAnswer() {
        gameState = "GameOver"
    }

    private fun onClickRestartButton() {
        actualElement = null
        setupGame()
    }

    private fun setupGame() {
        setGameScore(0)
        gameState = "Waiting"
        switchGameVisibility()
        game()
    }

    private fun game() {
        if (gameState == "Waiting") onGameWaiting()
        else if (gameState == "GameOver") onGameOver()
    }

    private fun onGameWaiting() {
        setElement()
        gameState = "Playing"
    }

    private fun setElement() {
        if (actualElement == null) actualElement = periodicTable.elements.random()
        elementSymbolTextView.text = actualElement!!.symbol
    }

    private fun onGameOver() {
        switchGameVisibility()
        val lostDialog = GameLostDialog(actualElement!!)
        lostDialog.show(supportFragmentManager, "GameLostDialog")
    }

    private fun switchGameVisibility() {
        if (gameState == "GameOver") {
            elementSymbolTextView.visibility = View.INVISIBLE
            solidButton.visibility = View.INVISIBLE
            liquidButton.visibility = View.INVISIBLE
            gasButton.visibility = View.INVISIBLE
            restartButton.visibility = View.VISIBLE
            gameLostTextView.visibility = View.VISIBLE
        } else {
            elementSymbolTextView.visibility = View.VISIBLE
            solidButton.visibility = View.VISIBLE
            liquidButton.visibility = View.VISIBLE
            gasButton.visibility = View.VISIBLE
            restartButton.visibility = View.INVISIBLE
            gameLostTextView.visibility = View.INVISIBLE
        }
    }
}