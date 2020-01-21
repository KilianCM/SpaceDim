package com.lpdim.spacedim.score

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lpdim.spacedim.R
import com.lpdim.spacedim.api.API
import com.lpdim.spacedim.utils.MoshiService.userAdapter
import com.lpdim.spacedim.game.WebSocketManager.okHttpClient
import com.lpdim.spacedim.game.waiting.PlayerAdapter
import com.lpdim.spacedim.utils.MoshiService.userListAdapter
import kotlinx.android.synthetic.main.activity_score.*
import okhttp3.Call
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import java.io.IOException

class ScoreActivity : AppCompatActivity() {

    private var scoreAdapter: ScoreAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        Timber.d("OnCreate")
        scoreAdapter = ScoreAdapter()
        findViewById<RecyclerView>(R.id.player_list_score).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = scoreAdapter
        }

        getUserScoreList("top")
    }

    private fun getUserScoreList(param: String){
        Timber.d(API.BASE_URL_HTTP + API.GET_USER_LIST + param)
        val request = Request.Builder()
            .url(API.BASE_URL_HTTP + API.GET_USER_LIST + param)
            .build()
        okHttpClient.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    Timber.d(it.message)
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    response.body?.let {
                        Timber.d(it.toString())
                        userListAdapter.fromJson(it.source())?.let {
                            runOnUiThread {
                                scoreAdapter?.players = it
                                loadingBarScore.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        })
    }
}
