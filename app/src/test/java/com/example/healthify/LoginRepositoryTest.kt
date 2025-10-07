@file:Suppress("UNCHECKED_CAST")

package com.example.healthify

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock

class LoginRepositoryTest {

    @Test
    fun `loginUser calls success callback on successful login`() {
        val mockAuth = mock<FirebaseAuth>()
        val mockTask = mock<Task<AuthResult>>()
        val listenerCaptor = argumentCaptor<OnCompleteListener<AuthResult>>()

        `when`(mockAuth.signInWithEmailAndPassword(anyString(), anyString()))
            .thenReturn(mockTask)

        val repo = LoginRepository(mockAuth)

        var success = false
        var failure = false

        repo.loginUser(
            "test@example.com",
            "password",
            onSuccess = { success = true },
            onFailure = { failure = true }
        )

        verify(mockTask).addOnCompleteListener(listenerCaptor.capture())
        val listener = listenerCaptor.firstValue
        `when`(mockTask.isSuccessful).thenReturn(true)

        listener.onComplete(mockTask)

        assert(success)
        assert(!failure)
    }

    @Test
    fun `loginUser calls failure callback on failed login`() {
        val mockAuth = mock<FirebaseAuth>()
        val mockTask = mock<Task<AuthResult>>()
        val listenerCaptor = argumentCaptor<OnCompleteListener<AuthResult>>()

        `when`(mockAuth.signInWithEmailAndPassword(anyString(), anyString()))
            .thenReturn(mockTask)

        val repo = LoginRepository(mockAuth)

        var success = false
        var failure = false

        repo.loginUser(
            "bad@example.com",
            "wrongpassword",
            onSuccess = { success = true },
            onFailure = { failure = true }
        )

        verify(mockTask).addOnCompleteListener(listenerCaptor.capture())
        val listener = listenerCaptor.firstValue
        `when`(mockTask.isSuccessful).thenReturn(false)

        listener.onComplete(mockTask)

        assert(failure)
        assert(!success)
    }
}
