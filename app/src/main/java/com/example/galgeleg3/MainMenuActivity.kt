package com.example.galgeleg3

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_mainmenu.*

class MainMenuActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var drdkbutton: Button
    private lateinit var regnearkbutton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainmenu)

        drdkbutton = findViewById(R.id.button)
        drdkbutton.setOnClickListener(this)

        regnearkbutton = findViewById(R.id.button2)
        regnearkbutton.setOnClickListener(this)
    }


    override fun onClick(v: View) {
        if (v == drdkbutton) {
            val i = Intent(this, PlayActivity::class.java)
            i.putExtra("type", "DR")
            startActivity(i)
        }

        if (v == regnearkbutton) {
            val i = Intent(this, PlayActivity::class.java)
            i.putExtra("type", "Regneark")
            startActivity(i)
        }
    }

}