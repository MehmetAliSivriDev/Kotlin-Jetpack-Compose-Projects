package com.example.readerapp.screens.register.view

import android.graphics.Outline
import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.readerapp.components.EmailInput
import com.example.readerapp.components.PasswordInput
import com.example.readerapp.components.ReaderLogo
import com.example.readerapp.navigation.ReaderScreens
import com.example.readerapp.screens.register.viewModel.RegisterViewModel

@Composable
fun ReaderRegisterScreen(navController: NavController, viewModel: RegisterViewModel = viewModel()){
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ReaderLogo()
            UserForm(
                navController = navController,
                loading = false,
            ){
                    email, pass ->
                viewModel.createUserWithEmailAndPassword(email, pass){
                    navController.navigate(ReaderScreens.HomeNavigator.name)
                }
            }
        }
    }
}

@Composable
fun UserForm(
    navController: NavController,
    loading: Boolean = false,
    onDone: (String, String) -> Unit = {email, pwd -> }
){
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }

    val repeatPassword = rememberSaveable {
        mutableStateOf("")
    }

    val passwordVisibility = rememberSaveable {
        mutableStateOf(false)
    }

    val repeatPasswordVisibility = rememberSaveable {
        mutableStateOf(false)
    }

    val passwordFocusRequest = FocusRequester.Default
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }

    val isEmailValid = remember{
        mutableStateOf(true)
    }

    val isPasswordValid = remember {
        mutableStateOf(true)
    }

    val isRepeatPassValid = remember {
        mutableStateOf(true)
    }

    val modifier = Modifier
        .padding(15.dp)
        .background(MaterialTheme.colorScheme.background)
        .verticalScroll(rememberScrollState())

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailInput(
            emailState = email,
            enabled = !loading,
            onAction = KeyboardActions {
                passwordFocusRequest.requestFocus()
            },
            isError = !isEmailValid.value
        )

        PasswordInput(
            modifier = Modifier.focusRequester(passwordFocusRequest),
            passwordState = password,
            labelId = "Password",
            enabled = !loading,
            passwordVisibility = passwordVisibility,
            isError = !isPasswordValid.value,
            errorText = "Şifre en az 6 karakter olmalı"
        )

        PasswordInput(
            modifier = Modifier.focusRequester(passwordFocusRequest),
            passwordState = repeatPassword,
            labelId = "Repeat Password",
            enabled = !loading,
            passwordVisibility = repeatPasswordVisibility,
            isError = !isRepeatPassValid.value,
            errorText = "Şifreler uyuşmuyor"
        )

        Button (
            modifier = Modifier.fillMaxWidth()
                .padding(top =  5.dp, end = 3.dp, bottom = 3.dp, start = 3.dp),
            enabled = !loading && valid,
            shape = CircleShape,

            onClick = {
                isEmailValid.value = email.value.trim().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email.value).matches()
                isPasswordValid.value = password.value.length >= 6
                isRepeatPassValid.value = password.value.trim() == repeatPassword.value.trim()

                if(isRepeatPassValid.value){
                    if(isEmailValid.value && isPasswordValid.value)
                    {
                        onDone(email.value.trim(), password.value.trim())
                        keyboardController?.hide()
                    }
                }
            }
        ) {
            if (loading) CircularProgressIndicator(
                modifier = Modifier.size(25.dp)
            )
            else Text(text = "Register", modifier = Modifier.padding(5.dp))
        }

        Row(
            modifier = Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Do you have an account?", style = MaterialTheme.typography.labelLarge,
                color = Color.LightGray)
            TextButton(
                onClick = {
                    navController.navigate(ReaderScreens.LoginScreen.name)
                }
            ) {
                Text(text = "Login", style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold)
            }
        }

    }
}
