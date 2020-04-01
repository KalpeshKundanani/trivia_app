package com.app_scrip_assignment.trivia.splash_screen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app_scrip_assignment.trivia.main.MainActivity

/**
 * Splash screen that displays app's logo at the
 * start of the app and then opens the
 * main activity.
 *
 * @author: Kalpesh Kundanani
 * Date: 31st March 2020
 */
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // launching main activity
        startActivity(Intent(this, MainActivity::class.java))
        // To remove splash screen activity from activity stack.
        finish()
    }
}