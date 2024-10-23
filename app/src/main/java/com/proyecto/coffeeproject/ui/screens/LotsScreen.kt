package com.proyecto.coffeeproject.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LotsScreen(
    navController: NavController,
    modifier: Modifier,
    name: String
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ){
        Column {

            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.lots), color = White)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = White,
                ),
            )

            Column(
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.lots_of_pu),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )

                Text(
                    text = name,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp
                )
            }
            
            RenderLots(navController, name = name)
        }
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(30.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = {
                    navController.navigate("lots_form_screen/${name}")
                },
                //modifier = Modifier.padding(12.dp)
            ) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        }
    }
}

@Composable
fun RenderLots(
    navController: NavController,
    name: String
) {
    var lots by remember { mutableStateOf<List<Lot>>(emptyList()) }

    LaunchedEffect(name) {
        fetchLotsForProductiveUnit(name) { fetchedLots ->
            lots = fetchedLots
        }
    }

    if (lots.isEmpty()) {
        Text(text = "No hay lotes")
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(lots) { lot ->
                LotItem(
                    navController = navController,
                    lot
                )
            }
        }
    }
}

@Composable
fun LotItem(
    navController: NavController,
    lot: Lot
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
            .size(width = 375.dp, height = 100.dp)
            .clickable {
                navController.navigate("lot_projection_screen/test")
            }
    ) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.weight(3f)
                ) {
                    Text(text = "Lot Name: ${lot.lotName}", fontWeight = FontWeight.Bold)
                    Text(text = "User: ${lot.user}")
                    Text(text = "Area: ${lot.area}")
                    Text(text = "Groove: ${lot.groove}")
                    Text(text = "Date: ${lot.date}")
                }

                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .padding()
                        .weight(1f)
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

fun fetchLotsForProductiveUnit(name: String, onLotsFetched: (List<Lot>) -> Unit) {
    val db = FirebaseFirestore.getInstance()

    db.collection("productive_unit")
        .whereEqualTo("name", name)
        .get()
        .addOnSuccessListener { documents ->
            if (!documents.isEmpty) {
                val productiveUnitDoc = documents.first()

                db.collection("productive_unit")
                    .document(productiveUnitDoc.id)
                    .collection("lots")
                    .get()
                    .addOnSuccessListener { lotsSnapshot ->
                        val lotsList = lotsSnapshot.map { doc ->
                            Lot(
                                lotName = doc.getString("lotName") ?: "",
                                user = doc.getString("user") ?: "",
                                area = doc.getDouble("area") ?: 0.0,
                                groove = doc.getDouble("groove") ?: 0.0,
                                date = doc.getString("date") ?: ""
                            )
                        }
                        onLotsFetched(lotsList)
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firestore", "Error", e)
                    }
            }
        }
        .addOnFailureListener { exception ->
            Log.w("Firestore", "Error getting productive unit: ", exception)
        }
}