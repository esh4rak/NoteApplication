package com.eshrak.noteapplication.util

import android.view.View
import com.google.android.material.snackbar.Snackbar

object SnackBarUtility {
    fun showSnackBar(view: View, message: String) {

        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackBar.setAction("OK") {
            //ToDo
        }
        snackBar.show()
    }
}
