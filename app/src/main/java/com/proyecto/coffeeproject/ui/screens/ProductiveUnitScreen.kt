package com.proyecto.coffeeproject.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.coffeeproject.R
import com.proyecto.coffeeproject.model.productive_unit.Lot
import com.proyecto.coffeeproject.model.productive_unit.ProductiveUnit
import com.proyecto.coffeeproject.viewmodel.AuthState
import com.proyecto.coffeeproject.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductiveUnitScreen(
    navController: NavController,
    modifier: Modifier,
    authViewModel: AuthViewModel
) {
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Unauthenticated -> navController.navigate("login_screen")
            else -> Unit
        }
    }

    Column {
        TopAppBar(
            title = {
                Text(text = stringResource(id = R.string.productive_units), color = White)
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = White,
            ),
            actions = {
                IconButton(onClick = { authViewModel.signOut() }) {
                    Icon(Icons.Filled.ExitToApp, null, tint = White)
                }
            },
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            ProductiveUnitList(
                navController = navController
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(30.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        ExtendedFloatingActionButton(
            onClick = {
                navController.navigate("productive_unit_form_screen")
            },
            //modifier = Modifier.padding(12.dp)
        ) {
            Icon(Icons.Filled.Add, "Floating action button.")
            Text(text = "Añadir UP")
        }
    }
}

@Composable
fun ProductiveUnitList(
    navController: NavController
) {
    val productiveUnits = remember { mutableStateListOf<ProductiveUnit>() }

    LaunchedEffect(Unit) {
        val db = FirebaseFirestore.getInstance()

        db.collection("productive_unit")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val id = document.id  // Get the document ID
                    val name = document.getString("name") ?: ""
                    val user = document.getString("user") ?: ""
                    val lots = document.get("lots") as List<Lot>? ?: emptyList()

                    // Pass the id along with other fields to the ProductiveUnit object
                    productiveUnits.add(ProductiveUnit(id, name, user, lots))
                }
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting documents: ", exception)
            }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(productiveUnits) { productiveUnit ->
            ProductiveUnitItem(
                productiveUnit,
                navController,
                productiveUnit.name,
            )
        }
    }
}

@Composable
fun ProductiveUnitItem(
    productiveUnit: ProductiveUnit,
    navController: NavController,
    name: String
) {

    ElevatedCard (
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .padding(top = 16.dp)
            .size(width = 355.dp, height = 100.dp)
            .clickable {
                navController.navigate("lots_screen/${name}")
            }
    ) {
        Column (
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp)
        ) {

            Text(
                text = "Unidad productiva: ${productiveUnit.name}",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
            )

            Text(
                text = "Usuario: ${productiveUnit.user}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp),
                fontSize = 16.sp,
            )

            Column (
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {

                Row {
                    IconButton(
                        onClick = {
                            navController.navigate("edit_productive_unit/${productiveUnit.id}/${productiveUnit.user}/${productiveUnit.name}")
                            /*updateProductiveUnit(
                                productiveUnit.id,
                                productiveUnit.name,
                                productiveUnit.user
                            )*/
                        },
                        modifier = Modifier
                            .padding()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                        )
                    }

                    IconButton(
                        onClick = { deleteProductiveUnit(productiveUnit.id)  },
                        modifier = Modifier
                            .padding()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                        )
                    }
                }
            }
        }
    }

}

fun updateProductiveUnit(id: String, updatedName: String, updatedUser: String) {
    val db = FirebaseFirestore.getInstance()

    db.collection("productive_units")
        .document(id)
        .update("name", updatedName, "user", updatedUser)
        .addOnSuccessListener {
            Log.d("Firestore", "Productive unit updated successfully")
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error updating productive unit", e)
        }
}

fun deleteProductiveUnit(id: String) {
    val db = FirebaseFirestore.getInstance()

    db.collection("productive_unit")
        .document(id)
        .delete()
        .addOnSuccessListener {
            Log.d("Firestore", "Productive unit deleted successfully")
        }
        .addOnFailureListener { e ->
            Log.w("Firestore", "Error deleting productive unit", e)
        }
}