package com.daniribalbert.autodogs.ui.main

import android.os.Bundle
import com.daniribalbert.autodogs.R
import com.daniribalbert.autodogs.ui.base.BaseActivity

class MainActivity : BaseActivity(), MainFragment.FragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun onShowFullScreenImage(imageUrl: String) {
        FullScreenImageDialogFragment.newInstance(imageUrl)
            .show(supportFragmentManager, FullScreenImageDialogFragment.TAG)
    }

}
