package com.fyp3.quiz

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.request.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_list_trial_score.*
import org.json.JSONException
import org.json.JSONObject


/**
 * Created by limcg on 14/01/2018.
 */
class ListTrialScoreActivity : AppCompatActivity() {

    companion object {
        private val TAG = ListTrialScoreActivity::class.java.simpleName
    }

    lateinit var scoreObjectArrayList : ArrayList<ScoreObject>

    var is_parent : Boolean = false
    lateinit var server_url : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_trial_score)

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

        scoreObjectArrayList = ArrayList<ScoreObject>()

        toolbar.title = "Trial Exam Score"
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

                                        val scoreObj = ScoreObject()
                                        val percentage = innerArray.getJSONObject(z).getInt("percentage")
                                        scoreObj.percentage = percentage

                                        val score_feedback = innerArray.getJSONObject(z).getString("score_feedback")
                                        scoreObj.score_feedback = score_feedback

                                        val taken_datetime = innerArray.getJSONObject(z).getString("datetime")
                                        scoreObj.taken_datetime = taken_datetime

                                        val trial_result_id = innerArray.getJSONObject(z).getInt("trial_result_id")
                                        scoreObj.trial_result_id = trial_result_id

                                        val trial_topic = innerArray.getJSONObject(z).getString("trial_topic")
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
                                Toast.makeText(this, "No have score.", Toast.LENGTH_LONG).show()

                                finish()
                            }


                        } catch (e : JSONException) {

                            Toast.makeText(this, "ERROR.", Toast.LENGTH_LONG).show()

                            finish()

                        }

                    }, Response.ErrorListener { error ->

                Toast.makeText(this, "ERROR.", Toast.LENGTH_LONG).show()

                finish()

            });

            MyApp.instance!!.addToRequestQueue(requestScore, TAG)

        } catch (e : JSONException) {

        }

    }

    private fun generateTableLayout()
    {
        val params_Topic = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.2f)
        params_Topic.setMargins(0, 20, 0, 20)
        val params = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.5f)
        params.setMargins(0, 20, 0, 20)

        val row_header = TableRow(this)

        for(i in 0 until 4)
        {
            val txt_view = TextView(this)
            txt_view.textSize = 14f
            txt_view.setTypeface(null, Typeface.BOLD)

            if(i == 0)
            {
                txt_view.text = "Topic"
                txt_view.layoutParams = params_Topic
                txt_view.gravity = Gravity.CENTER

            }
            else if(i == 1)
            {
                txt_view.text = "DateTime"
                txt_view.layoutParams = params
                txt_view.gravity = Gravity.CENTER
            }
            else if(i == 2)
            {
                txt_view.text = "Score"
                txt_view.layoutParams = params
                txt_view.gravity = Gravity.CENTER
            }
            else
            {
                txt_view.text = "Feedback"
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

        for(z in 0 until scoreObjectArrayList.size)
        {
            val data_row = TableRow(this)

            // Topic Column
            val txt_score_topic = TextView(this)
            txt_score_topic.layoutParams = params_Topic
            txt_score_topic.textSize = 12f
            txt_score_topic.setTypeface(null, Typeface.BOLD)
            txt_score_topic.text = scoreObjectArrayList.get(z).trial_topic

            data_row.addView(txt_score_topic)

            // DateTime Column
            val txt_score_datetime = TextView(this)
            txt_score_datetime.layoutParams = params
            txt_score_datetime.textSize = 12f
            txt_score_datetime.setTypeface(null, Typeface.BOLD)
            txt_score_datetime.gravity = Gravity.CENTER
            txt_score_datetime.text = scoreObjectArrayList.get(z).taken_datetime

            data_row.addView(txt_score_datetime)

            // Percentage Column
            val txt_score_percentage = TextView(this)
            txt_score_percentage.layoutParams = params
            txt_score_percentage.textSize = 12f
            txt_score_percentage.setTypeface(null, Typeface.BOLD)
            txt_score_percentage.gravity = Gravity.CENTER
            txt_score_percentage.text = scoreObjectArrayList.get(z).percentage.toString() + "%"

            data_row.addView(txt_score_percentage)

            // Score Feedback Column
            val txt_score_feedback = TextView(this)
            txt_score_feedback.layoutParams = params
            txt_score_feedback.textSize = 12f
            txt_score_feedback.setTypeface(null, Typeface.BOLD)
            txt_score_feedback.gravity = Gravity.CENTER
            txt_score_feedback.text = scoreObjectArrayList.get(z).score_feedback

            data_row.addView(txt_score_feedback)

            table_trial_attempt.addView(data_row)
        }

    }
}