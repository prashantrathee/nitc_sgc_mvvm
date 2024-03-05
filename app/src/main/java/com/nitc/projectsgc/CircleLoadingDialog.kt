package com.nitc.projectsgc

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.fragment.app.FragmentActivity

interface CircleLoadingDialog {

    public fun getLoadingDialog(activity:FragmentActivity,context:Context):Dialog{
        val loadingDialog = Dialog(context)
        loadingDialog.setContentView(activity.layoutInflater.inflate(R.layout.loading_dialog,null))
        loadingDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return loadingDialog
    }

}