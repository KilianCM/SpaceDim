package com.lpdim.spacedim.home

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.lpdim.spacedim.R
import com.lpdim.spacedim.api.API
import com.lpdim.spacedim.game.GameActivity
import com.lpdim.spacedim.game.MoshiService
import com.lpdim.spacedim.game.MoshiService.userAdapter
import com.lpdim.spacedim.game.MoshiService.userPostAdapter
import com.lpdim.spacedim.game.WebSocketLiveData
import com.lpdim.spacedim.game.WebSocketLiveData.Companion.client
import com.lpdim.spacedim.game.model.User
import com.lpdim.spacedim.game.model.UserPost
import com.squareup.moshi.Json
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
            val builder = AlertDialog.Builder(this)
            val inflater = HomeActivity().layoutInflater;
            builder.setView(inflater.inflate(R.layout.activity_home, null))
            builder.setTitle("Registering")
            builder.create()
            //TODO: Rajouter l'editText pour taper le nom de l'utilisateur
        }
        //TODO: ajouter un bouton "S'inscrire" qui lance un Dialog (https://developer.android.com/guide/topics/ui/dialogs) avec un champs pour saisir le nouveau username
        //TODO: ajouter bouton "Jouer" qui récupère le text de l'input textBoxName et qui ouvre un Dialog pour saisir le nom de la room
    }


    /**
     * Launch the GameActivity passing the userid and the room name as parameters
     */
    fun launchGame(view: View) {
        val intent = Intent(this, GameActivity::class.java).apply {
            //TODO: put extra with roomname and userid
            //putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }


    /**
     * Call API to find user by his username
     * @return userId|null the id of the found user or null if the user doesn't exist
     */
    private fun testIfUserExist(username: String): Int?{
        var userId: Int? = null
        val url = API.BASE_URL_HTTP + API.GET_USER + username
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
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    Toast.makeText(this@HomeActivity, "User $username are created", Toast.LENGTH_LONG).show()
                    //TODO: Toast utilisateur créé
                }
            }
        })
    }



}
