package com.bengisusahin.e_commerce.view.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bengisusahin.e_commerce.R
import com.bengisusahin.e_commerce.util.FieldValidation
import com.bengisusahin.e_commerce.util.FormState

// This is the login screen composable function that will be used to display the login screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    formState: FormState,
    username: String,
    password: String,
    rememberMe: Boolean,
    onLoginClick: (String, String, Boolean) -> Unit) {
    val usernameState = remember { mutableStateOf(TextFieldValue(username)) }
    val passwordState = remember { mutableStateOf(TextFieldValue(password)) }
    val rememberMeState = remember { mutableStateOf(rememberMe) }
    val passwordVisibility = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 140.dp, start = 15.dp, end = 15.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(60.dp))
        OutlinedTextField(
            value = usernameState.value,
            onValueChange = { usernameState.value = it },
            label = { Text("Username" , color = Color.Black ) },
            modifier = Modifier.fillMaxWidth(),
            isError = formState.usernameError !is FieldValidation.Success,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.DarkGray,
                unfocusedBorderColor = Color.Black,
                errorBorderColor = Color.Red
            )
        )
        if (formState.usernameError !is FieldValidation.Success) {
            Text(
                text = (formState.usernameError as FieldValidation.Error).message,
                color = Color.Red,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 16.dp)
            )
        }
        Spacer(modifier = Modifier.height(28.dp))
        OutlinedTextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = { Text("Password" , color = Color.Black ) },
            modifier = Modifier.fillMaxWidth(),
            isError = formState.passwordError !is FieldValidation.Success,
            visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisibility.value = !passwordVisibility.value }) {
                    Icon(
                        painter = painterResource(id = if (passwordVisibility.value) R.drawable.ic_unlocked else R.drawable.ic_locked),
                        contentDescription = "Toggle password visibility"
                    )
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.DarkGray,
                unfocusedBorderColor = Color.Black,
                errorBorderColor = Color.Red
            )
        )
        if (formState.passwordError !is FieldValidation.Success) {
            Text(
                text = (formState.passwordError as FieldValidation.Error).message,
                color = Color.Red,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 16.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(
                checked = rememberMeState.value,
                onCheckedChange = { rememberMeState.value = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.Green
                )
            )
            Text(text = "Remember Me")
        }

        Spacer(modifier = Modifier.height(28.dp))
        Button(
            onClick = {
                onLoginClick(usernameState.value.text, passwordState.value.text, rememberMeState.value)
            },
            colors = ButtonDefaults.buttonColors(Color(0xFF2196F3)),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login", color = Color.White)
        }
    }
}
