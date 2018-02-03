package com.fyp3.quiz

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_view_pdf.*

/**
 * Created by limcg on 31/12/2017.
 */
class ViewPDFActivity : AppCompatActivity() {

    lateinit var pdf_path : String
    lateinit var pdf_title : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pdf)

        if( intent.hasExtra("pdf_title"))
        {
            pdf_title = intent.getStringExtra("pdf_title")
        }

        if( intent.hasExtra("pdf_path") )
        {
            pdf_path = intent.getStringExtra("pdf_path")
        }

        toolbar.title = pdf_title
        toolbar.setNavigationOnClickListener { view ->

            onBackPressed()
        }

        webview_pdf.settings.javaScriptEnabled = true
        webview_pdf.loadUrl("http://docs.google.com/gview?embedded=true&url=" + Constants.BASE_URL + pdf_path)
        webview_pdf.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressbar_webview_pdf.visibility = View.GONE
            }
        }

    }

}