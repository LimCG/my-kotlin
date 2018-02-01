package com.fyp.quiz

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.request.StringRequest
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by limcg on 01/01/2018.
 */
class LoginActivity : AppCompatActivity() {

    companion object {
        private val TAG = LoginActivity::class.java.simpleName

        private val TEACHER_ROLE = 1111
        private val PARENT_ROLE = 2222
        private val STUDENT_ROLE = 3333

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        toolbar.setNavigationOnClickListener { view ->

            onBackPressed()
        }

        toolbar.title = this.resources.getText(R.string.btn_login)

        btn_register.setOnClickListener{ view ->

            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btn_login.setOnClickListener{ view ->

            val LoginUser = edt_login_user.text.toString().trim()
            val LoginPass = edt_login_pass.text.toString().trim()

            if( LoginUser.length <= 0 )
            {
                Toast.makeText(this, "Field is required!", Toast.LENGTH_LONG).show()
            }
            else if( LoginPass.length <= 0 )
            {
                Toast.makeText(this, "Field is required!", Toast.LENGTH_LONG).show()
            }
            else
            {
                // Login now

                val progressDialog = Utils.showProgressDialog(this, this.resources.getString(R.string.processing))

                val PostRequest = object : StringRequest(Request.Method.POST, Constants.LOGIN_URL,
                        Response.Listener { response ->

                            try {

                                val JsonObj = JSONObject(response)
                                val Status = JsonObj.getString("status")
                                val StatusMsg = JsonObj.getString("status_msg")

                                if(Status.equals("OK"))
                                {
                                    val login_role = JsonObj.getJSONObject("data").getInt("user_role")

                                    val user_id = JsonObj.getJSONObject("data").getInt("user_id")
                                    MyPref.userIDPrefs = user_id

                                    val fullname = JsonObj.getJSONObject("data").getString("full_name")
                                    MyPref.userFullNamePrefs = fullname

                                    when (login_role)
                                    {
                                        TEACHER_ROLE -> {

                                            val Intent = Intent(this, TeacherActivity::class.java)
                                            startActivity(Intent)
                                            finish()
                                        }

                                        PARENT_ROLE -> {

                                            val intent = Intent(this, ParentActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }

                                        STUDENT_ROLE -> {

                                            val Intent = Intent(this, StudentActivity::class.java)
                                            startActivity(Intent)
                                            finish()

                                        }

                                        else -> {

                                            Toast.makeText(this, "Wrong TAG", Toast.LENGTH_LONG).show()
                                        }
                                    }

                                }

                                Toast.makeText(this, StatusMsg, Toast.LENGTH_LONG).show()

                            } catch (e : JSONException) {

                                Toast.makeText(this, "ERROR!", Toast.LENGTH_LONG).show()
                            }

                            Utils.dismissProgressDialog(progressDialog)

                        }, Response.ErrorListener { error ->

                    Toast.makeText(this, "ERROR!", Toast.LENGTH_LONG).show()

                    Utils.dismissProgressDialog(progressDialog)

                }) {
                    override fun getParams(): MutableMap<String, String> {

                        val Params = HashMap<String, String>()
                        Params.put("username", LoginUser)
                        Params.put("userpass", LoginPass)

                        return Params
                    }
                }

                MyApp.instance!!.addToRequestQueue(PostRequest, TAG)
            }

        }

    }
}