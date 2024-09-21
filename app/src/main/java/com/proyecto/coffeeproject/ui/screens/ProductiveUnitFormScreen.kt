package com.proyecto.coffeeproject.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.coffeeproject.R

@Composable
fun ProductiveUnitFormScreen(
    navController: NavController,
    modifier: Modifier
) {
    ProductiveUnitForm(modifier = modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductiveUnitForm(
    modifier: Modifier
) {

    var name by remember {
        mutableStateOf("")
    }

    var user by remember {
        mutableStateOf("")
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.productive_units), color = White)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = White,
                ),
            )

            Column(
                modifier = Modifier
                    .padding(top = 16.dp )
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = stringResource(id = R.string.fill_info))

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    Modifier.padding(),
                    label = { Text(stringResource(id = R.string.productive_unit_name)) }
                )

                OutlinedTextField(
                    value = user,
                    onValueChange = {
                        user = it
                    },
                    Modifier.padding(),
                    label = { Text(stringResource(id = R.string.user)) }
                )

                Box(

                ) {
                    OutlinedButton(
                        modifier = Modifier
                            .padding(vertical = 16.dp),
                        onClick = {
                            createProductiveUnit(name, user)
                            user = ""
                            name = ""
                        },
                        //enabled = authState.value != AuthState.Loading
                    ) {
                        Text(stringResource(id = R.string.create), color = Color.Black)
                    }
                }
            }
        }
    }
}

private fun createProductiveUnit(name: String, user: String) {
    val db = FirebaseFirestore.getInstance()
    val productiveUnit = hashMapOf(
        "name" to name,
        "user" to user,
        "lots" to emptyList<String>()  // Initially no lots
    )

    db.collection("productive_unit")
        .add(productiveUnit)
        .addOnSuccessListener { documentReference ->
            Log.d("Firestore", "Productive unit added with ID: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error adding productive unit", e)
        }
}