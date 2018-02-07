package com.fyp3.quiz

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_parent.*

class ParentActivity : AppCompatActivity() {

    companion object {
        private val TAG = ListTrialScoreActivity::class.java.simpleName
    }

    lateinit var topicTitleArrayList : ArrayList<String>
    lateinit var scoreObjectArrayList : ArrayList<ScoreObject>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent)

        topicTitleArrayList = ArrayList<String>()
        scoreObjectArrayList = ArrayList<ScoreObject>()

        toolbar.title = "My Child's Score"
        toolbar.setNavigationOnClickListener { view ->

            onBackPressed()
        }

        btn_view_child_activity_score.setOnClickListener { view ->

            val intent = Intent(this, ListExerciseScoreActivity::class.java)
            intent.putExtra("is_parent", true)

            startActivity(intent)

        }

        btn_view_child_trial_score.setOnClickListener { view ->

            val intent = Intent(this, ListTrialScoreActivity::class.java)
            intent.putExtra("is_parent", true)

            startActivity(intent)

        }

        btn_view_child_final_score.setOnClickListener { view ->

            val intent = Intent(this, ListFinalScoreActivity::class.java)
            intent.putExtra("is_parent", true)

            startActivity(intent)

        }

    }

}