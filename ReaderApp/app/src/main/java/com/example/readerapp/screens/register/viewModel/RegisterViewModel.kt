package com.example.readerapp.screens.register.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readerapp.model.MUser
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(): ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun createUserWithEmailAndPassword(email: String, password: String, home: () -> Unit){
        viewModelScope.launch {
            if(_loading.value == false){
                _loading.value = true
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val displayName = task.result?.user?.email?.split('@')?.get(0)
                        createUser(displayName)
                        home()
                    }else{

                    }
                    _loading.value = false
                }
            }
        }
    }

    fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid

        if (displayName != null && userId != null) {

            val user = MUser(
                id = null,
                userId = userId,
                displayName = displayName,
                avatarUrl = "",
                quote = "life is great",
                profession = "android developer"
            ).toMap()

            FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .set(user)
                .addOnSuccessListener {
                    Log.d("Firestore", "Kullanıcı başarıyla kaydedildi.")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Kullanıcı kaydedilemedi: ${e.message}")
                }
        } else {
            Log.e("Firestore", "createUser: UID veya displayName null.")
        }
    }

}