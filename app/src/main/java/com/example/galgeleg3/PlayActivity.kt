package com.example.galgeleg3

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager.getDefaultSharedPreferences
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import galgeleg.Galgelogik
import java.lang.reflect.Type
import java.sql.Types.NULL
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors.newSingleThreadExecutor
import kotlin.collections.ArrayList


class PlayActivity : AppCompatActivity() {
    private lateinit var txtinput: TextView
    private lateinit var livestxt: TextView
    private lateinit var debugtxt: TextView
    private lateinit var debugtxt2: TextView
    private lateinit var galgeimg: ImageView
    private val galgelogik: Galgelogik = Galgelogik.getInstance()
    private val handler: Handler = Handler()
    private val bgthread: Executor = newSingleThreadExecutor()
    private lateinit var getWordLocation: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        if (intent!!.hasExtra("type")) {
            getWordLocation = intent.getStringExtra("type")!!
            saveArrayList(arrayListOf(getWordLocation), "wordsString")
        } else {
            getWordLocation = getArrayList("wordsString")?.get(0).toString()
        }

        // if there is no saved data
        if (getArrayList(getWordLocation).isNullOrEmpty()) {
            // background thread does network stuff, and saves the retrieved data locally in storage
            bgthread.execute {
                println("Getting word from $getWordLocation")
                galgelogik.hentOrdOnline(getWordLocation)
                saveArrayList(galgelogik.muligeOrd, getWordLocation)
                galgelogik.setMuligeOrd(getArrayList(getWordLocation))

                // main thread starts game and updates UI once BGthread is done networking
                handler.post {
                    galgelogik.startNytSpil()
                    initUI()
                }
            }
        } else { // else, load the saved data and load the UI
            println("Getting word from cache...")
            galgelogik.setMuligeOrd(getArrayList(getWordLocation))
            galgelogik.startNytSpil()
            initUI()
        }
    }

    // Changes enter button on keyboard to execute following code.
 /*   override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {

        // if enter button is pressed on keyboard:
        if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
            //check if guess is correct, and act based on that
            evaluateGuess()

            // check om spillet er slut, og start tabt/vundet skærmbilledet
            isGameOver()

            // returner true så keyboardet ikke forsvinder
            return true
        } else {
            return false
        }
    }*/

    // metode der sætter UI på skærmbilledet
    private fun initUI() {
        galgeimg = findViewById(R.id.galgeimg)
        galgeimg.setImageResource(R.drawable.galge)

        txtinput = findViewById(R.id.guessinput)
        txtinput.visibility = View.VISIBLE
        txtinput.requestFocus()
        txtinput.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    //check if guess is correct, and act based on that
                    evaluateGuess()

                    // check om spillet er slut, og start tabt/vundet skærmbilledet
                    isGameOver()
                    true
                }
                else -> false
            }
        }

        livestxt = findViewById(R.id.lives)
        livestxt.text = getString(R.string.livesinit)

        debugtxt = findViewById(R.id.test)
        debugtxt.text = getString(R.string.welcome)

        debugtxt2 = findViewById(R.id.test2)
        debugtxt2.text = galgelogik.synligtOrd
    }

    @SuppressLint("SetTextI18n")
    private fun evaluateGuess() {
        if (txtinput.length() == 1) {
            galgelogik.gætBogstav(txtinput.text.toString().decapitalize(Locale.getDefault()))

            if (galgelogik.erSidsteBogstavKorrekt()) { // correct guess
                debugtxt2.text = galgelogik.synligtOrd
                Toast.makeText(this, getString(R.string.trueguess), Toast.LENGTH_LONG).show()
            } else { // not correct guesss
                livestxt.text =
                    getString(R.string.youhave) + (6 - galgelogik.antalForkerteBogstaver) + getString(
                        R.string.livesleft
                    )
                Toast.makeText(this, getString(R.string.falseguess), Toast.LENGTH_LONG).show()

                // switch case that changes the image depending on lives left
                when (galgelogik.antalForkerteBogstaver) {
                    1 -> galgeimg.setImageResource(R.drawable.forkert1)
                    2 -> galgeimg.setImageResource(R.drawable.forkert2)
                    3 -> galgeimg.setImageResource(R.drawable.forkert3)
                    4 -> galgeimg.setImageResource(R.drawable.forkert4)
                    5 -> galgeimg.setImageResource(R.drawable.forkert5)
                    else -> galgeimg.setImageResource(R.drawable.forkert6)
                }
            }

            //kunne forbedres så det ikke var den rene toString(), så ingen brackets
            debugtxt.text = getString(R.string.usedletters) + galgelogik.brugteBogstaver.toString()
            txtinput.text = null

        }
    }

    private fun isGameOver() {
        if (galgelogik.erSpilletSlut()) {
            if (galgelogik.erSpilletVundet()) {
                val i = Intent(this, WonActivity::class.java)
                i.putExtra("tries", galgelogik.brugteBogstaver.size)
                startActivity(i)
            } else { //spillet er tabt
                val i = Intent(this, LostActivity::class.java)
                i.putExtra("word", galgelogik.ordet)
                startActivity(i)
            }

            // genstart spillet, så det er klart når man returnerer fra tabt/vundet skærmbilledet og vil spille igen
            // delay tilføjet for at gøre animationen for intentskiftet mere flydende
            handler.postDelayed(1000) {
                recreate()
            }

        }
    }


    // https://stackoverflow.com/questions/7057845/save-arraylist-to-sharedpreferences
    // gem listen af ord som et json objekt i preferencemanager
    private fun saveArrayList(list: ArrayList<String?>?, key: String?) {
        val prefs: SharedPreferences = getDefaultSharedPreferences(this)
        val editor: SharedPreferences.Editor = prefs.edit()
        val gson = Gson()
        val json: String = gson.toJson(list)
        editor.putString(key, json)
        editor.apply()
    }

    // https://stackoverflow.com/questions/7057845/save-arraylist-to-sharedpreferences
    // hent listen af data fra preferencemanager som et json objekt, og pak det ud til en arraylist
    private fun getArrayList(key: String?): ArrayList<String?>? {
        val prefs: SharedPreferences = getDefaultSharedPreferences(this)
        val gson = Gson()
        val json: String? = prefs.getString(key, null)
        val type: Type = object : TypeToken<ArrayList<String?>?>() {}.getType()
        return gson.fromJson(json, type)
    }
}



