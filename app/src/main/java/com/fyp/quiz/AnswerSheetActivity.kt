package com.fyp.quiz

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.request.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_answer_sheet.*
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONArray


/**
 * Created by limcg on 08/01/2018.
 */
class AnswerSheetActivity : AppCompatActivity() {

    companion object {

        private val TAG = AnswerSheetActivity::class.java.simpleName
    }

    lateinit var questionAnswerJsonArray : String
    lateinit var questionArrayList : ArrayList<QuesObject>

    var alertDialog : AlertDialog? = null

    var exam_type : String = ""

    var selectedTopic : String = ""
    var selectedTopicID : Int = 0

    var selectedSubTopic : String = ""
    var selectedSubTopicID : Int = 0

    var position : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer_sheet)

        questionArrayList = ArrayList<QuesObject>()

        toolbar.title = "Score & Answer Sheet"
        toolbar.setNavigationOnClickListener { view ->
            onBackPressed()
        }

        if(intent.hasExtra("answer"))
        {
            questionAnswerJsonArray = intent.getStringExtra("answer")
        }

        if(intent.hasExtra("selectedTopic"))
        {
            selectedTopic = intent.getStringExtra("selectedTopic")
        }

        if(intent.hasExtra("selectedTopicID"))
        {
            selectedTopicID = intent.getIntExtra("selectedTopicID", 0)
        }

        if( intent.hasExtra("selectedSubTopic"))
        {
            selectedSubTopic = intent.getStringExtra("selectedSubTopic")

        }

        if(intent.hasExtra("selectedSubTopicID"))
        {
            selectedSubTopicID = intent.getIntExtra("selectedSubTopicID", 0)
        }


        if(intent.hasExtra("exam_type"))
        {
            exam_type = intent.getStringExtra("exam_type")
        }

        try {

            val jo = JSONObject()
            val ja = JSONArray(questionAnswerJsonArray)
            jo.put("answer_data", ja)
            jo.put("selectedTopic", selectedTopic)
            jo.put("selectedTopicID", selectedTopicID)
            jo.put("selectedSubTopic", selectedSubTopic)
            jo.put("selectedSubTopicID", selectedSubTopicID)
            jo.put("exam_type", exam_type)
            jo.put("user_id", MyPref.userIDPrefs)

            val JsonRequest = JsonObjectRequest(Request.Method.POST, Constants.CHECK_ANSWER_URL,
                    jo, Response.Listener { response ->

               try {

                   val percentage = response.getString("percentage")
                   val total_ques = response.getInt("total_question")
                   val correct_count = response.getInt("correct_count")

                   txt_answer_sheet_score.text = "Score: " + correct_count + " / " + total_ques + " = " + percentage

                   val Data = response.getJSONArray("data")
                   if( Data.length() > 0 ) {

                       for (i in 0 until Data.length()) {

                           val Ques = QuesObject()

                           val obj = Data.getJSONObject(i)
                           Ques.ques_id = obj.getInt("ques_id")
                           Ques.ques_topic = obj.getString("ques_topic")
                           Ques.ques_subTopic = obj.getString("ques_subtopic")
                           Ques.ques_content = obj.getString("ques_content")
                           Ques.ques_title = obj.getString("ques_title")
                           Ques.option_a = obj.getString("opt_a")
                           Ques.option_b = obj.getString("opt_b")
                           Ques.option_c = obj.getString("opt_c")
                           Ques.option_d = obj.getString("opt_d")

                           if(obj.getInt("ques_type") == 0)
                           {
                               Ques.isObjective = true
                               Ques.objective_answer = obj.getString("objective_ans")
                           }
                           else
                           {
                               Ques.isObjective = false
                               Ques.subjective_answer = obj.getString("subjective_ans")
                           }

                           Ques.isCorrectAnswer = obj.getInt("isCorrectAnswer")
                           if( Ques.isCorrectAnswer == 0 )
                           {
                               Ques.your_answer = obj.getString("your_answer")
                           }

                           questionArrayList.add(i, Ques)

                       }

                       // Initial Fragment
                       val ft = supportFragmentManager.beginTransaction()
                       ft.replace(R.id.framelayout_answer_sheet,
                               AnswerSheetFragment.newInstance(position, questionArrayList.get(position)))
                       ft.commit()
                       // END of Initial Fragment //


                   }

               } catch (e : JSONException) {

                   //
               }

            }, Response.ErrorListener { error ->

                Log.i(TAG, error.toString())
            })

            MyApp.instance!!.addToRequestQueue(JsonRequest, TAG)

            if( position == 0 )
            {
                btn_pre.visibility = View.GONE
            }
            else
            {
                btn_pre.visibility = View.VISIBLE
            }

        } catch (e: JSONException) {

            Log.i(TAG, e.toString())
        }

        btn_pre.setOnClickListener { view ->

            position = position.dec()

            // first question previous button hide
            if( position == 0 )
            {
                btn_pre.visibility = View.GONE
                btn_next.text = resources.getText(R.string.next)
            }
            else
            {
                btn_next.text = resources.getText(R.string.next)
            }

            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.framelayout_answer_sheet,
                    AnswerSheetFragment.newInstance(position, questionArrayList.get(position)))
            ft.commit()

        }

        btn_next.setOnClickListener { view ->

            // Still have more question
            if( questionArrayList.size - 1 > position )
            {
                position = position.inc()

                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.framelayout_answer_sheet,
                        AnswerSheetFragment.newInstance(position, questionArrayList.get(position)))
                ft.commit()

                // hit last question button text change to Submit
                if( position == questionArrayList.size - 1)
                {
                    btn_next.text = "Finish"
                }

                // Second question onward previous button enable
                if( position > 0 )
                {
                    btn_pre.visibility = View.VISIBLE
                }


            }
            else
            {
                // Submit Answer to server
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle(resources.getString(R.string.alert))
                alertDialog.setMessage("Quit Answer Sheet?")
                alertDialog.setPositiveButton("Quit", DialogInterface.OnClickListener { dialogInterface, i ->

                    dialogInterface.dismiss()

                    finish()

                })
                alertDialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->

                    dialogInterface.dismiss()


                })
                this.alertDialog = alertDialog.create()
                alertDialog.show()

            }

        }

    }

    override fun onPause() {
        super.onPause()
        this.alertDialog?.dismiss()
    }
}