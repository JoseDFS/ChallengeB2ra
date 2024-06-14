package com.example.challengeb2ra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.challengeb2ra.ui.AppNavHost
import com.example.challengeb2ra.ui.theme.ChallengeB2RATheme

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChallengeB2RATheme {
                AppNavHost()
            }
        }
    }
}