package com.example.myapplication123

import android.media.MediaPlayer
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import java.util.*

class Survival : AppCompatActivity() {
    private var gameOver: Boolean = false
    private var activePlayer: Int = 1
    private val gameState = IntArray(9) {0}
    private var counter: Int = 0
    private var highscore: Int = 0
    private var score: Int = 0
    private var checkpoint: Int = 0
    private var pause: Boolean = false
    private var soundPool: SoundPool? = null
    private val soundId = 1
    private var muteSFX: Boolean = false
    private var BgMusic: MediaPlayer? = null
    private var musicPlaying: Boolean = false
    private var timer = object: CountDownTimer(30000, 1000) {

        // Callback function, fired on regular interval
        override fun onTick(millisUntilFinished: Long) {
            findViewById<TextView>(R.id.timer).text = "TIMER " + millisUntilFinished / 1000
        }

        // Callback function, fired
        // when the time is up
        override fun onFinish() {

            findViewById<TextView>(R.id.timer).text = "TIMER 00"
            pause = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)
        findViewById<TextView>(R.id.timer).text = "TIMER 0"
        findViewById<TextView>(R.id.status).text = java.lang.String.format(resources.getString(R.string.first_turn_text))


    }

    private fun updateScore(status: TextView){
        findViewById<TextView>(R.id.score).text = java.lang.String.format("$score")
        findViewById<TextView>(R.id.highscore).text = java.lang.String.format("$highscore")
    }

    fun playerTap(view: View){

        val status = findViewById<TextView>(R.id.status)
        val imgTag = Integer.parseInt(view.tag.toString())
        val imgViewOld = findViewById<ImageView>((view.id))
        val gs_id = gameState[imgTag]
        val pops = intArrayOf(R.raw._pop,R.raw.pop1//,R.raw.pop2
            ,R.raw.pop3
            ,R.raw.pop4//,R.raw.pop5
            ,R.raw.pop6,R.raw.pop7,R.raw.pop8//,R.raw.pop9
            ,R.raw.pop10,R.raw.pop11//,R.raw.pop12
            ,R.raw.pop13,R.raw.pop14)

        if (counter==0){// time count down for 30 seconds,
            // with 1 second as countDown interval
            timer.start()
            status.text = java.lang.String.format("")
        }
//
        if(pause){playSound(R.raw.lose2)
            if (score > highscore){
                highscore = score
            }
            return}

        updateScore(status)
        if (gs_id == 0 && counter!=0){
            playSound(R.raw.mis)
            score--
            if (score < 0){score=0}
            return
        }
        else if (gs_id == 1){
            score++
            val pop = pops[Random().nextInt(pops.size)]
            playSound(pop)
        }
        else if (gs_id == 2){
            score += 5
            playSound(R.raw.lvl_up2)
        }

        updateScore(status)
        // clear this position
        imgViewOld.setImageResource(0)
        gameState[imgTag] = 0

        counter++

        val blue = ContextCompat.getColor(applicationContext, R.color.o_color)
        val black = ContextCompat.getColor(applicationContext, R.color.black)
        val red = ContextCompat.getColor(applicationContext, R.color.x_color)
        val view_ids = intArrayOf(R.id.imageView0,R.id.imageView1,R.id.imageView2
            ,R.id.imageView3,R.id.imageView4,R.id.imageView5,R.id.imageView6
            ,R.id.imageView7,R.id.imageView8)

        //1
        val imgs = intArrayOf(R.drawable.circle1, R.drawable.circle2)
        val circle = imgs[Random().nextInt(imgs.size)]
        var r = Random().nextInt(view_ids.size)
        drawImg(r, view_ids, circle, blue,1)

        // 5
        if (counter.mod(17)==0){
            r = Random().nextInt(view_ids.size)
            //drawImg(r, view_ids, circle, red,2)
            val timer2 = object: CountDownTimer(600, 150) {

                // Callback function, fired on regular interval
                override fun onTick(millisUntilFinished: Long) {
                    findViewById<ImageView>(view_ids[r]).rotation = (Random().nextInt(4) * 45).toFloat()
                    if (millisUntilFinished<200){
                        findViewById<ImageView>(view_ids[r]).setColorFilter(black)
                    }

                }
                // Callback function, fired
                // when the time is up
                override fun onFinish() {
                    gameState[r] = 0
                    findViewById<ImageView>(view_ids[r]).setImageResource(0)
                }
            }
            gameState[r] = 2
            findViewById<ImageView>(view_ids[r]).setImageResource(circle)
            findViewById<ImageView>(view_ids[r]).rotation = (Random().nextInt(4) * 45).toFloat()
            findViewById<ImageView>(view_ids[r]).setColorFilter(red)
            timer2.start()
        }

        //1

        if (gameState.sum() < 3 ){
            r = Random().nextInt(view_ids.size)
            drawImg(r, view_ids, circle, blue,1)

        }
    }

    private fun playSound(file: Int) {
        if (!muteSFX){
            val mSound = MediaPlayer.create(this, file)
            mSound.start()
            mSound.setOnCompletionListener(MediaPlayer.OnCompletionListener { mp -> mp.release() })
        }
    }
    fun muteSoundEffects(view: View) {
        var img = 0
        if (muteSFX){
            img = R.drawable.unmute
        } else{img = R.drawable.mute}
        findViewById<ImageView>(R.id.mute_button).setBackgroundResource(img)
        muteSFX = !muteSFX
    }

    private fun drawImg(
        r: Int,
        view_ids: IntArray,
        img: Int,
        c: Int,
        id: Int
    ) {
        val timer2 = object: CountDownTimer(1000, 200) {

            // Callback function, fired on regular interval
            override fun onTick(millisUntilFinished: Long) {
                findViewById<ImageView>(view_ids[r]).rotation = (Random().nextInt(4) * 45).toFloat()
            }
            // Callback function, fired
            // when the time is up
            override fun onFinish() {
//                /gameState[r] = 0
//                findViewById<ImageView>(view_ids[r]).setImageResource(0)
            }
        }
        timer2.start()
        gameState[r] = id
        findViewById<ImageView>(view_ids[r]).setImageResource(img)
        findViewById<ImageView>(view_ids[r]).rotation = (Random().nextInt(4) * 45).toFloat()
        findViewById<ImageView>(view_ids[r]).setColorFilter(c)
    }

    fun restartGame(view: View){
        timer.cancel()
        pause = false
        val status = findViewById<TextView>(R.id.status)
        status.text = java.lang.String.format("Ready?")
        playSound(R.raw.restart2)
        score++
        if (score > highscore){
            highscore = score
        }
        updateScore(status)
        gameOver = false
        activePlayer = 1
        counter = 0
        score = 0
        for (i in 0 until 9) {
            gameState[i] = 0
        }
        findViewById<ImageView>(R.id.imageView0).setImageResource(0)
        findViewById<ImageView>(R.id.imageView1).setImageResource(0)
        findViewById<ImageView>(R.id.imageView2).setImageResource(0)
        findViewById<ImageView>(R.id.imageView3).setImageResource(0)
        findViewById<ImageView>(R.id.imageView4).setImageResource(0)
        findViewById<ImageView>(R.id.imageView5).setImageResource(0)
        findViewById<ImageView>(R.id.imageView6).setImageResource(0)
        findViewById<ImageView>(R.id.imageView7).setImageResource(0)
        findViewById<ImageView>(R.id.imageView8).setImageResource(0)


        status.text = resources.getString(R.string.first_turn_text)
    }


}