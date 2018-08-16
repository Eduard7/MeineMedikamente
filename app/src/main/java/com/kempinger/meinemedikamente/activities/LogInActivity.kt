package com.kempinger.meinemedikamente.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.kempinger.meinemedikamente.R
import com.kempinger.meinemedikamente.model.SnackBar
import kotlinx.android.synthetic.main.activity_log_in.*
import java.util.*




@Suppress("DEPRECATION")
class LogInActivity : AppCompatActivity() {


    private var flagp: Boolean = false
    private var flagu: Boolean = false
    private var countTrys = 0
    private var regbuttonisVisble = false
     private var callbackManager = CallbackManager.Factory.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)



        FacebookSdk.getApplicationContext()
        AppEventsLogger.activateApp(this)



        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired


        if (isLoggedIn){
            loginSucces()
        }

        editusername.hint = USERNAME
        editpassword.hint = PASSWORD

        button_submit.visibility = View.INVISIBLE
        getUserName()

        button_submit.alpha = 1f
        getUserPassword()

        callbackManager = CallbackManager.Factory.create()
        login_button.setOnClickListener {





            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"))

            LoginManager.getInstance().registerCallback(callbackManager,
                    object : FacebookCallback<LoginResult> {
                        override fun onSuccess(loginResult: LoginResult) {

                            loginSucces()
                        }

                        override fun onCancel() {

                            LoginManager.getInstance().logOut()
                            exitApplication()

                        }

                        override fun onError(exception: FacebookException) {
                            LoginManager.getInstance().logOut()

                        }
                    })

        }

        if (xusername == USERNAME) {
            editusername.setText(USERNAME)
            editpassword.setText(PASSWORD)
            button_reg.visibility = View.VISIBLE
            regbuttonisVisble = true
        } else {
            editusername.hint = xusername.substring(0..1) + punktPunkt
            editpassword.hint = xpassword.substring(0..1) + punktPunkt
            button_reg.visibility = View.INVISIBLE
            regbuttonisVisble = false
        }


        button_reg.setOnClickListener {
            if (flagp == true && flagu == true) {
                username = editusername.text.toString()
                userpassword = editpassword.text.toString()
                SnackBar().showInfo(findViewById(android.R.id.content),getString(R.string.success),showGreenChar)
                saveUserName()
                saveUserPassword()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.alpha_from, R.anim.alpha_to)
            }

        }

        //CLEAR
        buttonclear.setOnClickListener {
            editpassword.setText("")
            editusername.setText("")
            editusername.requestFocus(0)
            flagp = false
            flagu = false

            // editpassword.isFocusable = false
        }


        //EDIT USERNAME
        editusername.setOnEditorActionListener { _ , actionId, _ ->

            val handled = false
            flagu = false
            if (editusername.text.isNotEmpty() && editusername.text.length > 5) {
                flagu = true

            }

            if (actionId == EditorInfo.IME_ACTION_DONE) {


                val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.SHOW_FORCED)
            }


            handled
        }

        //EDIT PASSWORD

        editpassword.setOnEditorActionListener { _, actionId, _ ->
            val handled = false

            if (editpassword.text.isNotEmpty() && editpassword.text.length > 3) {

                flagp = true


                if (!regbuttonisVisble) {
                    button_submit.visibility = View.VISIBLE
                    editusername.hint = USERNAME
                    editpassword.hint = PASSWORD
                }
            } else {
                flagp = false
            }
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.SHOW_FORCED)
            }


            handled
        }

        //SUBMIT

        val bs = findViewById<Button>(R.id.button_submit)

        bs.setOnClickListener(View.OnClickListener {
            countTrys += 1


            val edpwrd = editpassword.text.toString()
            val edusr = editusername.text.toString()

            if (edpwrd != xpassword) {
                flagp = false
            }
            if (edusr != xusername) {
                flagu = false
            }

            if (edpwrd == PASSWORD && edusr == USERNAME) {
                button_reg.visibility = View.VISIBLE
                regbuttonisVisble = true
                flagp = false
                flagu = false
                bs.visibility = View.INVISIBLE

            }
            if (edpwrd == xpassword && edusr == xusername) {
                flagp = true
                flagu = true

            }

            if (countTrys > 3) {
                exitApplication()
            }




            if (!flagu || !flagp) {

                val lf = "Login failed the $countTrys. time!"
                SnackBar().showInfo(findViewById(android.R.id.content),lf,!showGreenChar)
                editpassword.setText("")
                editusername.setText("")
                editusername.requestFocus(0)

            } else {

                bs.alpha = 0f

                val intent = Intent(this, MainActivity::class.java)

                startActivity(intent)

            }
        })



    }


    //FB ACTIVITY RESULT

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }


    // EXIT APP
    private fun exitApplication() {
        finishAndRemoveTask()
        this.finishAffinity()

    }

    //LOGIN SUCCES
    private fun loginSucces() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.alpha_from, R.anim.alpha_to)
    }

    //SAVE PASSWORRD
    private fun saveUserPassword() {

        val sharedFileName = filename
        val prefs = getSharedPreferences(sharedFileName, 0)
        val editor = prefs!!.edit()

        editor.putString(prefpswrd, userpassword)
        editor.apply()


    }

    private fun getUserPassword() {

        val sharedFileName = filename
        val prefs = getSharedPreferences(sharedFileName, 0)

        xpassword = prefs.getString(prefpswrd, PASSWORD)
        if (xpassword.isEmpty()) {
            xpassword = PASSWORD
        }

    }

    //SAVE USERNAME
    private fun saveUserName() {

        val sharedFileName = filename
        val prefs = getSharedPreferences(sharedFileName, 0)
        val editor = prefs!!.edit()

        editor.putString(prefusers, username)
        editor.apply()


    }

    private fun getUserName() {

        val sharedFileName = filename
        val prefs = getSharedPreferences(sharedFileName, 0)

        xusername = prefs.getString(prefusers, USERNAME)
        if (xusername.isEmpty()) {
            xusername = USERNAME
        }

    }

}
