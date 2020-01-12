package com.lpdim.spacedim.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.lpdim.spacedim.R
import com.lpdim.spacedim.game.GameActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    fun launchGame(view: View) {
        val intent = Intent(this, GameActivity::class.java).apply {
            //TODO: put extra with roomname and userid
            //putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }
}
