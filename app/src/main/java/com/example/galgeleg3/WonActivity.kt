package com.example.galgeleg3

import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView


class WonActivity : AppCompatActivity() {
    private lateinit var livestxt: TextView
    private lateinit var debugtxt: TextView
    private lateinit var debugtxt2: TextView
    private lateinit var galgeimg: ImageView
    private lateinit var inputtext: TextView
    private lateinit var lottieconfetti: LottieAnimationView
    private lateinit var mp : MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        mp = MediaPlayer.create(this, R.raw.fanfare);

        inputtext = findViewById(R.id.guessinput)
        inputtext.visibility = View.INVISIBLE

        galgeimg = findViewById(R.id.galgeimg)
        galgeimg.setImageResource(R.drawable.won)

        livestxt = findViewById(R.id.lives)
        livestxt.text = getString(R.string.pressback)

        debugtxt = findViewById(R.id.test)
        debugtxt.text = getString(R.string.youused)+ intent.getIntExtra("tries", 0) + getString(R.string.attempts)

        debugtxt2 = findViewById(R.id.test2)
        debugtxt2.text = getString(R.string.congrats)

        mp.start() // starts our music player to celebrate the player

        lottieconfetti = findViewById(R.id.animationView)
        lottieconfetti.playAnimation() // plays lottieanimation
    }

    override fun onDestroy() {
        super.onDestroy()

        if (mp.isPlaying()) { // frees the resource allocated by the music player
            mp.stop()
            mp.release()
        }

        lottieconfetti.cancelAnimation() // stops the lottieanimations from playing when activity is destroyed
    }
}