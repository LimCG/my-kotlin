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

class RegisterActivity : AppCompatActivity() {

    companion object {
        private val TAG = RegisterActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        toolbar.title = "Parent Register"
        toolbar.setNavigationOnClickListener { view ->
            onBackPressed()
        }

        btn_register_continue.setOnClickListener { view ->

            val parent_name = edt_register_parent_name.text.toString().trim()
            val parent_username = edt_register_parent_username.text.toString().trim()
            val parent_pass = edt_register_parent_password.text.toString()

            if( parent_name.length > 0 && parent_username.length > 0 && parent_pass.length > 0) {

                if( parent_pass.length > 7 ) {

                    val intent = Intent(this, RegisterFinalActivity::class.java)
                    intent.putExtra("parent_fullname", parent_name)
                    intent.putExtra("parent_username", parent_username)
                    intent.putExtra("parent_pass", parent_pass)
                    startActivity(intent)
                }
                else
                {
                    Toast.makeText(this, "Password length too short. Must more than 6 characters.", Toast.LENGTH_LONG).show()

                }

            } else {

                Toast.makeText(this, "Field is required!", Toast.LENGTH_LONG).show()
            }

        }


    }
}