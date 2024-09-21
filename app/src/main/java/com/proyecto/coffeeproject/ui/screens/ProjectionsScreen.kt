package com.proyecto.coffeeproject.ui.screens

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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.proyecto.coffeeproject.R
import com.proyecto.coffeeproject.viewmodel.AuthState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectionsScreen(
    navComposable: NavController,
    modifier: Modifier,
    lotName: String
) {
    var first by remember {
        mutableStateOf(false)
    }

    var second by remember {
        mutableStateOf(false)
    }

    var third by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
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
                .padding(16.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "LOTE")

            if(first) {
                Text(
                    text = stringResource(id = R.string.productive_branches_desc),
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().onFocusChanged {
                    if( it.isFocused ) {
                        first = true
                    } else {
                        first = false
                    }
                },
                label = { Text(stringResource(id = R.string.productive_branches)) },
                value = "",
                onValueChange = {}
            )

            if(second) {
                Text(
                    text = stringResource(id = R.string.knots_average_desc),
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth()
                    .onFocusChanged {
                        if( it.isFocused ) {
                            second = true
                        } else {
                            second = false
                        }
                    },
                label = { Text(stringResource(id = R.string.knots_average)) },
                value = "",
                onValueChange = {}
            )

            if(third) {
                Text(
                    text = stringResource(id = R.string.fruit_average_by_knot_desc),
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth()
                    .onFocusChanged {
                        if( it.isFocused ) {
                            third = true
                        } else {
                            third = false
                        }
                    },
                label = { Text(stringResource(id = R.string.fruit_average_by_knot)) },
                value = "",
                onValueChange = {}
            )

            OutlinedButton(
                modifier = Modifier.padding(top = 16.dp),
                onClick = {

                },
                //enabled = authState.value != AuthState.Loading
            ) {
                Text(stringResource(id = R.string.next), color = Color.Black)
            }
        }
    }

}