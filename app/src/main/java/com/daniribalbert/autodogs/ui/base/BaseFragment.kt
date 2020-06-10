package com.daniribalbert.autodogs.ui.base

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.daniribalbert.autodogs.R
import com.daniribalbert.autodogs.network.model.ApiError

abstract class BaseFragment : Fragment() {

    fun handleError(error: ApiError) {
        val ctx = activity ?: return
        Toast.makeText(
            ctx, error.errorMsg ?: getString(R.string.error_generic_msg), Toast.LENGTH_SHORT
        ).show()

    }
}