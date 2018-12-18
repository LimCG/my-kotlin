package com.fyp3.quiz

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.request.StringRequest
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_splash.*
import org.json.JSONException
import org.json.JSONObject

class SplashActivity : AppCompatActivity() {

    companion object {

        val TAG = LoginActivity::class.java.simpleName

        val TEACHER_ROLE = 1111
        val PARENT_ROLE = 2222
        val STUDENT_ROLE = 3333

    }

    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds

    internal val mRunnable: Runnable = Runnable {
        if (!isFinishing) {

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        mDelayHandler = Handler()
//
//        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

        object : CountDownTimer(3500, 1000) {

            override fun onTick(l: Long) {

                Log.e("TAG", l.toString())

            }

            override fun onFinish() {

                findViewById<View>(R.id.loadingProgressBar).visibility = View.GONE
                findViewById<View>(R.id.rootView).setBackgroundColor(ContextCompat.getColor(this@SplashActivity, R.color.colorSplashText))
                bookIconImageView.setImageResource(R.mipmap.ic_launcher_round)
                startAnimation()

            }
        }.start()

    }

//    override fun onDestroy() {
//
//        mDelayHandler?.removeCallbacks(mRunnable)
//
//        super.onDestroy()
//    }

    private fun startAnimation() {

        bookIconImageView.animate().x(50f).y(100f).setDuration(1000).setListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {

            }

            override fun onAnimationEnd(animator: Animator) {

                findViewById<View>(R.id.afterAnimationView).visibility = View.VISIBLE

                findViewById<View>(R.id.loginButton).setOnClickListener { view ->

                    val LoginUser = usernameEditText.text.toString().trim()
                    val LoginPass = passwordEditText.text.toString().trim()

                    if( LoginUser.length <= 0 )
                    {
                        Toast.makeText(this@SplashActivity, "Field is required!", Toast.LENGTH_LONG).show()
                    }
                    else if( LoginPass.length <= 0 )
                    {
                        Toast.makeText(this@SplashActivity, "Field is required!", Toast.LENGTH_LONG).show()
                    }
                    else {

                        // Login now

                        if (LoginUser.equals("admin", true) && LoginPass.equals("admin", true)) {
                            val Intent = Intent(this@SplashActivity, ChooseTopicExerciseActivity::class.java)
                            startActivity(Intent)
                            finish()
                        }
                        else
                        {
                            Toast.makeText(this@SplashActivity, "Authentication error!", Toast.LENGTH_LONG).show()
                        }

                    }

//                        val progressDialog = Utils.showProgressDialog(this@SplashActivity, getString(R.string.processing))
//
//                        val PostRequest = object : StringRequest(Request.Method.POST, Constants.LOGIN_URL,
//                                Response.Listener { response ->
//
//                                    try {
//
//                                        val JsonObj = JSONObject(response)
//                                        val Status = JsonObj.getString("status")
//                                        val StatusMsg = JsonObj.getString("status_msg")
//
//                                        if(Status.equals("OK"))
//                                        {
//                                            val login_role = JsonObj.getJSONObject("data").getInt("user_role")
//
//                                            val user_id = JsonObj.getJSONObject("data").getInt("user_id")
//                                            MyPref.userIDPrefs = user_id
//
//                                            val fullname = JsonObj.getJSONObject("data").getString("full_name")
//                                            MyPref.userFullNamePrefs = fullname
//
//                                            when (login_role)
//                                            {
//                                                TEACHER_ROLE -> {
//
//                                                    val Intent = Intent(this@SplashActivity, TeacherActivity::class.java)
//                                                    startActivity(Intent)
//                                                    finish()
//                                                }
//
//                                                PARENT_ROLE -> {
//
//                                                    val intent = Intent(this@SplashActivity, ParentActivity::class.java)
//                                                    startActivity(intent)
//                                                    finish()
//                                                }
//
//                                                STUDENT_ROLE -> {
//
//                                                    val Intent = Intent(this@SplashActivity, ChooseTopicExerciseActivity::class.java)
//                                                    startActivity(Intent)
//                                                    finish()
//
//                                                }
//
//                                                else -> {
//
//                                                    Toast.makeText(this@SplashActivity, "Wrong TAG", Toast.LENGTH_LONG).show()
//                                                }
//                                            }
//
//                                        }
//
//                                        Toast.makeText(this@SplashActivity, StatusMsg, Toast.LENGTH_LONG).show()
//
//                                    } catch (e : JSONException) {
//
//                                        Toast.makeText(this@SplashActivity, "ERROR!", Toast.LENGTH_LONG).show()
//                                    }
//
//                                    Utils.dismissProgressDialog(progressDialog)
//
//                                }, Response.ErrorListener { error ->
//
//                            Toast.makeText(this@SplashActivity, "ERROR!", Toast.LENGTH_LONG).show()
//
//                            Utils.dismissProgressDialog(progressDialog)
//
//                        }) {
//                            override fun getParams(): MutableMap<String, String> {
//
//                                val Params = HashMap<String, String>()
//                                Params.put("username", LoginUser)
//                                Params.put("userpass", LoginPass)
//
//                                return Params
//                            }
//                        }
//
//                        MyApp.instance!!.addToRequestQueue(PostRequest, TAG)
//                    }


                }

            }

            override fun onAnimationCancel(animator: Animator) {

            }

            override fun onAnimationRepeat(animator: Animator) {

            }
        })
    }
}