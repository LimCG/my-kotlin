package com.fyp3.quiz

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.request.JsonObjectRequest
import com.android.volley.request.StringRequest
import kotlinx.android.synthetic.main.activity_choose_topic_exercise.*
import org.json.JSONException
import org.json.JSONObject

class ChooseTopicExerciseActivity : AppCompatActivity() {

    companion object {

        private val TAG = ChooseTopicExerciseActivity::class.java.simpleName
    }

    lateinit var arrayListTopic : ArrayList<String>
    lateinit var arrayListTopicID : ArrayList<Int>
    lateinit var arrayListSubTopic : ArrayList<String>
    lateinit var arrayListSubTopicID : ArrayList<Int>

    var selectedTopicID : Int = 0
    var selectedSubTopicID : Int = 0
    lateinit var selectedTopic : String
    lateinit var selectedSubTopic : String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_topic_exercise)

        arrayListTopic = ArrayList<String>()
        arrayListTopicID = ArrayList<Int>()
        arrayListSubTopic = ArrayList<String>()
        arrayListSubTopicID = ArrayList<Int>()

        toolbar.title = resources.getString(R.string.topic_subtopic_selection)
        toolbar.setNavigationOnClickListener { view ->

            onBackPressed()
        }

        txt_trial_subtopic.visibility = View.GONE
        spinner_exercise_subtopic.visibility = View.GONE

        val pDialog = Utils.showProgressDialog(this, resources.getString(R.string.processing))

        val JsonRequest = JsonObjectRequest(Request.Method.POST, Constants.GET_TOPIC_LIST, null,
                Response.Listener { response ->

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
                            spinner_exercise_topic.adapter = arrayAdapter

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

        spinner_exercise_topic.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                // Clean up Subtopic String container every time selected main topic
                if( arrayListSubTopic.size > 0 )
                {
                    arrayListSubTopic.clear()
                }

                selectedTopicID = arrayListTopicID.get(position)
                selectedTopic = arrayListTopic.get(position)

                val stringRequest = object : StringRequest(Request.Method.POST, Constants.GET_SUBTOPIC_LIST,
                        Response.Listener { response ->

                            Utils.dismissProgressDialog(pDialog)

                            try {

                                val jsonObj = JSONObject(response)

                                if( jsonObj.getString("status").equals("OK", true))
                                {
                                    val dataArray = jsonObj.getJSONArray("data")
                                    for(i in 0 until dataArray.length())
                                    {
                                        val subtopic_id = dataArray.getJSONObject(i).getInt("subtopic_id")
                                        arrayListSubTopicID.add(i, subtopic_id)

                                        val subtopic_name = dataArray.getJSONObject(i).getString("subtopic_name")
                                        arrayListSubTopic.add(i, subtopic_name)
                                    }

                                    txt_trial_subtopic.visibility = View.VISIBLE
                                    spinner_exercise_subtopic.visibility = View.VISIBLE

                                    val arrayAdapter = ArrayAdapter<String>(view!!.context,
                                            android.R.layout.simple_dropdown_item_1line, arrayListSubTopic)
                                    spinner_exercise_subtopic.adapter = arrayAdapter

                                }
                                else
                                {
                                    // Result error
                                    Toast.makeText(this@ChooseTopicExerciseActivity, "Error!", Toast.LENGTH_LONG).show()

                                }

                            } catch (e : JSONException) {

                                Toast.makeText(this@ChooseTopicExerciseActivity, "Error!", Toast.LENGTH_LONG).show()

                            }

                        }, Response.ErrorListener { error ->

                    Utils.dismissProgressDialog(pDialog)

                    Toast.makeText(this@ChooseTopicExerciseActivity, "Error!", Toast.LENGTH_LONG).show()



                }) {
                    override fun getParams(): MutableMap<String, String> {

                        val params = HashMap<String, String>()
                        params.put("topic_id", selectedTopicID.toString())

                        return params
                    }
                }

                MyApp.instance!!.addToRequestQueue(stringRequest, TAG)

            }

        }

        spinner_exercise_subtopic.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                selectedSubTopic = arrayListSubTopic.get(position)
                selectedSubTopicID = arrayListSubTopicID.get(position)

            }
        }

        btn_go_to_exercise.setOnClickListener { view ->

            val intent = Intent(this@ChooseTopicExerciseActivity, ExerciseActivity::class.java)
            intent.putExtra("intent_topic", selectedTopic)
            intent.putExtra("intent_topic_id", selectedTopicID)
            intent.putExtra("intent_subtopic", selectedSubTopic)
            intent.putExtra("intent_subtopic_id", selectedSubTopicID)
            startActivity(intent)
            finish()
        }


    }


}