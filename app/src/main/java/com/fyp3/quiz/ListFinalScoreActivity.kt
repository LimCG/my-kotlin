package com.fyp3.quiz

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.request.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_list_final_score.*
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by limcg on 01/02/2018.
 */
class ListFinalScoreActivity : AppCompatActivity() {

    companion object {
        private val TAG = ListFinalScoreActivity::class.java.simpleName
    }

    var is_parent : Boolean = false
    lateinit var server_url : String
    lateinit var user_id : String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_final_score)

        if(intent.hasExtra("is_parent"))
        {
            is_parent = intent.getBooleanExtra("is_parent", false);
        }

        if(is_parent)
        {
            server_url = Constants.PARENT_LIST_FINAL_SCORE
        }
        else
        {
            server_url = Constants.LIST_FINAL_SCORE
        }

        if(intent.hasExtra("user_id"))
        {
            user_id = intent.getStringExtra("user_id")
        }
        else
        {
            user_id = MyPref.userIDPrefs.toString()
        }

        toolbar.title = "Final Exam Score"
        toolbar.setNavigationOnClickListener { view ->
            onBackPressed()
        }

        try {

            val jo = JSONObject()
            jo.put("user_id", user_id)

            val pDialog = Utils.showProgressDialog(this@ListFinalScoreActivity, resources.getString(R.string.processing))
            val request = JsonObjectRequest(Request.Method.POST, server_url, jo,
                    Response.Listener { response ->

                        Utils.dismissProgressDialog(pDialog)

                        if( response.getString("status").equals("OK", true))
                        {
                            val arrayList = ArrayList<String>()
                            val jsonArray = response.getJSONArray("data")

                            if(arrayList.size > 0)
                            {
                                arrayList.clear()
                            }

                            for(i in 0 until jsonArray.length())
                            {
                                val percentage = jsonArray.getJSONObject(i).getString("percentage")
                                val datetime = jsonArray.getJSONObject(i).getString("datetime")

                                val displayTxt = i.inc().toString() + ".    Score " + percentage + "%   " + datetime

                                arrayList.add(displayTxt)
                            }

                            listview_list_final.adapter = ArrayAdapter<String>(this@ListFinalScoreActivity,
                                    android.R.layout.simple_expandable_list_item_1, arrayList)
                        }


                    }, Response.ErrorListener { error ->

                Utils.dismissProgressDialog(pDialog)

            })

            MyApp.instance!!.addToRequestQueue(request, TAG)

        } catch (e: JSONException) {

        }


    }
}