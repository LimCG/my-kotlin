package com.fyp3.quiz

import android.app.ProgressDialog
import android.content.Context

class Utils {

    companion object {

        fun showProgressDialog(context : Context, msg : String) : ProgressDialog
        {
            val progressDialog = ProgressDialog(context)
            progressDialog.setMessage(msg)
            progressDialog.setCancelable(false)
            progressDialog.show()

            return progressDialog

        }

        fun dismissProgressDialog(progressDialog : ProgressDialog)
        {
            if( progressDialog.isShowing)
            {
                progressDialog.dismiss()
            }
        }

    }
}