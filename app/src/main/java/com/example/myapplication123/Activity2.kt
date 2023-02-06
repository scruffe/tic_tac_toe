package com.example.myapplication123

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
    private val timer = object: CountDownTimer(30000, 1000) {

        // Callback function, fired on regular interval
        override fun onTick(millisUntilFinished: Long) {
            findViewById<TextView>(R.id.timer).text = "" + millisUntilFinished / 1000
        }

        // Callback function, fired
        // when the time is up
        override fun onFinish() {
            findViewById<TextView>(R.id.timer).text = "done!"
            pause = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)
        findViewById<TextView>(R.id.status).text = java.lang.String.format(resources.getString(R.string.first_turn_text))


    }

    fun missClick(view: View, status: TextView){
        lives--
        if (score > highscore){
            highscore = score
        }
        var x = (difficulty-lives)/2
        if (x<0){x=1}
        score -= (score/10) * x
        if (score<0){
            score = 0
        }
        if (lives < 0){
            return
        }
        updateScore(status)

        if (counter == 0){
            counter = 1
        }
    }

    private fun updateScore(status: TextView){
        if (score > highscore){
            highscore = score
        }
        status.text = java.lang.String.format("hs $highscore, Lives $lives, score $score")

    }

    fun playerTap(view: View){
        val status = findViewById<TextView>(R.id.status)
        val imgTag = Integer.parseInt(view.tag.toString())
        val imgViewOld = findViewById<ImageView>((view.id))
        val gs_id = gameState[imgTag]

        if (counter==0){// time count down for 30 seconds,
            // with 1 second as countDown interval

            timer.start()
        }

        if (pause){
            timer.cancel()
            return
        }
        if (lives<0){return}
        stake = (score/10) * (8-lives)
        updateScore(status)
        if (score>80){checkpoint=6}
        else if(score>50&&checkpoint<5){checkpoint=5}
        else if(score>30&&checkpoint<3){checkpoint=3}
        else if(score>20&&checkpoint<2){checkpoint=2}
        else if(score>10&&checkpoint<1){checkpoint=1}


        if (gamemode==2){
            if (gs_id == 2){
                // hit an X
                missClick(view, status)
                return

            }
            if (gs_id == 0 && counter!=0){
                // missed target
                missClick(view, status)
                return
            }
            var hit: Boolean = false
            if (gs_id == 1||gs_id == 3||gs_id == 4){
                hit = true
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
            val black = ContextCompat.getColor(applicationContext, R.color.black)
            val red = ContextCompat.getColor(applicationContext, R.color.x_color)
            val view_ids = intArrayOf(R.id.imageView0,R.id.imageView1,R.id.imageView2
                ,R.id.imageView3,R.id.imageView4,R.id.imageView5,R.id.imageView6
                ,R.id.imageView7,R.id.imageView8)

            //circle
            val imgs = intArrayOf(R.drawable.circle1, R.drawable.circle2)
            val circle = imgs[Random().nextInt(imgs.size)]
            var r1 = Random().nextInt(view_ids.size)

            if (gameState[r1]==2){
                r1 = Random().nextInt(view_ids.size)
                if (gameState[r1]==2){
                    r1 = Random().nextInt(view_ids.size)
                }
            }
            val color1 = if (Random().nextInt(3)==1 && checkpoint>3){red} else { blue}
            drawImg(r1, view_ids, circle, color1,1)
            //star
            val starR = Random().nextInt(view_ids.size)
            if (gameState[starR]==0 && checkpoint>5){
                drawImg(starR, view_ids, circle, black,1)
            }

            //cross
            val imgs2 = intArrayOf(R.drawable.x1, R.drawable.x2, R.drawable.x)
            val cross = imgs2[Random().nextInt(imgs2.size)]
            var r2 = Random().nextInt(view_ids.size)
            if (r2 == r1){
                if (r2==8) {r2--} else {r2++}
            }

            val color2 = if (Random().nextInt(5)==1 && score>10){blue} else {red}
            var img = 0
            if (Random().nextInt(3)!=1 && counter!=1){
                if (gameState[r2] != 2){
                    img = cross
                }
                drawImg(r2, view_ids, img, color2, 2)
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

    private fun drawImg(
        rand: Int,
        view_ids: IntArray,
        img: Int,
        black: Int,
        id: Int
    ) {
        gameState[rand] = id
        findViewById<ImageView>(view_ids[rand]).setImageResource(img)
        findViewById<ImageView>(view_ids[rand]).rotation = (Random().nextInt(4) * 45).toFloat()
        findViewById<ImageView>(view_ids[rand]).setColorFilter(black)
    }

    fun restartGame(view: View){
        val status = findViewById<TextView>(R.id.status)
        score++
        updateScore(status)

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

        findViewById<ImageView>(R.id.top_line).visibility = View.INVISIBLE
        findViewById<ImageView>(R.id.center_line).visibility = View.INVISIBLE
        findViewById<ImageView>(R.id.bottom_line).visibility = View.INVISIBLE
        findViewById<ImageView>(R.id.left_line).visibility = View.INVISIBLE
        findViewById<ImageView>(R.id.right_line).visibility = View.INVISIBLE

        status.text = resources.getString(R.string.first_turn_text)
    }
}
