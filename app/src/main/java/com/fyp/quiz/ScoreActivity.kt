package com.fyp.quiz

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_score.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.request.JsonObjectRequest
import org.json.JSONException
import org.json.JSONObject


/**
 * Created by limcg on 14/01/2018.
 */
class ScoreActivity : AppCompatActivity() {

    companion object {
        private val TAG = ScoreActivity::class.java.simpleName
    }

    lateinit var topicTitleArrayList : ArrayList<String>
    lateinit var scoreObjectArrayList : ArrayList<ScoreObject>

    var is_parent : Boolean = false
    lateinit var server_url : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        if(intent.hasExtra("is_parent"))
        {
            is_parent = intent.getBooleanExtra("is_parent", false);
        }

        if(is_parent)
        {
            server_url = Constants.PARENT_LIST_TRIAL_SCORE
        }
        else
        {
            server_url = Constants.LIST_TRIAL_SCORE
        }

        topicTitleArrayList = ArrayList<String>()
        scoreObjectArrayList = ArrayList<ScoreObject>()

        toolbar.title = "LATIHAN SKOR"
        toolbar.setNavigationOnClickListener { view ->

            onBackPressed()
        }

        try {

            val jo = JSONObject()
            jo.put("user_id", MyPref.userIDPrefs)

            val requestScore = JsonObjectRequest(Request.Method.POST, server_url,
                    jo, Response.Listener { response ->

                        try {

                            val status = response.getString("status")

                            if(status.equals("OK", true))
                            {
                                val array = response.getJSONArray("data")

                                for(i in 0 until array.length())
                                {
                                    val innerArray = array.getJSONArray(i)

                                    for(z in 0 until innerArray.length())
                                    {
                                        val trial_topic = innerArray.getJSONObject(z).getString("trial_topic")

                                        if( !topicTitleArrayList.contains(trial_topic))
                                        {
                                            topicTitleArrayList.add(trial_topic)
                                        }

                                        val scoreObj = ScoreObject()
                                        val percentage = innerArray.getJSONObject(z).getInt("percentage")
                                        scoreObj.percentage = percentage

                                        val trial_result_id = innerArray.getJSONObject(z).getInt("trial_result_id")
                                        scoreObj.trial_result_id = trial_result_id
                                        scoreObj.trial_topic = trial_topic

                                        scoreObjectArrayList.add(scoreObj)

                                    }

                                }

                                // Generate Table Layout
                                generateTableLayout()


                            }
                            else
                            {
                                // no result
                                Toast.makeText(this, "No have score", Toast.LENGTH_LONG).show()

                                finish()
                            }


                        } catch (e : JSONException) {

                            Toast.makeText(this, "Error.", Toast.LENGTH_LONG).show()

                            finish()

                        }

                    }, Response.ErrorListener { error ->

                Toast.makeText(this, "Error.", Toast.LENGTH_LONG).show()

                finish()

            });

            MyApp.instance!!.addToRequestQueue(requestScore, TAG)

        } catch (e : JSONException) {

        }

    }

    private fun generateTableLayout()
    {
        val params_Topic = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        params_Topic.setMargins(0, 20, 0, 20)
        val params = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f)
        params.setMargins(0, 20, 0, 20)

        val row_header = TableRow(this)

        for(i in 0 until 4)
        {
            val txt_view = TextView(this)
            txt_view.textSize = 16f
            txt_view.setTypeface(null, Typeface.BOLD)


            if(i == 0)
            {
                txt_view.text = "Topic"
                txt_view.layoutParams = params_Topic

            }
            else
            {
                txt_view.text = "Trial " + i
                txt_view.layoutParams = params
                txt_view.gravity = Gravity.CENTER
            }

            row_header.addView(txt_view)
        }

        table_trial_attempt.addView(row_header)

        // Draw horizontal line
        val line = View(this)
        line.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5)
        line.setBackgroundColor(Color.BLACK)
        table_trial_attempt.addView(line)

        for(i in 0 until topicTitleArrayList.size)
        {
            val row = TableRow(this); // Row 1
            val txt_topic = TextView(this)
            txt_topic.text = topicTitleArrayList.get(i)
            txt_topic.layoutParams = params_Topic
            txt_topic.textSize = 14f
            txt_topic.setTypeface(null, Typeface.BOLD)
            row.addView(txt_topic)

            for(z in 0 until scoreObjectArrayList.size)
            {
                if( scoreObjectArrayList.get(z).trial_topic.equals(topicTitleArrayList.get(i), true))
                {
                    val txt_score = TextView(this)
                    txt_score.textSize = 14f
                    txt_score.setTypeface(null, Typeface.BOLD)
                    txt_score.gravity = Gravity.CENTER

                    if( scoreObjectArrayList.get(z).percentage < 0 )
                    {
                        txt_score.text = "-"
                    }
                    else
                    {
                        txt_score.text = scoreObjectArrayList.get(z).percentage.toString() + " %"


                    }

                    txt_score.layoutParams = params
                    row.addView(txt_score)

                }

            }

            table_trial_attempt.addView(row)

        }

    }
}