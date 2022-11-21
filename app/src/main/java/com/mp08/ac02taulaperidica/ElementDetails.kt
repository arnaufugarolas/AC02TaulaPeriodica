package com.mp08.ac02taulaperidica

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mp08.ac02taulaperidica.dataClass.Element

class ElementDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_element_details)
        startActivity(intent as Intent)

        val myintend = intent.getSerializableExtra("element")
        val element = myintend as Element

        startActivity(a)
    }
}