package com.example.myapplication123

import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.media.SoundPool
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.*


class Activity2 : AppCompatActivity() {
    private var gameOver: Boolean = false
    private var activePlayer: Int = 1
    private val gameState = IntArray(9) {0}
    private var counter: Int = 0
    private var highscore: Int = 0
    private var score: Int = -1
    private var lives: Int = 5
    private var stake: Int = 5
    private var difficulty: Int = 5
    private var gamemode: Int = 2 //1 = survival time attack
    private var checkpoint: Int = 0
    private var pause: Boolean = false
    private var soundPool: SoundPool? = null
    private val soundId = 1
    private var muteSFX: Boolean = false
    private var BgMusic: MediaPlayer? = null
    private var musicPlaying: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)
        findViewById<TextView>(R.id.status).text = java.lang.String.format(resources.getString(R.string.first_turn_text))
        findViewById<TextView>(R.id.timer).text = "Lives 5"

    }

    fun missClick(view: View, status: TextView){
        lives--

        var x = (difficulty-lives)/2
        if (x<0){x=1}
        score -= (score/10) * x
        if (score<0){
            score = 0
        }
        if (lives < 0){
            playSound(R.raw.lose2)

            if (score > highscore){
                highscore = score
                updateScore(status)
            }
            return
        }
        updateScore(status)

        if (counter == 0){
            counter = 1
        }
    }

    private fun updateScore(status: TextView){
        var x: Int = lives
        if (x < 0) {x = 0}
        findViewById<TextView>(R.id.timer).text = java.lang.String.format("Lives $x")
        findViewById<TextView>(R.id.score).text = java.lang.String.format("$score")
        findViewById<TextView>(R.id.highscore).text = java.lang.String.format("$highscore")
    }

    fun playerTap(view: View){
        val status = findViewById<TextView>(R.id.status)
        val imgTag = Integer.parseInt(view.tag.toString())
        val imgViewOld = findViewById<ImageView>((view.id))
        val gs_id = gameState[imgTag]//2 5 9 12
        val pops = intArrayOf(R.raw._pop,R.raw.pop1//,R.raw.pop2
            ,R.raw.pop3
            ,R.raw.pop4//,R.raw.pop5
            ,R.raw.pop6,R.raw.pop7,R.raw.pop8//,R.raw.pop9
            ,R.raw.pop10,R.raw.pop11//,R.raw.pop12
            ,R.raw.pop13,R.raw.pop14)

        if (counter==0){
            status.text = java.lang.String.format("")
        }
        if (lives<0){
            playSound(R.raw.lose2)


            return}
        stake = (score/10) * (8-lives)
        updateScore(status)
        if (score>80&&checkpoint<6){
            checkpoint=6
            playSound(R.raw.lvl_up2)}
        else if(score>50&&checkpoint<5){
            checkpoint=5
            playSound(R.raw.lvl_up2)}
        else if(score>30&&checkpoint<3){
            checkpoint=3
            playSound(R.raw.lvl_up2)}
        else if(score>20&&checkpoint<2){
            checkpoint=2
            playSound(R.raw.lvl_up2)}
        else if(score>10&&checkpoint<1){
            checkpoint=1
            playSound(R.raw.lvl_up2)}


        if (gamemode==2){
            if (gs_id == 2){ // hit x
                playSound(R.raw.kruisje)
                missClick(view, status)
                return

            }
            else if (gs_id == 0 && counter!=0){
                // missed target
                playSound(R.raw.mis)
                missClick(view, status)
                return
            }
            else if (gs_id == 1||gs_id == 3||gs_id == 4){
                val pop = pops[Random().nextInt(pops.size)]
                playSound(pop)
            }else if (gs_id == 7){
                score += 10
                playSound(R.raw.lvl_up2)
            }else if (gs_id == 6){
                score += 5
                playSound(R.raw.lvl_up2)
            }
            if (counter == 0){
                score = checkpoint * 10
            }
            // clear this position
            imgViewOld.setImageResource(0)
            gameState[imgTag] = 0

            score++
            updateScore(status)
            counter++
//            if (counter == 300){
//                // end of game counter
//                restartGame(view)
//                return
//            }
            val blue = ContextCompat.getColor(applicationContext, R.color.o_color)
            val red = ContextCompat.getColor(applicationContext, R.color.x_color)
            val view_ids = intArrayOf(R.id.imageView0,R.id.imageView1,R.id.imageView2
                ,R.id.imageView3,R.id.imageView4,R.id.imageView5,R.id.imageView6
                ,R.id.imageView7,R.id.imageView8)

            //circle

            summonCircles()

            //cross
            val imgs2 = intArrayOf(R.drawable.x1, R.drawable.x2)
            val cross = imgs2[Random().nextInt(imgs2.size)]
            val r2 = Random().nextInt(view_ids.size)
            if (gameState[r2] ==0){
                val color2 = if (Random().nextInt(5)==1 && score>10){blue} else {red}
                var img = 0
                var id = 0
                if (Random().nextInt(3)!=1 && counter!=1){
                    if (gameState[r2] != 2){
                        img = cross
                        id = 2
                    }
                    drawImg(r2, view_ids, img, color2, id,1000)
                }
            }
        }

        ///////////////////////// SURVIVAL
        if (gamemode==1){
            if (gs_id == 0 && counter!=0){
                missClick(view, status)
                return
            }
            // clear this position
            imgViewOld.setImageResource(0)

            counter++
            if (counter == 10){

                restartGame(view)
                return
            }
            score++
            updateScore(status)

            gameState[imgTag] = 0

            var drawable = 0
            //val lastPlayerWinInt = activePlayer * 3

            if (activePlayer == 1) {
                val imgs = intArrayOf(R.drawable.circle1, R.drawable.circle2,R.drawable.x1, R.drawable.x2)
                drawable = imgs[Random().nextInt(imgs.size)]
                //activePlayer = 3
            }
            val view_ids = intArrayOf(R.id.imageView0,R.id.imageView1,R.id.imageView2
                ,R.id.imageView3,R.id.imageView4,R.id.imageView5,R.id.imageView6
                ,R.id.imageView7,R.id.imageView8)
            var r = Random().nextInt(view_ids.size)
            findViewById<ImageView>(view_ids[r]).setImageResource(drawable)

            // mark new position
            gameState[r] = 1
        }
    }

    private fun playSound(file: Int) {
        if (!muteSFX){
            val mSound = MediaPlayer.create(this, file)
            mSound.start()
            mSound.setOnCompletionListener(OnCompletionListener { mp -> mp.release() })
        }
    }

    private fun summonCircles(){
        val purple = ContextCompat.getColor(applicationContext, R.color.purple_700)
        val light_purple = ContextCompat.getColor(applicationContext, R.color.purple_500)
        val pink = ContextCompat.getColor(applicationContext, R.color.purple_200)
        val blue = ContextCompat.getColor(applicationContext, R.color.o_color)
        val black = ContextCompat.getColor(applicationContext, R.color.black)
        val red = ContextCompat.getColor(applicationContext, R.color.x_color)
        val view_ids = intArrayOf(R.id.imageView0,R.id.imageView1,R.id.imageView2
            ,R.id.imageView3,R.id.imageView4,R.id.imageView5,R.id.imageView6
            ,R.id.imageView7,R.id.imageView8)

        val imgs = intArrayOf(R.drawable.circle1, R.drawable.circle2)
        val circle = imgs[Random().nextInt(imgs.size)]

        var r = Random().nextInt(view_ids.size)
        if (gameState[r]!=0){
            r = Random().nextInt(view_ids.size)
            if (gameState[r]!=0){
                r = Random().nextInt(view_ids.size)
            }
        }
        val color1 = if (Random().nextInt(3)==1 && checkpoint>3){red} else { blue}
        drawImg(r, view_ids, circle, color1,1,1000)

        // 5
        if (counter.mod(17)==0){
            r = Random().nextInt(view_ids.size)
            val timer2 = object: CountDownTimer(600, 150) {
                // Callback function, fired on regular interval
                override fun onTick(millisUntilFinished: Long) {
                    findViewById<ImageView>(view_ids[r]).rotation = (Random().nextInt(4) * 45).toFloat()
                    if (millisUntilFinished<200){
                        gameState[r] = 1
                        findViewById<ImageView>(view_ids[r]).setColorFilter(light_purple)
                    }else if (millisUntilFinished<300){
                        gameState[r] = 6
                    }

                }
                // Callback function, fired
                // when the time is up
                override fun onFinish() {
                    gameState[r] = 0
                    findViewById<ImageView>(view_ids[r]).setImageResource(0)
                }
            }
            gameState[r] = 7
            findViewById<ImageView>(view_ids[r]).setImageResource(circle)
            findViewById<ImageView>(view_ids[r]).rotation = (Random().nextInt(4) * 45).toFloat()
            findViewById<ImageView>(view_ids[r]).setColorFilter(purple)
            timer2.start()
        }

        if (checkpoint<6){return}
        // multiple circles come
        r = Random().nextInt(view_ids.size)
        if (gameState[r]==0){
            drawImg(r, view_ids, circle, blue,1,1000)
        }
        //1
        r = Random().nextInt(view_ids.size)
        drawImg(r, view_ids, circle, blue,1,1000)


    }

    private fun drawImg(
        r: Int,
        view_ids: IntArray,
        img: Int,
        c: Int,
        id: Int,
        ms: Long
    ) {

        val timer2 = object: CountDownTimer(ms, 200) {

            // Callback function, fired on regular interval
            override fun onTick(millisUntilFinished: Long) {
                findViewById<ImageView>(view_ids[r]).rotation = (Random().nextInt(4) * 90).toFloat()
                if (millisUntilFinished<200){
                    findViewById<ImageView>(view_ids[r]).setColorFilter(ContextCompat.getColor(applicationContext, R.color.black))
                }
            }

            override fun onFinish() {

            }
        }
        timer2.start()
        gameState[r] = id
        findViewById<ImageView>(view_ids[r]).setImageResource(img)
        findViewById<ImageView>(view_ids[r]).rotation = (Random().nextInt(4) * 45).toFloat()
        findViewById<ImageView>(view_ids[r]).setColorFilter(c)
    }

    fun restartGame(view: View){
        val status = findViewById<TextView>(R.id.status)

        playSound(R.raw.restart2)
        score++
        if (score > highscore){
            highscore = score
        }
        updateScore(status)
        findViewById<TextView>(R.id.timer).text = "Lives 5"

        lives = difficulty
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

    fun muteSoundEffects(view: View) {
        var img = 0
        if (muteSFX){
            img = R.drawable.unmute
        } else{img = R.drawable.mute}
        findViewById<ImageView>(R.id.mute_button).setBackgroundResource(img)
        muteSFX = !muteSFX
    }
}
