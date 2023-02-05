package com.example.myapplication123

import android.os.Bundle
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
    private var lives: Int = 3
    private var gamemode: Int =1 //1 = survival time attack


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)
        findViewById<TextView>(R.id.status).text = java.lang.String.format(resources.getString(R.string.first_turn_text))
    }

    fun playerTap(view: View){
        val status = findViewById<TextView>(R.id.status)
        val imgTag = Integer.parseInt(view.tag.toString())
        val imgViewOld = findViewById<ImageView>((view.id))

        if (gamemode==1){
            if (gameState[imgTag] == 0 && counter!=0){
                lives--
                if (score > highscore){
                    highscore = score
                }
                score -= 3
                if (score<0){
                    score = 0
                }

                if (lives < 0){
                    restartGame(view)
                    status.text = java.lang.String.format(resources.getString(R.string.game_over))
//                counter = 1
                    return
                }

                status.text = java.lang.String.format("hs $highscore, Lives $lives, score $score")
                counter = 1
                return
            }
            // clear this position
            imgViewOld.setImageResource(0)

            counter++
            if (counter == 60){
                // Game over
                status.text = java.lang.String.format(resources.getString(R.string.game_over))
                restartGame(view)
                return
            }
            score++
            status.text = java.lang.String.format("hs $highscore, Lives $lives, score $score")


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
            val r = Random().nextInt(view_ids.size)
            findViewById<ImageView>(view_ids[r]).setImageResource(drawable)

            // mark new position
            gameState[r] = 1
        }



//        fun checkWin(winInt: Int):Boolean {
//            if(threeInARow(winInt, activePlayer)) {
//                status.text = resources.getString(R.string.game_over)
//                gameOver = true
//                return true
//            }
//            return false
//        }



//        } else {
//            val crosses = intArrayOf(R.drawable.x1, R.drawable.x2)
//            drawable = crosses[rand.nextInt(crosses.size)]
//            activePlayer = 0
//        }

//        imgView.setImageResource(drawable)
//
//        if (checkWin((lastPlayerWinInt))){
//            return
//        }



    }
    private fun checkHighscore(c: Int, highscore: Int): Int {
        var x = highscore

        return x
    }

    fun restartGame(view: View){
        lives = 3
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

        findViewById<TextView>(R.id.status).text = resources.getString(R.string.first_turn_text)
    }

    private fun drawLine(id: Int, color: Int,degrees: Float){
        val line = findViewById<ImageView>(id)
        line.visibility = View.VISIBLE
        line.setColorFilter(color)
        line.rotation = degrees
    }

    private fun threeInARow(i: Int, activePlayer: Int): Boolean{
        val color = if (activePlayer == 3){
            ContextCompat.getColor(applicationContext, R.color.o_color)
        } else{
            ContextCompat.getColor(applicationContext, R.color.x_color)
        }
        val row1 = gameState[0]+gameState[1]+gameState[2]
        val row2 = gameState[3]+gameState[4]+gameState[5]
        val row3 = gameState[6]+gameState[7]+gameState[8]

        val col1 = gameState[0]+gameState[3]+gameState[6]
        val col2 = gameState[1]+gameState[4]+gameState[7]
        val col3 = gameState[2]+gameState[5]+gameState[8]

        val dia1 = gameState[0]+gameState[4]+gameState[8]
        val dia2 = gameState[2]+gameState[4]+gameState[6]

        when {
            row1 == i -> {
                drawLine(R.id.top_line, color, 0.0F)
                return true
            }
            row2 == i -> {
                drawLine(R.id.center_line, color, 0.0F)
                return true
            }
            row3 == i -> {
                drawLine(R.id.bottom_line, color, 0.0F)
                return true
            }
            col1 == i -> {
                drawLine(R.id.left_line, color, 90.0F)
                return true
            }
            col2 == i -> {
                drawLine(R.id.center_line, color, 90.0F)
                return true
            }
            col3 == i -> {
                drawLine(R.id.right_line, color, 90.0F)
                return true
            }
            dia1 == i -> {
                drawLine(R.id.center_line, color, 45.0F)
                return true
            }
            dia2 == i -> {
                drawLine(R.id.center_line, color, 135.0F)
                return true
            }
            else -> return false
        }

    }
}