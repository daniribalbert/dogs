package com.daniribalbert.autodogs.utils.extensions

import android.content.Context
import android.widget.Toast

fun showShortToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}