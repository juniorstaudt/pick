package com.br.jrstaudt.pick.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.br.jrstaudt.pick.models.RequestState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId

class HomeViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    val userNameState = MutableLiveData<RequestState<String>>()

    private fun saveToken() {
        val user = FirebaseAuth.getInstance().uid
        if (user != null)
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
                db.collection("users")
                    .document(FirebaseAuth.getInstance().uid ?: "")
                    .update("token", it.token)
                    .addOnSuccessListener {}
                    .addOnFailureListener {}
            }
    }
    private fun getUser() {
        saveToken()
    }
}