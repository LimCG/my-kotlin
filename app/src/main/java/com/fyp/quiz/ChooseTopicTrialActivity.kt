package com.fyp.quiz

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.request.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_choose_topic_trial.*
import org.json.JSONException

/**
 * Created by limcg on 06/01/2018.
 */
class ChooseTopicTrialActivity : AppCompatActivity() {

    companion object {

        private val TAG = ChooseTopicTrialActivity::class.java.simpleName
    }

    lateinit var arrayListTopic : ArrayList<String>
    lateinit var arrayListTopicID : ArrayList<Int>

    var selectedTopicID : Int = 0
    lateinit var selectedTopic : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_topic_trial)

        arrayListTopic = ArrayList<String>()
        arrayListTopicID = ArrayList<Int>()

        toolbar.title = resources.getString(R.string.topic_selection)
        toolbar.setNavigationOnClickListener { view ->

            onBackPressed()
        }

        val pDialog = Utils.showProgressDialog(this, resources.getString(R.string.processing))

        val JsonRequest = JsonObjectRequest(Request.Method.POST, Constants.GET_TOPIC_LIST, null,
                Response.Listener { response ->

                    Utils.dismissProgressDialog(pDialog)

                    try {

                        if( response.getString("status").equals("OK", true) )
                        {

                            val dataJsonArray = response.getJSONArray("data")
                            for(i in 0 until dataJsonArray.length())
                            {
                                val topic_id = dataJsonArray.getJSONObject(i).getInt("topic_id")
                                arrayListTopicID.add(i, topic_id)

                                val topic = dataJsonArray.getJSONObject(i).getString("topic")
                                arrayListTopic.add(i, topic)
                            }

                            val arrayAdapter = ArrayAdapter<String>(this,
                                    android.R.layout.simple_dropdown_item_1line, arrayListTopic)
                            spinner_trial_topic.adapter = arrayAdapter

                        }
                        else
                        {
                            Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show()

                            finish()
                        }

                    } catch (e : JSONException) {

                        Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show()

                        finish()
                    }

                }, Response.ErrorListener { error ->


            Utils.dismissProgressDialog(pDialog)

            Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show()

            finish()

        })

        MyApp.instance!!.addToRequestQueue(JsonRequest, TAG)

        spinner_trial_topic.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                selectedTopicID = arrayListTopicID.get(position)
                selectedTopic = arrayListTopic.get(position)

            }

        }

        btn_go_to_trial.setOnClickListener { view ->

            val intent = Intent(this@ChooseTopicTrialActivity, TrialActivity::class.java)
            intent.putExtra("intent_topic", selectedTopic)
            intent.putExtra("intent_topic_id", selectedTopicID)
            startActivity(intent)

        }

    }
}