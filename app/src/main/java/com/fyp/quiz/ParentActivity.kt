package com.fyp.quiz

import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_parent.*
import org.json.JSONException

/**
 * Created by limcg on 16/01/2018.
 */
class ParentActivity : AppCompatActivity() {

    companion object {
        private val TAG = ScoreActivity::class.java.simpleName
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

            val intent = Intent(this, ScoreActivity::class.java)
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