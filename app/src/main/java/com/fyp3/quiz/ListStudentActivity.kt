package com.fyp3.quiz

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.request.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_list_student.*

class ListStudentActivity : AppCompatActivity() {

    companion object {
        private val TAG = ListStudentActivity::class.java.simpleName
    }

    lateinit var studentArrayList : ArrayList<StudentObject>
    lateinit var studentFullNameList : ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_student)

        studentArrayList = ArrayList<StudentObject>()
        studentFullNameList = ArrayList<String>()

        toolbar.title = "Student Listing"
        toolbar.setNavigationOnClickListener { view ->
            onBackPressed()
        }

        val studentListRequest = JsonObjectRequest(Request.Method.POST, Constants.LIST_STUDENT_URL, null,
                Response.Listener { response ->

                    if(response.getString("status").equals("OK", true))
                    {
                        val dataArray = response.getJSONArray("data")

                        for(i in 0 until dataArray.length())
                        {
                            val studentObj = StudentObject()

                            val student_id = dataArray.getJSONObject(i).getString("user_id")
                            val student_full_name = dataArray.getJSONObject(i).getString("full_name")

                            studentObj.student_full_name = student_full_name
                            studentObj.student_id = student_id

                            studentArrayList.add(studentObj)
                            studentFullNameList.add(student_full_name)

                        }

                        val arrayAdapter = ArrayAdapter<String>(this@ListStudentActivity,
                                android.R.layout.simple_expandable_list_item_1, studentFullNameList)
                        listview_list_student.adapter = arrayAdapter
                        listview_list_student.setOnItemClickListener(object : AdapterView.OnItemClickListener {

                            override fun onItemClick(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                                val student_id = studentArrayList.get(position).student_id

                                val intent = Intent(this@ListStudentActivity, ListFinalScoreActivity::class.java)
                                intent.putExtra("user_id", student_id)

                                startActivity(intent)

                            }

                        })

                    }
                    else
                    {
                        Toast.makeText(this, "No have records", Toast.LENGTH_LONG).show()
                    }



                }, Response.ErrorListener { error ->

            Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show()

        })

        MyApp.instance!!.addToRequestQueue(studentListRequest, TAG)

    }
}