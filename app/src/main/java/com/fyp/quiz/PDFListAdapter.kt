package com.fyp.quiz

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.request.StringRequest
import kotlinx.android.synthetic.main.cardview_pdf_list.view.*
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by limcg on 31/12/2017.
 */
class PDFListAdapter(val context: Context, val pdfStorage : ArrayList<PDFObject>, val isStudent : Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private val TAG = PDFListAdapter::class.java.simpleName
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.cardview_pdf_list, parent, false)

        return MyCustomView(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {

        val pdfObject = pdfStorage.get(position)

        if(holder is MyCustomView)
        {
            holder.bindItem(pdfObject)
        }

    }

    override fun getItemCount(): Int {

        return pdfStorage.size
    }

    inner class MyCustomView(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItem(pdfObject : PDFObject) {

            itemView.txt_pdf_title.text = pdfObject.pdf_title

            if( isStudent )
            {
                itemView.imageBtn_delete.visibility = View.GONE
            }
            else
            {
                itemView.imageBtn_delete.setOnClickListener { view ->

                    val PostRequst = object : StringRequest(Request.Method.POST, Constants.REMOVE_PDF,
                            Response.Listener { response ->

                                try {

                                    val JsonObj = JSONObject(response)
                                    val Status = JsonObj.getString("status")
                                    val StatusMsg = JsonObj.getString("status_msg")

                                    if( Status.equals("OK"))
                                    {
                                        // remove file success
                                        pdfStorage.remove(pdfObject)
                                        notifyDataSetChanged()

                                    }
                                    else
                                    {
                                        // Fail to delete file
                                    }

                                    //Toast.makeText(context, StatusMsg, Toast.LENGTH_LONG).show()

                                } catch (e : JSONException) {

                                    Toast.makeText(context, "ERROR", Toast.LENGTH_LONG).show()

                                }

                            }, Response.ErrorListener { error ->

                        Toast.makeText(context, "ERROR", Toast.LENGTH_LONG).show()


                    }) {
                        override fun getParams(): MutableMap<String, String> {

                            val Params = HashMap<String, String>()
                            Params.put("pdf_id", pdfObject.pdf_id.toString())
                            return Params
                        }
                    }

                    MyApp.instance!!.addToRequestQueue(PostRequst, TAG)
                }

            }

            itemView.cardview_pdf.setOnClickListener { view ->

                val Intent = Intent(context, ViewPDFActivity::class.java)
                Intent.putExtra("pdf_path", pdfObject.pdf_path)
                Intent.putExtra("pdf_title", pdfObject.pdf_title)
                context.startActivity(Intent)

            }

        }

    }

}