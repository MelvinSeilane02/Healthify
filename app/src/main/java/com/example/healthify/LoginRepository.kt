package com.example.healthify

import com.google.firebase.auth.FirebaseAuth

class LoginRepository(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) {

    fun loginUser(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (Exception?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception)
                }
            }
    }
}
