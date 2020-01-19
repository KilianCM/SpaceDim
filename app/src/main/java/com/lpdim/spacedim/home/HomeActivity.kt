package com.lpdim.spacedim.home

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.lpdim.spacedim.R
import com.lpdim.spacedim.api.API
import com.lpdim.spacedim.game.GameActivity
import com.lpdim.spacedim.game.MoshiService.userAdapter
import com.lpdim.spacedim.game.MoshiService.userPostAdapter
import com.lpdim.spacedim.game.WebSocketLiveData
import com.lpdim.spacedim.game.WebSocketLiveData.Companion.client
import com.lpdim.spacedim.game.model.UserPost
import com.lpdim.spacedim.score.ScoreActivity
import okhttp3.RequestBody.Companion.toRequestBody
import kotlinx.android.synthetic.main.activity_home.*
import okhttp3.*
import timber.log.Timber
import java.io.IOException
import java.lang.Exception

class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        btnRegister.setOnClickListener{
            displayRegisterDialog()
        }
        btnLaunchGame.setOnClickListener {
            checkUser(editTextName.text.toString())
        }

        btnScore.setOnClickListener {
            goToScorePage()
        }
    }

    private fun checkUser(username: String) {
        testIfUserExist(username, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if(response.isSuccessful){
                        try {
                            response.body?.let {
                                val user = userAdapter.fromJson(it.source())
                                val userId = user?.id
                                userId?.let {
                                    runOnUiThread {
                                        displayRoomSelectDialog(userId)
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Timber.d("Failed to deserialize user")
                            runOnUiThread {
                                Toast.makeText(this@HomeActivity, getString(R.string.user_not_exist), Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        })
    }

    private fun displayRoomSelectDialog(userId: Int) {
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.room_selection_dialog, null)
        builder.setView(view)
        builder.setTitle(getString(R.string.room_selection_title))
            .setPositiveButton(R.string.ok) { dialog, id ->
                val roomName = view.findViewById<TextView>(R.id.editTextRoomName).text
                launchGame(roomName.toString(), userId)
            }
            .setNegativeButton(R.string.cancel) { dialog, id ->
                dialog.cancel()
            }
        builder.create().show()
    }

    private fun displayRegisterDialog() {
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.register_dialog, null)
        builder.setView(view)
        builder.setTitle(getString(R.string.register_title))
            .setPositiveButton(R.string.ok) { dialog, id ->
                val username = view.findViewById<TextView>(R.id.editTextRegister).text
                registerUser(username.toString())
            }
            .setNegativeButton(R.string.cancel) { dialog, id ->
                dialog.cancel()
            }
        builder.create().show()
    }


    /**
     * Launch the GameActivity passing the userid and the room name as parameters
     */
    fun launchGame(roomName: String, userId: Int) {
        val intent = Intent(this, GameActivity::class.java).apply {
            putExtra("roomName", roomName)
            putExtra("userId", userId)
        }
        startActivity(intent)
    }


    /**
     * Call API to find user by his username
     */
    private fun testIfUserExist(username: String, callback: Callback) {
        val url = API.BASE_URL_HTTP + API.GET_USER + username
        val request = Request.Builder()
            .url(url)
            .build()
        WebSocketLiveData.client.newCall(request).enqueue(callback)
    }

    /**
     * Create a new user
     */
    private fun registerUser(username: String) {
        val userPost = UserPost(username)
        val requestBody = userPostAdapter.toJson(userPost).toRequestBody()

        val request = Request.Builder()
            .url(API.BASE_URL_HTTP + API.REGISTER_USER)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@HomeActivity, getString(R.string.error_register), Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    runOnUiThread {
                        Toast.makeText(this@HomeActivity, getString(R.string.user_registered, username), Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    fun goToScorePage(){
        val intent = Intent(this, ScoreActivity::class.java)
        startActivity(intent)

    }



}
