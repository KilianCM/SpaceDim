package com.lpdim.spacedim.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.lpdim.spacedim.R
import com.lpdim.spacedim.api.API
import com.lpdim.spacedim.game.GameActivity
import com.lpdim.spacedim.game.WebSocketManager.okHttpClient
import com.lpdim.spacedim.utils.MoshiService.userAdapter
import com.lpdim.spacedim.utils.MoshiService.userPostAdapter
import com.lpdim.spacedim.game.model.UserPost
import com.lpdim.spacedim.score.ScoreActivity
import okhttp3.RequestBody.Companion.toRequestBody
import kotlinx.android.synthetic.main.activity_home.*
import okhttp3.*
import java.io.IOException
import java.lang.Exception

class HomeActivity : AppCompatActivity() {

    private var userInfos: Pair<Int?,String?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // get userid and username stored in shared preferences
        userInfos = getUserFromSharedPreferences()
        userInfos?.second?.let {
            editTextName.setText(it, TextView.BufferType.EDITABLE)
        } ?: run {
            displayLoginDialog()
        }

        btnLaunchGame.setOnClickListener {
            userInfos?.first?.let {
                displayRoomSelectDialog(it)
            } ?: kotlin.run {
                displayLoginDialog()
            }
        }

        btnLogin.setOnClickListener {
            displayLoginDialog()
        }

        btnScore.setOnClickListener {
            goToScorePage()
        }
    }

    /**
     * Check if User is already created, if not we create a new one
     */
    private fun checkUser(username: String) {
        testIfUserExist(username, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    try {
                        response.body?.let { body ->
                            val user = userAdapter.fromJson(body.source())
                            user?.let {
                                saveUserInSharedPreferences(user.id, user.name)
                                runOnUiThread {
                                    editTextName.setText(it.name, TextView.BufferType.EDITABLE)
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    // if response is not successful, the user doesn't exist so we need to register a new user
                    registerUser(username)
                    // relaunch this method to check the user and launch the roomDialog
                    checkUser(username)
                }
            }
        })
    }

    /**
     * Display a dialog window to type the room name
     */
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

    /**
     * Display a dialog window to log a user
     */
    private fun displayLoginDialog() {
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.login_dialog, null)
        builder.setView(view)
        builder.setTitle(getString(R.string.login_title))
            .setPositiveButton(R.string.ok) { dialog, id ->
                val username = view.findViewById<TextView>(R.id.editTextLogin).text
                checkUser(username.toString())
            }
            .setNegativeButton(R.string.cancel) { dialog, id ->
                dialog.cancel()
            }
        builder.create().show()
    }


    /**
     * Launch the GameActivity passing the userid and the room name as parameters
     */
    private fun launchGame(roomName: String, userId: Int) {
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
        okHttpClient.newCall(request).enqueue(callback)
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

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@HomeActivity, getString(R.string.error_register), Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                response.body?.let {
                    val user = userAdapter.fromJson(it.source())
                    user?.let { user ->
                        saveUserInSharedPreferences(user.id, user.name)
                    }
                }
                runOnUiThread {
                    Toast.makeText(this@HomeActivity, getString(R.string.user_registered, username), Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    /**
     * Launch the ScoreActivity
     */
    private fun goToScorePage(){
        val intent = Intent(this, ScoreActivity::class.java)
        startActivity(intent)
    }

    private fun getUserFromSharedPreferences(): Pair<Int?, String?> {
        val sharedPreferences =  getPreferences(Context.MODE_PRIVATE)

        var userId: Int? =  sharedPreferences.getInt(getString(R.string.id_key), -1)
        if(userId == -1) userId = null

        var userName: String? = sharedPreferences.getString(getString(R.string.username_key), "")
        if(userName == "") userName = null

        return Pair(userId, userName)
    }

    private fun saveUserInSharedPreferences(userId: Int, userName: String) {
        val sharedPreferences =  getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        userInfos = Pair(userId, userName)
        editor.putInt(getString(R.string.id_key), userId)
        editor.putString(getString(R.string.username_key), userName)
        editor.apply()
    }
}
