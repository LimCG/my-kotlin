package com.fyp3.quiz

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.request.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_list_exercise_score.*
import org.json.JSONException
import org.json.JSONObject

class ListExerciseScoreActivity : AppCompatActivity() {

    companion object {
        private val TAG = ListExerciseScoreActivity::class.java.simpleName
    }

    lateinit var mainTopicTitleArrayList : ArrayList<String>
    lateinit var subContainerArrayList : ArrayList<ExerciseScoreObject>

    var is_parent : Boolean = false
    lateinit var server_url : String
    lateinit var user_id : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_exercise_score)

        if(intent.hasExtra("is_parent"))
        {
            is_parent = intent.getBooleanExtra("is_parent", false);
        }

        if(is_parent)
        {
            server_url = Constants.PARENT_LIST_EXERCISE_SCORE
        }
        else
        {
            server_url = Constants.LIST_EXERCISE_SCORE
        }

        if(intent.hasExtra("user_id"))
        {
            user_id = intent.getStringExtra("user_id")
        }
        else
        {
            user_id = MyPref.userIDPrefs.toString()
        }

        mainTopicTitleArrayList = ArrayList<String>()
        subContainerArrayList = ArrayList<ExerciseScoreObject>()

        toolbar.title = "Exercise Score"
        toolbar.setNavigationOnClickListener { view ->
            onBackPressed()
        }

        try {

            val jo = JSONObject()
            jo.put("user_id", user_id)

            val pDialog = Utils.showProgressDialog(this, resources.getString(R.string.processing))

            val getExerciseScoreRequest = JsonObjectRequest(Request.Method.POST, server_url,
                    jo, Response.Listener { response ->

                        Utils.dismissProgressDialog(pDialog)

                        try {

                            val status = response.getString("status")

                            if(status.equals("OK", true))
                            {
                                val dataArray = response.getJSONArray("data")

                                for (i in 0 until dataArray.length())
                                {
                                    val firstArray = dataArray.getJSONArray(i)

                                    for(x in 0 until firstArray.length())
                                    {
                                        val json = firstArray.getJSONArray(x)

                                        for(z in 0 until json.length())
                                        {
                                            val exercise_topic = json.getJSONObject(z).getString("exercise_topic")
                                            val exercise_subtopic = json.getJSONObject(z).getString("exercise_subtopic")
                                            val percentage = json.getJSONObject(z).getInt("percentage")
                                            val exercise_result_id = json.getJSONObject(z).getInt("exercise_result_id")
                                            val datetime = json.getJSONObject(z).getString("datetime")
                                            val score_feedback = json.getJSONObject(z).getString("score_feedback")

                                            if(!mainTopicTitleArrayList.contains(exercise_topic))
                                            {
                                                mainTopicTitleArrayList.add(exercise_topic)
                                            }

                                            val exerciseScoreObject = ExerciseScoreObject()
                                            exerciseScoreObject.exercise_result_id = exercise_result_id
                                            exerciseScoreObject.exercise_subtopic = exercise_subtopic
                                            exerciseScoreObject.percentage = percentage
                                            exerciseScoreObject.exercise_topic = exercise_topic
                                            exerciseScoreObject.score_feedback = score_feedback
                                            exerciseScoreObject.taken_datetime = datetime

                                            subContainerArrayList.add(exerciseScoreObject)

                                        }

                                    }

                                }

                                // End of parse json. Process content

                                generateTableLayout()
                            }
                            else
                            {
                                // No Have score
                                Toast.makeText(this, "No have score.", Toast.LENGTH_LONG).show()

                                finish()
                            }


                        } catch (e : JSONException) {

                            Toast.makeText(this, "ERROR.", Toast.LENGTH_SHORT).show()

                            finish()

                        }

                    }, Response.ErrorListener { error ->

                Utils.dismissProgressDialog(pDialog)

                Toast.makeText(this, "ERROR.", Toast.LENGTH_SHORT).show()

                finish()


            })

            MyApp.instance!!.addToRequestQueue(getExerciseScoreRequest, TAG)

        } catch (e : JSONException) {

        }

    }

    private fun generateTableLayout()
    {
        var count = 0

        var row_content : TableRow? = null

        val params_Topic = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
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

        table_exercise_attempt.addView(row_header)

        // Draw horizontal line
        val line = View(this)
        line.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5)
        line.setBackgroundColor(Color.BLACK)
        table_exercise_attempt.addView(line)

        for(i in 0 until mainTopicTitleArrayList.size)
        {
            //Main topic head
            val row_main_header = TableRow(this)
            val txt_view = TextView(this)
            txt_view.textSize = 16f
            txt_view.setTypeface(null, Typeface.BOLD)
            txt_view.text = mainTopicTitleArrayList.get(i)
            txt_view.layoutParams = params_Topic

            row_main_header.addView(txt_view)

            table_exercise_attempt.addView(row_main_header)

            for(j in 0 until subContainerArrayList.size)
            {
                if(subContainerArrayList.get(j).exercise_topic.equals(mainTopicTitleArrayList.get(i), true))
                {
                    val data_row = TableRow(this)

                    // Topic Column
                    val txt_score_topic = TextView(this)
                    txt_score_topic.layoutParams = params_Topic
                    txt_score_topic.textSize = 12f
                    txt_score_topic.setTypeface(null, Typeface.BOLD)
                    txt_score_topic.text = subContainerArrayList.get(j).exercise_subtopic

                    data_row.addView(txt_score_topic)

                    // DateTime Column
                    val txt_score_datetime = TextView(this)
                    txt_score_datetime.layoutParams = params
                    txt_score_datetime.textSize = 12f
                    txt_score_datetime.setTypeface(null, Typeface.BOLD)
                    txt_score_datetime.gravity = Gravity.CENTER
                    txt_score_datetime.text = subContainerArrayList.get(j).taken_datetime

                    data_row.addView(txt_score_datetime)

                    // Percentage Column
                    val txt_score_percentage = TextView(this)
                    txt_score_percentage.layoutParams = params
                    txt_score_percentage.textSize = 12f
                    txt_score_percentage.setTypeface(null, Typeface.BOLD)
                    txt_score_percentage.gravity = Gravity.CENTER
                    txt_score_percentage.text = subContainerArrayList.get(j).percentage.toString() + "%"

                    data_row.addView(txt_score_percentage)

                    // Score Feedback Column
                    val txt_score_feedback = TextView(this)
                    txt_score_feedback.layoutParams = params
                    txt_score_feedback.textSize = 12f
                    txt_score_feedback.setTypeface(null, Typeface.BOLD)
                    txt_score_feedback.gravity = Gravity.CENTER
                    txt_score_feedback.text = subContainerArrayList.get(j).score_feedback

                    data_row.addView(txt_score_feedback)

                    table_exercise_attempt.addView(data_row)

                }

            }
        }

    }

}