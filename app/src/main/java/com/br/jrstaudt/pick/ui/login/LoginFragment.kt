package com.br.jrstaudt.pick.ui.login

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.br.jrstaudt.pick.R
import com.br.jrstaudt.pick.exceptions.EmailInvalidException
import com.br.jrstaudt.pick.exceptions.PasswordInvalidException
import com.br.jrstaudt.pick.models.RequestState
import com.br.jrstaudt.pick.ui.base.BaseFragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.*

class LoginFragment : BaseFragment() {

    override val layout = R.layout.fragment_login
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private lateinit var tvSubTitleLogin: TextView
    private lateinit var containerLogin: LinearLayout
    private lateinit var tvResetPassword: TextView
    private lateinit var tvNewAccount: TextView

    private lateinit var btLogin: Button
    private lateinit var etEmailLogin: EditText
    private lateinit var etPasswordLogin: EditText

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater
            .from(context)
            .inflateTransition(android.R.transition.move)

        firebaseAnalytics = Firebase.analytics
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) { super.onViewCreated(view, savedInstanceState)
        setUpView(view)
        startLoginAnimation()
        registerObserver()
    }

    private fun registerObserver() {
        loginViewModel.loginState.observe(viewLifecycleOwner, Observer {
            when(it) {
                is RequestState.Success -> showSuccess()
                is RequestState.Error -> showError(it.throwable)
                is RequestState.Loading -> showLoading("Efetuando login...")
            }
        })
        this.loginViewModel.resetPasswordState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is RequestState.Success -> {
                    hideLoading()
                    showMessage(it.data) }
                is RequestState.Error -> showError(it.throwable)
                is RequestState.Loading -> showLoading("Estamos enviando um e-mail para redefinição da senha.") }
        })
    }

    private fun showSuccess() {
        hideLoading()
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_loginFragment_to_main_nav_graph)

        val firebaseAnalytics = Firebase.analytics

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN) {
            param("fez_login", 3)}

    }

    private fun showError(throwable: Throwable) {
        hideLoading()

        etEmailLogin.error = null
        etPasswordLogin.error = null

        when(throwable) {
            is EmailInvalidException -> {
                etEmailLogin.error = throwable.message
                etEmailLogin.requestFocus()
            }
            is PasswordInvalidException -> {
                etPasswordLogin.error = throwable.message
                etPasswordLogin.requestFocus()
            }
            else -> showMessage(throwable.message)
        }
    }

    private fun setUpView(view: View) {
        containerLogin = view.findViewById(R.id.containerLogin)
        tvSubTitleLogin = view.findViewById(R.id.tvSubTitleLogin)
        tvNewAccount = view.findViewById(R.id.tvNewAccount)
        tvResetPassword = view.findViewById(R.id.tvResetPassword)

        btLogin = view.findViewById(R.id.btLogin)
        etEmailLogin = view.findViewById(R.id.etEmailLogin)
        etPasswordLogin = view.findViewById(R.id.etPasswordLogin)

        btLogin.setOnClickListener {
            loginViewModel.signIn(
                etEmailLogin.text.toString(),
                etPasswordLogin.text.toString()
            )
        }
        tvResetPassword.setOnClickListener {
            loginViewModel.resetPassword(
                etEmailLogin.text.toString()
            )
        }
        tvNewAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }
    private fun startLoginAnimation() {
        val anim = AnimationUtils.loadAnimation(context, R.anim.anim_form_login)
        containerLogin.startAnimation(anim)
        tvSubTitleLogin.startAnimation(anim)
        tvNewAccount.startAnimation(anim)
        tvResetPassword.startAnimation(anim)
    }
}

