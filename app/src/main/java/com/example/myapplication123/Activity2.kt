package com.example.myapplication123

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import java.util.*

class Activity2 : AppCompatActivity() {

    var gameActive: Boolean? = true

    // 0 = O
    // 3 = X
    var activePlayer: Int = 0
    var activePlayerStr: String = "O"
    // 0 = O
    // 3 = X
    // 2 = null
    val gameState = IntArray(9) { 2 }

    var counter: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)
    }



    fun playerTap(view: View){
        val status = findViewById<TextView>(R.id.status)
        val imgTag = Integer.parseInt(view.tag.toString())
        val imgView = findViewById<ImageView>(view.id)
        var gameOver: Boolean? = null

        // restart
        if (gameActive == false) {
            restartGame(view);
            return
        }

        // check if the spot is already played
        if (gameState[imgTag] != 2){
            return
        }

        // mark this position
        gameState[imgTag] = activePlayer

        fun checkWin(winInt: Int, playerStr: String):Boolean {
            if(threeInARow(winInt, activePlayer)) {
                status.text =  "Game Over $playerStr wins! "
                gameActive = false
                return true
            }
            return false
        }

        var drawable = 0
        var lastPlayerStr = activePlayerStr
        var lastPlayerWinInt = activePlayer * 3



        val rand = Random()
        if (activePlayer == 0) {
            // drawable = R.drawable.circle1
            val circles = intArrayOf(R.drawable.circle1, R.drawable.circle2)
            drawable = circles[rand.nextInt(circles.size)]
            activePlayer = 3
            activePlayerStr = "X"

        } else {
            val crosses = intArrayOf(R.drawable.x1, R.drawable.x2)
            drawable = crosses[rand.nextInt(crosses.size)]
            activePlayer = 0
            activePlayerStr = "O"
        }
        status.text =  "It's $activePlayerStr's turn! "
        imgView.setImageResource(drawable)

        if (checkWin((lastPlayerWinInt), lastPlayerStr)){
            return
        }

        //check if game is drawn
        counter++
        if (counter >= 9){
            status.text = "Match Draw"
            return
        }
    }

    fun restartGame(view: View){
        gameActive = true
        activePlayer = 0
        activePlayerStr = "O"
        counter = 0
        for (i in 0 until 9) {
            gameState[i] = 2
        }
        findViewById<ImageView>(R.id.imageView0).setImageResource(0);
        findViewById<ImageView>(R.id.imageView1).setImageResource(0);
        findViewById<ImageView>(R.id.imageView2).setImageResource(0);
        findViewById<ImageView>(R.id.imageView3).setImageResource(0);
        findViewById<ImageView>(R.id.imageView4).setImageResource(0);
        findViewById<ImageView>(R.id.imageView5).setImageResource(0);
        findViewById<ImageView>(R.id.imageView6).setImageResource(0);
        findViewById<ImageView>(R.id.imageView7).setImageResource(0);
        findViewById<ImageView>(R.id.imageView8).setImageResource(0);

        findViewById<ImageView>(R.id.top_line).visibility = View.INVISIBLE
        findViewById<ImageView>(R.id.center_line).visibility = View.INVISIBLE
        findViewById<ImageView>(R.id.bottom_line).visibility = View.INVISIBLE
        findViewById<ImageView>(R.id.left_line).visibility = View.INVISIBLE
        findViewById<ImageView>(R.id.right_line).visibility = View.INVISIBLE

        val status = findViewById<TextView>(R.id.status)
        status.setText("X's Turn - Tap to play");
    }

    private fun threeInARow(i: Int, activePlayer: Int): Boolean{
        var color = if (activePlayer == 3){
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
                val line = findViewById<ImageView>(R.id.top_line)
                line.visibility = View.VISIBLE
                line.setColorFilter(color)

                return true
            }
            row2 == i -> {
                val line = findViewById<ImageView>(R.id.center_line)
                line.visibility = View.VISIBLE
                line.setColorFilter(color)
                line.rotation = 0.0F

                return true
            }
            row3 == i -> {
                val line = findViewById<ImageView>(R.id.bottom_line)
                line.visibility = View.VISIBLE
                line.setColorFilter(color)

                return true
            }
            col1 == i -> {
                val line = findViewById<ImageView>(R.id.left_line)
                line.visibility = View.VISIBLE
                line.setColorFilter(color)


                return true
            }
            col2 == i -> {
                val line = findViewById<ImageView>(R.id.center_line)
                line.visibility = View.VISIBLE
                line.setColorFilter(color)
                line.rotation = 90.0F
                return true
            }
            col3 == i -> {
                val line = findViewById<ImageView>(R.id.right_line)
                line.visibility = View.VISIBLE
                line.setColorFilter(color)

                return true
            }
            dia1 == i -> {
                val line = findViewById<ImageView>(R.id.center_line)
                line.visibility = View.VISIBLE
                line.setColorFilter(color)
                line.rotation = 45.0F
                return true
            }
            dia2 == i -> {
                val line = findViewById<ImageView>(R.id.center_line)
                line.visibility = View.VISIBLE
                line.setColorFilter(color)
                line.rotation = 135.0F
                return true
            }
            else -> return false
        }

    }
}