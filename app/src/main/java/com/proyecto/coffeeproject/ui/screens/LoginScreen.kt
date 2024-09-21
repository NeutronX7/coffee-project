package com.proyecto.coffeeproject.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.proyecto.coffeeproject.R
import com.proyecto.coffeeproject.viewmodel.AuthState
import com.proyecto.coffeeproject.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    modifier: Modifier,
    authViewModel: AuthViewModel
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(30.dp)
    ){
        LoginForm(
            navController = navController,
            modifier = modifier,
            authViewModel = authViewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginForm(
    navController: NavController,
    modifier: Modifier,
    authViewModel: AuthViewModel
) {
    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Authenticated -> navController.navigate("productive_unit_screen")
            is AuthState.Error -> Toast.makeText(context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Column( modifier ) {
        if( authState.value == AuthState.Loading ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(top = 16.dp)
                    .fillMaxWidth())
        }
        Row(
            modifier = modifier
                .fillMaxWidth()
                .align(CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(
                modifier = modifier
                    .align(Alignment.CenterVertically),
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                ElevatedCard (
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 10.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .size(width = 355.dp, height = 390.dp)
                ) {
                    Column(
                        modifier = modifier,
                        horizontalAlignment = CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "Bienvenido",
                            modifier = Modifier.padding(all = 16.dp)
                        )

                        OutlinedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                            },
                            Modifier.padding(),
                            label = { Text(stringResource(id = R.string.user)) }
                        )

                        Spacer(modifier = Modifier.padding(vertical = 16.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                            },
                            Modifier.padding(),
                            label = { Text(stringResource(id = R.string.password)) },
                            visualTransformation = PasswordVisualTransformation()
                        )
                    }
                }

                Spacer(modifier = Modifier.padding(vertical = 16.dp))

                OutlinedButton(
                    onClick = {
                        authViewModel.login(email,password)
                    },
                    enabled = authState.value != AuthState.Loading
                ) {
                    Text(stringResource(id = R.string.login), color = Color.Black)
                }
            }
        }
    }
}