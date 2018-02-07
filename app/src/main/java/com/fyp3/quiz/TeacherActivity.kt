package com.fyp3.quiz

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import android.content.Intent
import kotlinx.android.synthetic.main.activity_teacher.*
import kotlinx.android.synthetic.main.content_teacher.*

class TeacherActivity : AppCompatActivity() {

    companion object {

        private val TAG = TeacherActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)

        toolbar.title = "Teacher Control Panel"

        btn_list_pdf.setOnClickListener { view ->

            val intent = Intent(this, ListPDFActivity::class.java)
            startActivity(intent)

        }

        btn_view_score.setOnClickListener { view ->

            val intent = Intent(this, ListStudentActivity::class.java)
            startActivity(intent)
        }

    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
}
