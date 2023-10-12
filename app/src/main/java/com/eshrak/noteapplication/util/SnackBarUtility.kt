package com.eshrak.noteapplication.util

import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar

object SnackBarUtility {
    fun showSnackBar(view: View, message: String) {

        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackBar.view.setBackgroundColor(Color.parseColor("#8692f7"))
        snackBar.show()
    }
}
