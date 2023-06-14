package com.multiplexer.cricapp.utlis

import android.content.Context
import com.devhoony.lottieproegressdialog.LottieProgressDialog

@Suppress("DEPRECATION")
class ProgressBar {
    object ProgressBarSingleton {
        fun showProgressBar(
            context: Context,
            title: String? = null,
        ): LottieProgressDialog {
            val progressDialog = LottieProgressDialog(
                context, false, null, null, null, null, LottieProgressDialog.SAMPLE_6, title, null
            )
            progressDialog.show()
            return progressDialog
        }

        fun hideProgressBar(progressDialog: LottieProgressDialog) {
            progressDialog.dismiss()
        }
    }

}