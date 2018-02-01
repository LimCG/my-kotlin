package com.fyp.quiz

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
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

/**
 * Created by limcg on 26/01/2018.
 */
class ListExerciseScoreActivity : AppCompatActivity() {

    companion object {
        private val TAG = ListExerciseScoreActivity::class.java.simpleName
    }

    lateinit var mainTopicTitleArrayList : ArrayList<String>
    lateinit var subContainerArrayList : ArrayList<ExerciseScoreObject>

    var is_parent : Boolean = false
    lateinit var server_url : String

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

        mainTopicTitleArrayList = ArrayList<String>()
        subContainerArrayList = ArrayList<ExerciseScoreObject>()

        toolbar.title = "AKTIVITI SKOR"
        toolbar.setNavigationOnClickListener { view ->
            onBackPressed()
        }

        try {

            val jo = JSONObject()
            jo.put("user_id", MyPref.userIDPrefs)

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

                                            if(!mainTopicTitleArrayList.contains(exercise_topic))
                                            {
                                                mainTopicTitleArrayList.add(exercise_topic)
                                            }

                                            val exerciseScoreObject = ExerciseScoreObject()
                                            exerciseScoreObject.exercise_result_id = exercise_result_id
                                            exerciseScoreObject.exercise_subtopic = exercise_subtopic
                                            exerciseScoreObject.percentage = percentage
                                            exerciseScoreObject.exercise_topic = exercise_topic

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

                Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()

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
            txt_view.textSize = 16f
            txt_view.setTypeface(null, Typeface.BOLD)

            if(i == 0)
            {
                txt_view.text = " "
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

            for(u in 0 until 4)
            {
                val txt_view = TextView(this)
                txt_view.textSize = 16f
                txt_view.setTypeface(null, Typeface.BOLD)

                if( u == 0 )
                {
                    txt_view.text = mainTopicTitleArrayList.get(i)
                    txt_view.layoutParams = params_Topic
                }
                else
                {
                    txt_view.text = " "
                    txt_view.layoutParams = params_Topic
                }

                row_main_header.addView(txt_view)
            }

            table_exercise_attempt.addView(row_main_header)

            for(j in 0 until subContainerArrayList.size)
            {
                if(count == 0)
                {
                    row_content = TableRow(this)

                    val txt_topic = TextView(this)
                    txt_topic.text = "- " + subContainerArrayList.get(j).exercise_subtopic
                    txt_topic.layoutParams = params_Topic
                    txt_topic.textSize = 14f
                    txt_topic.setTypeface(null, Typeface.BOLD)
                    row_content.addView(txt_topic)

                    //increment count
                    count = count.inc()

                }

                if(subContainerArrayList.get(j).exercise_topic.equals(mainTopicTitleArrayList.get(i), true))
                {
                    val txt_score = TextView(this)
                    txt_score.textSize = 14f
                    txt_score.setTypeface(null, Typeface.BOLD)
                    txt_score.gravity = Gravity.CENTER

                    if( subContainerArrayList.get(j).percentage < 0 )
                    {
                        txt_score.text = "-"
                    }
                    else
                    {
                        txt_score.text = subContainerArrayList.get(j).percentage.toString() + " %"

                    }

                    txt_score.layoutParams = params
                    row_content!!.addView(txt_score)

                    if(count == 3)
                    {
                        count = 0

                        table_exercise_attempt.addView(row_content)
                    }
                    else
                    {
                        //increment count
                        count = count.inc()
                    }

                }

            }
        }

    }

}