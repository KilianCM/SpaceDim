package com.lpdim.spacedim.score

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.lpdim.spacedim.R
import com.lpdim.spacedim.api.API
import com.lpdim.spacedim.game.MoshiService
import com.lpdim.spacedim.game.MoshiService.userAdapter
import com.lpdim.spacedim.game.WebSocketLiveData.Companion.client
import com.lpdim.spacedim.game.model.User
import kotlinx.android.synthetic.main.activity_score.*
import okhttp3.Call
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.security.auth.callback.Callback
import kotlin.math.absoluteValue

class ScoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)
        val userId = intent.extras?.getInt("userId")
        userId?.let {
            getUserScore(userId)
        }
    }

    private fun getUserScore(userId: Int){
        val request = Request.Builder()
            .url(API.BASE_URL_HTTP + API.GET_USER_BY_ID + userId.toString())
            .build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(
                        this@ScoreActivity,
                        getString(R.string.user_not_exist),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use{
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    response.body?.let {
                        val user = userAdapter.fromJson(it.source())
                        val userScore = user?.score
                        userScore?.let {
                            runOnUiThread {
                                tvScoreValue.text = userScore.toString()
                            }
                        }
                    }

                }
            }
        })

    }
}
