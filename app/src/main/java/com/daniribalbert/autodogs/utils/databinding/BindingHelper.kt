package com.daniribalbert.autodogs.utils.databinding

import android.view.View
import androidx.databinding.BindingAdapter

class BindingHelper {

    companion object {
        @BindingAdapter("android:visibility")
        @JvmStatic
        fun setVisibility(view: View, visible: Boolean) {
            view.visibility = if (visible) View.VISIBLE else View.GONE
        }
    }
}
