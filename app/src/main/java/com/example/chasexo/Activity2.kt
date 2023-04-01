package com.example.chasexo


import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class Activity2 : AppCompatActivity() {
    //val sharedPref = applicationContext.getSharedPreferences("com.example.myapp.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE)
    private val gameState = IntArray(9) {0}
    private var counter: Int = 0
    private var highScoreCount: Int = 0
    private var scoreCount: Int = 0
    private var lives: Int = 0
    private var difficulty: Int = 5
    private var checkpoint: Int = 0
    private var muteSFX: Boolean = false
    private val views = intArrayOf(R.id.imageView0,R.id.imageView1,R.id.imageView2
        ,R.id.imageView3,R.id.imageView4,R.id.imageView5,R.id.imageView6
        ,R.id.imageView7,R.id.imageView8)
    private lateinit var status: TextView
    private lateinit var score: TextView
    private lateinit var highScore: TextView
    private lateinit var timer: TextView
    private lateinit var startTimerText: String
    private var gameMode : Int = 0
    private var pause: Boolean = false
    val lose = R.raw.lose2
    private val lvlUp = R.raw.lvl_up2
    private var countDownTimer = object: CountDownTimer(60000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            findViewById<TextView>(R.id.timer).text = java.lang.String.format(getString(R.string.gamemode_txt)+ " " + millisUntilFinished / 1000)
        }
        override fun onFinish() {
            findViewById<TextView>(R.id.timer).text = java.lang.String.format(getString(R.string.gamemode_txt) + " 0")
            pause = true
            gameOver()
        }
    }
    private var bubblePop = R.drawable.bubblepop
    private var heartPop = R.drawable.heartpop
    private val circle = R.drawable.bubble
    private val heart = R.drawable.heart
    private val pops = intArrayOf(R.raw._pop,R.raw.pop1//,R.raw.pop2
        , R.raw.pop3
        ,R.raw.pop4//,R.raw.pop5
        ,R.raw.pop6,R.raw.pop7,R.raw.pop8//,R.raw.pop9
        ,R.raw.pop10,R.raw.pop11//,R.raw.pop12
        ,R.raw.pop13,R.raw.pop14)
    private var imgTag = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)
        val sharedPref = super.getPreferences(Context.MODE_PRIVATE)
        val myIntent = intent
        status = findViewById(R.id.status)
        timer = findViewById(R.id.timer)
        score = findViewById(R.id.score)
        highScore = findViewById(R.id.highscore)
        gameMode = myIntent.getIntExtra("gameMode",2)
        status.text = resources.getString(R.string.first_turn_text)
        lives = resources.getInteger(R.integer.lives)
        findViewById<ImageView>(R.id.heart1).setImageResource(heart)
        findViewById<ImageView>(R.id.heart2).setImageResource(heart)

        startTimerText = java.lang.String.format(getString(R.string.gamemode_txt) + " " + getString(R.string.placeholder_time))
        highScoreCount = sharedPref.getInt("highScoreTimer", 0)
        if (gameMode == 2){
            highScoreCount = sharedPref.getInt("highScoreSurvival", 0)
            startTimerText = "" //java.lang.String.format(getString(R.string.lives_txt) + " $lives"
        }
        timer.text = startTimerText
        highScore.text = java.lang.String.format("$highScoreCount")
        score.text = java.lang.String.format("$scoreCount")
    }

    private fun missClick() {
        lives--

        var x = (difficulty-lives)/2
        if (x<0){x=1}
        scoreCount -= (scoreCount/10) * x
        if (scoreCount<0){
            scoreCount = 0
        }
        saveHighScore()
        updateScore()
        when (lives) {
            0 -> {
                findViewById<ImageView>(R.id.heart1).setImageResource(heartPop)
            }
            1 -> {
                findViewById<ImageView>(R.id.heart2).setImageResource(heartPop)
            }
            2 -> {
                findViewById<ImageView>(R.id.heart3).setImageResource(heartPop)
            }
            3 -> {
                findViewById<ImageView>(R.id.heart4).setImageResource(heartPop)
            }
        }
        if (lives < 0){
            gameOver()
            if (gameMode == 1){
                countDownTimer.cancel()
            }
            return
        }
        updateScore()

        if (counter == 0){
            counter = 1
        }
    }

    private fun saveHighScore(){
        if (scoreCount < highScoreCount) {
            return
        }
        val sharedPref = super.getPreferences(Context.MODE_PRIVATE) ?: return
        highScoreCount = scoreCount
        with (sharedPref.edit()) {
            if (gameMode==1){
                putInt("highScoreTimer", highScoreCount)
            } else if (gameMode==2){
                putInt("highScoreSurvival", highScoreCount)
            }

            apply()
        }


    }

    private fun updateScore(){
        score.text = java.lang.String.format("$scoreCount")
        saveHighScore()

        if (gameMode==1){
            return
        }
    }

    fun playerTap(view: View){
        imgTag = Integer.parseInt(view.tag.toString())
        val gameStateId = gameState[imgTag]

        if(gameMode == 1){
            timerGameMode(gameStateId, view)
            return
        }
        survivalGameMode(gameStateId, view)

    }

    private fun updateGame( popId: Int, view: View) {
        val imgViewOld = findViewById<ImageView>((view.id))
        updateScore()
        imgViewOld.setImageResource(popId)
        imgViewOld.rotation = (Random().nextInt(180) * 20).toFloat()
        gameState[imgTag] = 0
        counter++
    }

    private fun survivalGameMode(gameStateId: Int, view: View) {
        var popId = bubblePop
        if (lives < 0) {
            gameOver()
            return
        }
        updateScore()

        if (scoreCount.mod(10) == 0 && scoreCount / 10 > checkpoint) {
            checkpoint = scoreCount / 10
            if (checkpoint == 1){
                newImg(heart, 9)
            }
            if (checkpoint  == 3 || checkpoint  == 6){
                newImg(circle)
            }
        }
        if (counter == 0) {
            status.text = ""
            checkpoint = 0
        }
        when (gameStateId) {
            0 -> {
                if (counter != 0) {
                    // missed target
                    playSound(R.raw.mis)
                    missClick()
                    return
                }

            }
            1, 3, 4 -> {
                val pop = pops[Random().nextInt(pops.size)]
                playSound(pop)
            }
            2 -> { // hit x
                playSound(R.raw.kruisje)
                missClick()
                return

            }
            7 -> {
                scoreCount += 9
                playSound(lvlUp)
            }
            6 -> {
                scoreCount += 4
                playSound(lvlUp)
            }
            9 -> { //heart

                popId = heartPop
                updateGainHearts()
            }
        }
        scoreCount++
        updateGame(popId, view)

        if (counter.mod((40*(lives+1)))==0) {
            newImg(heart, 9)
            return
        }
        newImg(circle)
    }

    private fun updateGainHearts() {
        if (lives < 4){
            lives++
        }
        playSound(lvlUp)
        var heartId = 0
        when (lives) {
            1 -> {
                heartId = R.id.heart1
            }
            2 -> {
                heartId = R.id.heart2
            }
            3 -> {
                heartId = R.id.heart3
            }
            4 -> {
                heartId = R.id.heart4
            }
        }
        findViewById<ImageView>(heartId).setImageResource(heart)
    }

    private fun timerGameMode(gameStateId: Int, view: View) {
        if (lives < 0) {
            gameOver()
            countDownTimer.cancel()
            return
        }
        if (counter == 0) {
            startTimerGame()
            return
        }
        if (pause) {
            gameOver()
            countDownTimer.cancel()
            return
        }
        updateScore()

        var popId = bubblePop
        if (gameStateId == 0 && counter != 0) {
            playSound(R.raw.mis)
            missClick()
            return
        } else if (gameStateId == 1) {
            scoreCount++
            val pop = pops[Random().nextInt(pops.size)]
            playSound(pop)
        } else if (gameStateId == 9) {
            scoreCount += 5
            popId = R.drawable.heartpop
            updateGainHearts()
        }
        updateGame(popId, view)

        newImg(circle)

        if (counter.mod(40)!=0) {
            return
        }
        drawImg(randomInt(), heart, id = 9, ms=600, timer = true)
    }

    private fun gameOver() {
        playSound(R.raw.lose2)
        status.text = java.lang.String.format(getString(R.string.game_over))
        saveHighScore()
        if (gameMode == 1){
            countDownTimer.cancel()
        }
    }

    private fun startTimerGame() {
        counter++
        countDownTimer.start()
        status.text = java.lang.String.format("")
        newImg(circle)
        newImg(circle)
    }

    private fun playSound(file: Int) {
        if (muteSFX){
            return
        }
        val mSound = MediaPlayer.create(this, file)
        mSound.start()
        mSound.setOnCompletionListener { mp -> mp.release()
        }
    }

    private fun randomInt(): Int {
        return Random().nextInt(views.size)
    }


    private fun newImg(circle: Int, id: Int=1) {
        val r = randomInt()
        println(r.toString())
        println(imgTag.toString())
        if (gameState[r] != 0 || r == imgTag ){
            newImg(circle, id)
            return
        }
        drawImg(r, circle, id = id)
    }

    private fun drawImg(
        r: Int,
        img: Int,
        c: Int = 0,
        id: Int = 1,
        ms: Long = 1000,
        timer: Boolean = false
    ) {
        val view = findViewById<ImageView>(views[r])

        gameState[r] = id
        view.setImageResource(img)
        view.rotation = 0.0F
        if(c>0){
            view.rotation = 180.0F
            view.setColorFilter(c)
        }
        if (!timer) {
            return
        }
        val timer2 = object: CountDownTimer(ms, 200) {
            // Callback function, fired on regular interval
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                if (gameState[r]==0){
                    return
                }
                var pop = bubblePop
                if (gameState[r]==9){
                    pop = heartPop}
                view.setImageResource(0)
                gameState[r] = 0
            }
        }
        timer2.start()
    }

    fun restartGame(view: View){
        if (gameMode==1){
            countDownTimer.cancel()
            pause = false
        }
        findViewById<ImageView>(R.id.heart1).setImageResource(heart)
        findViewById<ImageView>(R.id.heart2).setImageResource(heart)
        findViewById<ImageView>(R.id.heart3).setImageResource(0)
        findViewById<ImageView>(R.id.heart4).setImageResource(0)

        playSound(R.raw.restart2)
        scoreCount++
        saveHighScore()

        updateScore()
        highScore.text = java.lang.String.format("$highScoreCount")
        timer.text = startTimerText

        lives = resources.getInteger(R.integer.lives)

        counter = 0
        scoreCount = 0
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
        val img = if (muteSFX){
            R.drawable.unmute
        } else{
            R.drawable.mute
        }
        findViewById<ImageView>(R.id.mute_button).setBackgroundResource(img)
        muteSFX = !muteSFX
    }
}
