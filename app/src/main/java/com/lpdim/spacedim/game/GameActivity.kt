package com.lpdim.spacedim.game

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lpdim.spacedim.R

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
    }

    override fun onDestroy() {
        super.onDestroy()
        WebSocketManager.closeConnection()
    }
}
