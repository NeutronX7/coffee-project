package com.proyecto.coffeeproject.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalContext
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.coffeeproject.R
import com.proyecto.coffeeproject.viewmodel.AuthState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material.icons.filled.DateRange as DateRange1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LotsFormScreen(
    navController: NavController,
    modifier: Modifier,
    name: String
) {

    var user by remember {
        mutableStateOf("")
    }

    var area by remember {
        mutableStateOf("")
    }

    var date by remember {
        mutableStateOf("")
    }

    var groove by remember {
        mutableStateOf("")
    }

    var lotName by remember {
        mutableStateOf("")
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
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
                    .padding()
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = stringResource(id = R.string.fill_info))

                Text( text = "Unidad productiva: $name", fontWeight = FontWeight.Bold )

                OutlinedTextField(
                    value = lotName,
                    onValueChange = {
                        lotName = it
                    },
                    Modifier.padding(),
                    label = {
                        Text(stringResource(id = R.string.lot_name))
                    }
                )

                OutlinedTextField(
                    value = user,
                    onValueChange = {
                        user = it
                    },
                    Modifier.padding(),
                    label = {
                        Text(stringResource(id = R.string.user))
                    }
                )

                OutlinedTextField(
                    value = area,
                    onValueChange = {
                        area = it
                    },
                    Modifier.padding(),
                    label = {
                        Text(stringResource(id = R.string.area))
                    }
                )

                OutlinedTextField(
                    value = groove,
                    onValueChange = {
                        groove = it
                    },
                    Modifier.padding(),
                    label = {
                        Text(stringResource(id = R.string.groove))
                    }
                )

                OutlinedTextField(
                    value = date,
                    onValueChange = {
                        date = it
                    },
                    Modifier.padding(),
                    label = {
                        Text(stringResource(id = R.string.date))
                    }
                )

                Box(

                ) {
                    OutlinedButton(
                        modifier = Modifier
                            .padding(vertical = 16.dp),
                        onClick = {
                            //createProductiveUnit(name, user)
                            //user = ""
                            //name = ""
                            createLotForProductiveUnit(name, lotName, user, area, groove, date)
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

@SuppressLint("ShowToast")
fun createLotForProductiveUnit(
    productiveUnitName: String,
    lotName: String,
    user: String,
    area: String,
    groove: String,
    date: String,
) {
    val db = FirebaseFirestore.getInstance()

    db.collection("productive_unit")
        .whereEqualTo("name", productiveUnitName)
        .get()
        .addOnSuccessListener { documents ->
            if (!documents.isEmpty) {
                val productiveUnitDoc = documents.first()  // Assuming one document per productive unit name

                val lotData = hashMapOf(
                    "lotName" to lotName,
                    "user" to user,
                    "area" to area.toDouble(),
                    "groove" to groove.toDouble(),
                    "date" to date
                )

                db.collection("productive_unit")
                    .document(productiveUnitDoc.id)
                    .collection("lots")
                    .add(lotData)
                    .addOnSuccessListener {
                        Log.w("Firestore", "Added")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firestore", "Error adding lot", e)
                    }
            } else {
                Log.w("Firestore", "No productive unit found with that name")
            }
        }
        .addOnFailureListener { exception ->
            Log.w("Firestore", "Error getting documents: ", exception)
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDocked() {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedDate,
            onValueChange = { },
            label = { Text("Fecha de floración") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange1,
                        contentDescription = "Select date"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )

        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.TopStart
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 64.dp)
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )
                }
            }
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}