package com.fyp.quiz

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.request.JsonObjectRequest
import com.android.volley.request.SimpleMultiPartRequest
import com.android.volley.request.StringRequest
import kotlinx.android.synthetic.main.activity_upload_pdf.*
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by limcg on 27/12/2017.
 */
class UploadPDFActivity : AppCompatActivity() {

    lateinit var filePath : String
    lateinit var selectedTopic : String
    var selectedTopicID : Int = 0
    lateinit var selectedSubTopic : String

    lateinit var arrayListTopic : ArrayList<String>
    lateinit var arrayListTopicID : ArrayList<Int>
    lateinit var arrayListSubTopic : ArrayList<String>
    lateinit var arrayListSubTopicID : ArrayList<Int>

    companion object {

        private val TAG = UploadPDFActivity::class.java.simpleName

        private val PICKFILE_RESULT_CODE = 1
        private val READ_EXTERNAL_STORAGE_REQUEST_CODE = 20

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_pdf)

        arrayListTopic = ArrayList<String>()
        arrayListTopicID = ArrayList<Int>()
        arrayListSubTopic = ArrayList<String>()
        arrayListSubTopicID = ArrayList<Int>()

        toolbar.title = this.resources.getString(R.string.activity_upload_toolbar_title)
        toolbar.setNavigationOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {

                onBackPressed()
            }

        })

        val pDialog = ProgressDialog(this)
        pDialog.setMessage("Loading....")
        pDialog.setCancelable(false)
        pDialog.isIndeterminate = true
        pDialog.show()

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
                            spinner_topic_upload.adapter = arrayAdapter

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

                    pDialog.dismiss()

                }, Response.ErrorListener { error ->


            pDialog.dismiss()

            Toast.makeText(this, "Error Loading.", Toast.LENGTH_LONG).show()

            finish()

        })

        MyApp.instance!!.addToRequestQueue(JsonRequest, TAG)

        spinner_topic_upload.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, p3: Long) {

                // Clean up Subtopic String container every time selected main topic
                if( arrayListSubTopic.size > 0 )
                {
                    arrayListSubTopic.clear()
                }

                selectedTopicID = arrayListTopicID.get(position)
                selectedTopic = arrayListTopic.get(position)

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

                                    val arrayAdapter = ArrayAdapter<String>(view!!.context,
                                            android.R.layout.simple_dropdown_item_1line, arrayListSubTopic)
                                    spinner_subtopic_upload.adapter = arrayAdapter

                                }
                                else
                                {
                                    // Result error
                                }

                            } catch (e : JSONException) {

                                // Json Exception
                            }

                        }, Response.ErrorListener { error ->

                    Log.i(TAG, error.toString());

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

        spinner_subtopic_upload.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                selectedSubTopic = parent!!.getItemAtPosition(position).toString()

            }


        }


        // Pick a file
        txt_upload_file.textSize = 18F
        txt_upload_file.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {

                if( checkPermission() )
                {
                    val intent = Intent(Intent.ACTION_GET_CONTENT)
                    intent.type = "application/pdf"
                    startActivityForResult(intent, PICKFILE_RESULT_CODE)
                }

            }

        })

        // Upload file
        btn_upload_now.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {

                val txt_file_title : String? = edt_upload_file_title.text.toString().trim()

                if( txt_file_title != null && txt_file_title.length > 0 )
                {

                    uploadPDF(filePath, selectedTopic, selectedSubTopic, txt_file_title)
                }
                else
                {
                    Toast.makeText(this@UploadPDFActivity, "Field is required/File not found", Toast.LENGTH_LONG).show()
                }
            }

        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode)
        {
            PICKFILE_RESULT_CODE -> {

                if(resultCode == Activity.RESULT_OK) {

                    filePath = RealPathUtil.getRealPath(this, data!!.data)

                    txt_upload_file.text = resources.getText(R.string.txt_file_selected)

                }

            }

            else -> {

                super.onActivityResult(requestCode, resultCode, data)

            }

        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if( requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE )
        {
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED )
            {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "application/pdf"
                startActivityForResult(intent, PICKFILE_RESULT_CODE)

            }
        }
    }

    private fun uploadPDF(filePath : String, main_topic : String, subtopic : String, file_title : String) {

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading....")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val SMPR = SimpleMultiPartRequest(Request.Method.POST,
                Constants.UPLOAD_PDF_URL, Response.Listener { response ->

            try {

                val jsonObj = JSONObject(response)
                val Status = jsonObj.getString("status")
                val Message = jsonObj.getString("message")

                if( Status.equals("OK") )
                {
                    // Upload success
                    finish()

                }

                Toast.makeText(this, Message, Toast.LENGTH_LONG).show()


            } catch (e : JSONException) {

                Toast.makeText(this, "Fail to Upload", Toast.LENGTH_LONG).show()

            }

            progressDialog.dismiss()

        }, Response.ErrorListener { volleyError ->

            progressDialog.dismiss()

            Toast.makeText(this, "Fail to Upload", Toast.LENGTH_LONG).show()

        })

        SMPR.addFile("pdf_file", filePath)
        SMPR.addStringParam("main_topic", main_topic)
        SMPR.addStringParam("sub_topic", subtopic)
        SMPR.addStringParam("file_title", file_title)
        MyApp.instance!!.addToRequestQueue(SMPR)

    }

    private fun checkPermission() : Boolean
    {
        if( Build.VERSION.SDK_INT >= 23 )
        {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                //Permission is granted

                return true;

            } else {

                //Permission is revoked

                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        READ_EXTERNAL_STORAGE_REQUEST_CODE);

                return false;
            }
        }
        else
        {
            // permission is granted
            return true
        }
    }

}