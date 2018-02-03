package com.fyp3.quiz

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.request.JsonObjectRequest
import com.android.volley.request.StringRequest
import kotlinx.android.synthetic.main.activity_list_pdf.*
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by limcg on 30/12/2017.
 */
class ListPDFActivity : AppCompatActivity() {

    companion object {
        private val TAG = ListPDFActivity::class.java.simpleName
    }

    lateinit var arrayListPDFObject : ArrayList<PDFObject>

    lateinit var arrayListTopic : ArrayList<String>
    lateinit var arrayListTopicID : ArrayList<Int>
    lateinit var arrayListSubTopic : ArrayList<String>
    lateinit var arrayListSubTopicID : ArrayList<Int>

    lateinit var selectedTopic : String
    var selectedTopicID : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_pdf)

        arrayListTopic = ArrayList<String>()
        arrayListTopicID = ArrayList<Int>()
        arrayListSubTopic = ArrayList<String>()
        arrayListSubTopicID = ArrayList<Int>()

        arrayListPDFObject = ArrayList<PDFObject>()
        setupRecyclerView()

        toolbar.title = "Manage Notes"
        toolbar.setNavigationOnClickListener { view ->

            onBackPressed()
        }

        val pDialog = Utils.showProgressDialog(this, resources.getString(R.string.processing))

        val JsonRequest = JsonObjectRequest(Request.Method.POST, Constants.GET_TOPIC_LIST, null,
                Response.Listener { response ->

                    try {

                        if( response.getString("status").equals("OK", true) )
                        {

                            val dataJsonArray = response.getJSONArray("data")
                            for(i in 0 until dataJsonArray.length())
                            {
                                val topic_id = dataJsonArray.getJSONObject(i).getInt("topic_id")
                                arrayListTopicID.add(i, topic_id)

                                val topic = dataJsonArray.getJSONObject(i).getString("topic")
                                arrayListTopic.add(i, topic)
                            }

                            val arrayAdapter = ArrayAdapter<String>(this,
                                    android.R.layout.simple_dropdown_item_1line, arrayListTopic)
                            spinner_pdf_topic.adapter = arrayAdapter

                        }
                        else
                        {
                            Toast.makeText(this, "Error Loading.", Toast.LENGTH_LONG).show()

                            finish()
                        }

                    } catch (e : JSONException) {

                        Toast.makeText(this, "Error Loading.", Toast.LENGTH_LONG).show()

                        finish()
                    }

                    Utils.dismissProgressDialog(pDialog)

                }, Response.ErrorListener { error ->


                Utils.dismissProgressDialog(pDialog)

                Toast.makeText(this, "Error Loading.", Toast.LENGTH_LONG).show()

                finish()

        })

        MyApp.instance!!.addToRequestQueue(JsonRequest, TAG)

        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayListTopic)
        spinner_pdf_topic.adapter = arrayAdapter
        spinner_pdf_topic.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                // Clean up Subtopic String container every time selected main topic
                if( arrayListSubTopic.size > 0 )
                {
                    arrayListSubTopic.clear()
                }

                selectedTopic = arrayListTopic.get(position)
                selectedTopicID = arrayListTopicID.get(position)

                val stringRequest = object : StringRequest(Request.Method.POST, Constants.GET_SUBTOPIC_LIST,
                        Response.Listener { response ->

                            try {

                                val jsonObj = JSONObject(response)

                                if( jsonObj.getString("status").equals("OK", true))
                                {
                                    val dataArray = jsonObj.getJSONArray("data")
                                    for(i in 0 until dataArray.length())
                                    {
                                        val subtopic_id = dataArray.getJSONObject(i).getInt("subtopic_id")
                                        arrayListSubTopicID.add(i, subtopic_id)

                                        val subtopic_name = dataArray.getJSONObject(i).getString("subtopic_name")
                                        arrayListSubTopic.add(i, subtopic_name)
                                    }

                                    val arrayAdapterSubTopic = ArrayAdapter<String>(view!!.context,
                                            android.R.layout.simple_dropdown_item_1line, arrayListSubTopic)
                                    spinner_pdf_subtopic.adapter = arrayAdapterSubTopic

                                }
                                else
                                {
                                    // Result error
                                }

                            } catch (e : JSONException) {

                                // Json Exception
                            }

                        }, Response.ErrorListener { error ->


                    // Error response


                }) {
                    override fun getParams(): MutableMap<String, String> {

                        val params = HashMap<String, String>()
                        params.put("topic_id", selectedTopicID.toString())

                        return params
                    }
                }

                MyApp.instance!!.addToRequestQueue(stringRequest, TAG)

            }

        }


        spinner_pdf_subtopic.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                //
            }

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                if( arrayListPDFObject.size > 0 )
                {
                    arrayListPDFObject.clear()
                }

                val pDialog2 = Utils.showProgressDialog(this@ListPDFActivity, resources.getString(R.string.processing))

                val PostRequest = object : StringRequest(Request.Method.POST, Constants.GET_PDF_LIST,
                        Response.Listener { response ->

                            Utils.dismissProgressDialog(pDialog2)

                    try {

                        val JsonObj = JSONObject(response)
                        val Status = JsonObj.getString("status")
                        val StatusMsg = JsonObj.getString("status_msg")

                        if(Status.equals("OK", true))
                        {
                            val JsonArrayData = JsonObj.getJSONArray("data")
                            for(i in 0 until JsonArrayData.length())
                            {
                                val PdfObj = PDFObject()
                                val note_id = JsonArrayData.getJSONObject(i).getInt("note_id")
                                PdfObj.pdf_id = note_id

                                val note_topic = JsonArrayData.getJSONObject(i).getString("note_topic")
                                PdfObj.pdf_topic = note_topic

                                val note_title = JsonArrayData.getJSONObject(i).getString("note_title")
                                PdfObj.pdf_title = note_title

                                val note_path = JsonArrayData.getJSONObject(i).getString("note_path")
                                PdfObj.pdf_path = note_path

                                arrayListPDFObject.add(PdfObj)

                            }
                        }

                        Toast.makeText(this@ListPDFActivity, StatusMsg, Toast.LENGTH_LONG).show()

                        setDataRecyclerView()

                    } catch (e : JSONException) {

                        // Server Error

                    }

                }, Response.ErrorListener { error ->

                    // Server error
                    Utils.dismissProgressDialog(pDialog2)

                }) {

                    override fun getParams(): MutableMap<String, String> {

                        val params = HashMap<String, String>()
                        params.put("note_subtopic", parent.getItemAtPosition(position).toString())

                        return params
                    }

                }

                MyApp.instance!!.addToRequestQueue(PostRequest, TAG)

            }

        }

    }

    private fun setupRecyclerView()
    {
        val linearLayoutManager = LinearLayoutManager(this)
        val articleAdapter = PDFListAdapter(this, arrayListPDFObject, false)
        recyclerview_list_pdf.layoutManager = linearLayoutManager
        recyclerview_list_pdf.adapter = articleAdapter

    }

    private fun setDataRecyclerView()
    {
        val articleAdapter = PDFListAdapter(this, arrayListPDFObject, false)
        recyclerview_list_pdf.adapter = articleAdapter

    }

}