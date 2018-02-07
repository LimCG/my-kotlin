package com.fyp3.quiz

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.request.StringRequest
import kotlinx.android.synthetic.main.activity_register_final.*
import org.json.JSONException
import org.json.JSONObject

class RegisterFinalActivity : AppCompatActivity() {

    companion object {
        private val TAG = RegisterFinalActivity::class.java.simpleName
    }

    lateinit var parent_fullname : String
    lateinit var parent_username : String
    lateinit var parent_pass : String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_final)

        toolbar.title = "Student Register"
        toolbar.setNavigationOnClickListener { view ->
            onBackPressed()
        }

        if(intent.hasExtra("parent_fullname"))
        {
            parent_fullname = intent.getStringExtra("parent_fullname");
        }

        if(intent.hasExtra("parent_username"))
        {
            parent_username = intent.getStringExtra("parent_username")
        }

        if(intent.hasExtra("parent_pass"))
        {
            parent_pass = intent.getStringExtra("parent_pass")
        }

        btn_register.setOnClickListener { view ->

            val child_name = edt_register_child_name.text.toString().trim()
            val child_username = edt_register_child_username.text.toString().trim()
            val child_pass = edt_register_child_password.text.toString()

            if(child_name.length > 0 && child_username.length > 0 && child_pass.length > 0)
            {
                if( child_pass.length > 6 )
                {
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

                            Toast.makeText(this, status_msg, Toast.LENGTH_LONG).show()

                        } catch (e : JSONException) {

                            Toast.makeText(this, "ERROR!", Toast.LENGTH_LONG).show()


                        }


                    }, Response.ErrorListener { error ->

                        Toast.makeText(this, "ERROR!", Toast.LENGTH_LONG).show()


                    }) {
                        override fun getParams(): MutableMap<String, String> {

                            val param = HashMap<String, String>()
                            param.put("parent_name", parent_fullname)
                            param.put("parent_username", parent_username)
                            param.put("parent_pass", parent_pass)
                            param.put("child_name", child_name)
                            param.put("child_username", child_username)
                            param.put("child_pass", child_pass)

                            return param
                        }
                    }

                    MyApp.instance!!.addToRequestQueue(registerRequest, TAG)

                }

            }

        }

    }
}