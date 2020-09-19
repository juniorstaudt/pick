package com.br.jrstaudt.pick.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.br.jrstaudt.pick.R
import com.br.jrstaudt.pick.ui.base.BaseFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

class HomeFragment : BaseFragment() {

    override val layout = R.layout.fragment_home
    private lateinit var btCreateClass: Button
    private lateinit var btAbout: Button
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val firebaseAnalytics = Firebase.analytics

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN) {
            param("acessou_home", 1)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView(view)
    }

    private fun setUpView(view: View) {

        btCreateClass = view.findViewById(R.id.btCreateClass)
        btAbout = view.findViewById(R.id.btAbout)

        setUpListener()

    }

    private fun setUpListener() {
        btCreateClass.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_homeFragment_to_musicClassFragment)
        }

        btAbout.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_homeFragment_to_aboutFragment)
        }
    }
}

