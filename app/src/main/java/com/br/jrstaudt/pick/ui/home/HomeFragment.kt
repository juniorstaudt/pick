package com.br.jrstaudt.pick.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.NavHostFragment
import com.br.jrstaudt.pick.R
import com.br.jrstaudt.pick.ui.base.BaseFragment

class HomeFragment : BaseFragment() {

    override val layout = R.layout.fragment_home
    private lateinit var btCreateClass: Button
    private lateinit var btAbout: Button

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

