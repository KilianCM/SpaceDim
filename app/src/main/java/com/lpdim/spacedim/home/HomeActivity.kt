package com.lpdim.spacedim.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.lpdim.spacedim.R
import com.lpdim.spacedim.game.GameActivity
import com.lpdim.spacedim.game.MoshiService
import com.lpdim.spacedim.game.MoshiService.userAdapter
import com.lpdim.spacedim.game.WebSocketLiveData
import com.lpdim.spacedim.game.model.User
import kotlinx.android.synthetic.main.activity_home.*
import okhttp3.*
import timber.log.Timber
import java.io.IOException
import java.lang.Exception

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        textBoxName.text
    }

    fun launchGame(view: View) {
        val intent = Intent(this, GameActivity::class.java).apply {
            //TODO: put extra with roomname and userid
            //putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }

    fun testIfUserExist(username: String): Int?{
        var userId: Int? = null
        val url = "http://vps769278.ovh.net/doc/api/user/find/$textBoxName"
        val request = Request.Builder()
            .url(url)
            .build()
        WebSocketLiveData.client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if(response.isSuccessful){
                        try {
                            response.body?.let {
                                val user = userAdapter.fromJson(it.source())
                                userId = user?.id
                            }
                        } catch (e: Exception) {
                            Timber.d("Failed to deserialize user")
                        }
                    }
                }
            }
        })

        return userId
    }
}
