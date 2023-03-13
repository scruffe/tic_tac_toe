package com.example.chasexo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


class MainActivity : AppCompatActivity() {
    private lateinit var mAdView : AdView
    private var gameMode: Int = 1
    private lateinit var view1: ImageView
    private lateinit var view2: ImageView
    private lateinit var view3: ImageView
    private lateinit var view4: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        view1 = findViewById(R.id.imageView)
        view2 = findViewById(R.id.imageView10)
        view3 = findViewById(R.id.imageView12)
        view4 = findViewById(R.id.imageView13)

        MobileAds.initialize(this) {}

        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    fun playButton(view: View) {
        val myIntent = Intent(this, Activity2::class.java)
        myIntent.putExtra("gameMode", gameMode)
        startActivity(myIntent)
    }

    fun playTimer(view: View) {
        view1.visibility = ImageView.INVISIBLE
        view2.visibility = ImageView.INVISIBLE
        view3.visibility = ImageView.VISIBLE
        view4.visibility = ImageView.VISIBLE
        gameMode = 1
    }
    fun playSurvival(view: View) {
        view1.visibility = ImageView.VISIBLE
        view2.visibility = ImageView.VISIBLE
        view3.visibility = ImageView.INVISIBLE
        view4.visibility = ImageView.INVISIBLE
        gameMode = 2
    }
}