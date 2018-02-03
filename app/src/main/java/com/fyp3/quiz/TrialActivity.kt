package com.fyp3.quiz

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.request.StringRequest
import kotlinx.android.synthetic.main.activity_trial.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by limcg on 02/01/2018.
 */
class TrialActivity : AppCompatActivity(), QuestionFragment.OnResultOptionListener {

    companion object {

        private val TAG = TrialActivity::class.java.simpleName
    }

    var ques_id : Int = 0
    lateinit var ques_answer : String
    var alertDialog : AlertDialog? = null

    lateinit var selectedTopic : String
    var selectedTopicID : Int = 0

    var position : Int = 0

    override fun onResultOption(ques_id: Int, ques_answer: String) {

        this.ques_id = ques_id
        this.ques_answer = ques_answer
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trial)

        if( intent.hasExtra("intent_topic"))
        {
            selectedTopic = intent.getStringExtra("intent_topic")

        }

        if(intent.hasExtra("intent_topic_id"))
        {
            selectedTopicID = intent.getIntExtra("intent_topic_id", 0)
        }

        val questionArrayList = ArrayList<QuesObject>()
        val questionAnswerJsonArray = JSONArray()

        toolbar.title = "Trial Exam"
        toolbar.setNavigationOnClickListener { view ->

            onBackPressed()
        }

        val stringRequest = object : StringRequest(Request.Method.POST, Constants.GET_TRIAL_QUESTION_URL,
                Response.Listener { response ->

                    try {

                        val JsonObj = JSONObject(response)

                        if( JsonObj.getString("status").equals("OK", true) )
                        {
                            val Data = JsonObj.getJSONArray("data")
                            if( Data.length() > 0 )
                            {
                                for(i in 0 until Data.length())
                                {
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

                                    questionArrayList.add(Ques)

                                }

                                // Initial Fragment
                                val ft = supportFragmentManager.beginTransaction()
                                ft.replace(R.id.framelayout_trial,
                                        QuestionFragment.newInstance(position, questionAnswerJsonArray.toString(),
                                                questionArrayList.get(position)))
                                ft.commit()
                                // END of Initial Fragment //

                            }
                            else
                            {
                                // Data array zero, No have question
                                val alertDialog = AlertDialog.Builder(this)
                                alertDialog.setTitle("Alert")
                                alertDialog.setMessage("No have question at the moment.")
                                alertDialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->

                                    dialogInterface.dismiss()

                                    finish()

                                })
                                this.alertDialog = alertDialog.create()
                                alertDialog.show()

                            }

                        }
                        else
                        {
                            // No Question
                            val alertDialog = AlertDialog.Builder(this)
                            alertDialog.setTitle("Alert")
                            alertDialog.setMessage("No have question at the moment.")
                            alertDialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->

                                dialogInterface.dismiss()

                                finish()

                            })
                            this.alertDialog = alertDialog.create()
                            alertDialog.show()
                        }

                    } catch (e : JSONException) {

                        Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show()
                        finish()
                    }

                }, Response.ErrorListener { error ->

            // Server response ERROR
            Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show()
            finish()
        }) {

            override fun getParams(): MutableMap<String, String> {

                val param = HashMap<String, String>()
                param.put("selected_topic_id", selectedTopicID.toString())

                return param
            }
        }

        MyApp.instance!!.addToRequestQueue(stringRequest, TAG)

        if( position == 0 )
        {
            btn_pre.visibility = View.GONE
        }
        else
        {
            btn_pre.visibility = View.VISIBLE
        }

        btn_pre.setOnClickListener { view ->

            // Saving answer
            val createJsonObj = JSONObject()
            createJsonObj.put("ans_ques_id", ques_id)
            createJsonObj.put("ans_ques_answer", ques_answer)
            questionAnswerJsonArray.put(position, createJsonObj)

            position = position.dec()

            // first question previous button hide
            if( position == 0 )
            {
                btn_pre.visibility = View.GONE
                btn_next.text = "Next"
            }

            val ft = supportFragmentManager.beginTransaction()
            ft.replace(R.id.framelayout_trial,
                    QuestionFragment.newInstance(position, questionAnswerJsonArray.toString(),
                            questionArrayList.get(position)))
            ft.commit()

        }

        btn_next.setOnClickListener { view ->

            val createJsonObj = JSONObject()
            createJsonObj.put("ans_ques_id", ques_id)
            createJsonObj.put("ans_ques_answer", ques_answer)
            questionAnswerJsonArray.put(position, createJsonObj)

            // Still have more question
            if( questionArrayList.size - 1 > position )
            {
                position = position.inc()

                val ft = supportFragmentManager.beginTransaction()
                ft.replace(R.id.framelayout_trial,
                        QuestionFragment.newInstance(position, questionAnswerJsonArray.toString(),
                                questionArrayList.get(position)))
                ft.commit()

                // hit last question button text change to Submit
                if( position == questionArrayList.size - 1)
                {
                    btn_next.text = "Submit"
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
                alertDialog.setTitle("Submit Answer")
                alertDialog.setMessage("Submit all the answer now?")
                alertDialog.setPositiveButton("Submit", DialogInterface.OnClickListener { dialogInterface, i ->

                    dialogInterface.dismiss()

                    val intent = Intent(this@TrialActivity, AnswerSheetActivity::class.java)
                    intent.putExtra("answer", questionAnswerJsonArray.toString())
                    intent.putExtra("selectedTopic", selectedTopic)
                    intent.putExtra("selectedTopicID", selectedTopicID)
                    intent.putExtra("exam_type", "trial")
                    startActivity(intent)
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

    override fun onBackPressed() {

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Alert")
        alertDialog.setMessage("Quit Trial Exam?")
        alertDialog.setPositiveButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->

            dialogInterface.dismiss()

        })
        alertDialog.setNegativeButton("Quit", DialogInterface.OnClickListener { dialogInterface, i ->

            dialogInterface.dismiss()

            finish()

        })
        this.alertDialog = alertDialog.create()
        alertDialog.show()

    }
}