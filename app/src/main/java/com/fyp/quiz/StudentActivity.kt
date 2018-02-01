package com.fyp.quiz

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.request.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_student.*
import org.json.JSONException

/**
 * Created by limcg on 02/01/2018.
 */
class StudentActivity : AppCompatActivity() {

    companion object {
        private val TAG = StudentActivity::class.java.simpleName
    }

    var alertDialog : AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        toolbar.title = resources.getText(R.string.student_title)
        toolbar.setNavigationOnClickListener { view ->

            onBackPressed()
        }

        btn_study_note.setOnClickListener { view ->

            startActivity(Intent(this, StudyNoteActivity::class.java))
        }

        btn_exercise.setOnClickListener { view ->

            startActivity(Intent(this, ChooseTopicExerciseActivity::class.java))

        }

        btn_trial.setOnClickListener { view ->

            startActivity(Intent(this, ChooseTopicTrialActivity::class.java))
        }

        btn_final.setOnClickListener { view ->

            startActivity(Intent(this, FinalActivity::class.java))

        }

        btn_view_exercise_score.setOnClickListener { view ->

            startActivity(Intent(this, ListExerciseScoreActivity::class.java))
        }

        btn_view_trial_score.setOnClickListener { view ->

            startActivity(Intent(this, ScoreActivity::class.java))

        }

        btn_view_final_score.setOnClickListener{ view ->

            startActivity(Intent(this, ListFinalScoreActivity::class.java))

//            val alertDialog = AlertDialog.Builder(this)
//            alertDialog.setTitle("Final Exam Score")
//            alertDialog.setCancelable(false)
//
//            val finalScoreRequest = JsonObjectRequest(Request.Method.POST, Constants.LIST_FINAL_SCORE, null,
//                    Response.Listener { response ->
//
//                        try {
//
//                            if( response.getString("status").equals("OK", true))
//                            {
//                                val datetime = response.getString("datetime")
//                                val percentage = response.getString("percentage")
//
//                                alertDialog.setMessage("Your final exam score: " + percentage + " %\nTaken at " + datetime)
//
//                            }
//                            else
//                            {
//                                alertDialog.setMessage("No result. You haven't take final exam.")
//                            }
//
//
//                        } catch (e : JSONException) {
//
//                            alertDialog.setMessage("Network Error ....")
//                        }
//
//                        alertDialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
//
//                            dialogInterface.dismiss()
//
//                        })
//                        this.alertDialog = alertDialog.create()
//                        alertDialog.show()
//
//                    }, Response.ErrorListener { error ->
//
//                            alertDialog.setMessage("Network Error ....")
//                            alertDialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
//                            dialogInterface.dismiss()
//
//                })
//                this.alertDialog = alertDialog.create()
//                alertDialog.show()
//
//
//
//            })
//
//
//            MyApp.instance!!.addToRequestQueue(finalScoreRequest, TAG)
//
        }

    }

    override fun onPause() {
        super.onPause()
        alertDialog?.dismiss()
    }
}