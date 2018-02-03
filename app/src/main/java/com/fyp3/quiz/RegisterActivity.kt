package com.fyp3.quiz

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.request.StringRequest
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by limcg on 28/01/2018.
 */
class RegisterActivity : AppCompatActivity() {

    companion object {
        private val TAG = RegisterActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        toolbar.title = "Daftar"
        toolbar.setNavigationOnClickListener { view ->
            onBackPressed()
        }

        btn_register.setOnClickListener { view ->

            val parent_name = edt_register_parent_name.text.toString().trim()
            val parent_username = edt_register_parent_username.text.toString().trim()
            val parent_pass = edt_register_parent_password.text.toString()

            val child_name = edt_register_child_name.text.toString().trim()
            val child_username = edt_register_child_username.text.toString().trim()
            val child_pass = edt_register_child_password.text.toString()

            if( parent_name.length > 0 && parent_username.length > 0 && parent_pass.length > 0
                    && child_name.length >0 && child_username.length > 0 && child_pass.length > 0 ) {

                if( parent_pass.length > 6 && child_pass.length > 6 ) {

                    // Submit registration
                    val registerRequest = object : StringRequest(Request.Method.POST, Constants.REGISTER_URL, Response.Listener { response ->

                        try {

                            val json = JSONObject(response)

                            val status = json.getString("status")
                            val status_msg = json.getString("status_msg")

                            if(status.equals("OK", true))
                            {
                                val intent = Intent(this, LoginActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)

                            }
                            else
                            {
                                edt_register_parent_name.text.clear()
                                edt_register_parent_password.text.clear()
                                edt_register_parent_username.text.clear()

                                edt_register_child_name.text.clear()
                                edt_register_child_password.text.clear()
                                edt_register_child_username.text.clear()
                            }

                            Toast.makeText(this, status_msg, Toast.LENGTH_LONG).show()

                        } catch (e : JSONException) {

                            edt_register_parent_name.text.clear()
                            edt_register_parent_password.text.clear()
                            edt_register_parent_username.text.clear()

                            edt_register_child_name.text.clear()
                            edt_register_child_password.text.clear()
                            edt_register_child_username.text.clear()

                            Toast.makeText(this, "ERROR!", Toast.LENGTH_LONG).show()


                        }


                    }, Response.ErrorListener { error ->

                        edt_register_parent_name.text.clear()
                        edt_register_parent_password.text.clear()
                        edt_register_parent_username.text.clear()

                        edt_register_child_name.text.clear()
                        edt_register_child_password.text.clear()
                        edt_register_child_username.text.clear()

                        Toast.makeText(this, "ERROR!", Toast.LENGTH_LONG).show()


                    }) {
                        override fun getParams(): MutableMap<String, String> {

                            val param = HashMap<String, String>()
                            param.put("parent_name", parent_name)
                            param.put("parent_username", parent_username)
                            param.put("parent_pass", parent_pass)
                            param.put("child_name", child_name)
                            param.put("child_username", child_username)
                            param.put("child_pass", child_pass)

                            return param
                        }
                    }

                    MyApp.instance!!.addToRequestQueue(registerRequest, TAG)


                } else {

                    Toast.makeText(this, "Password length too short.", Toast.LENGTH_LONG).show()
                }


            } else {

                Toast.makeText(this, "Field is required!", Toast.LENGTH_LONG).show()
            }

        }


    }
}