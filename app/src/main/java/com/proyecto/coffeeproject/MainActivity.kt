package com.proyecto.coffeeproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.proyecto.coffeeproject.ui.screens.EditProductiveUnitScreen
import com.proyecto.coffeeproject.ui.screens.LoginScreen
import com.proyecto.coffeeproject.ui.screens.LotsFormScreen
import com.proyecto.coffeeproject.ui.screens.LotsScreen
import com.proyecto.coffeeproject.ui.screens.ProductiveUnitFormScreen
import com.proyecto.coffeeproject.ui.screens.ProductiveUnitScreen
import com.proyecto.coffeeproject.ui.screens.ProjectionsScreen
import com.proyecto.coffeeproject.ui.theme.CoffeeProjectTheme
import com.proyecto.coffeeproject.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authViewModel : AuthViewModel by viewModels()
        enableEdgeToEdge()
        setContent {
            CoffeeProjectTheme {
                val navController = rememberNavController()
                SetupNavGraph(
                    navController = navController,
                    authViewModel = authViewModel
                )
            }
        }
    }
}

@Composable
fun SetupNavGraph(navController: NavHostController, authViewModel: AuthViewModel) {
    NavHost(navController = navController, startDestination = "login_screen") {
        composable("login_screen") {
            LoginScreen(
                navController = navController,
                modifier = Modifier
                    .fillMaxSize(),
                authViewModel = authViewModel
            )
        }

        composable("productive_unit_screen") {
            ProductiveUnitScreen(
                navController = navController,
                modifier = Modifier
                    .fillMaxSize(),
                authViewModel = authViewModel
            )
        }

        composable("productive_unit_form_screen") {
            ProductiveUnitFormScreen(
                navController = navController,
                modifier = Modifier
                    .fillMaxSize(),
            )
        }

        composable("edit_productive_unit/{id}/{name}/{productive_unit}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                }
            )
        ) {

            val id = remember {
                it.arguments?.getString("id")
            }

            val name = remember {
                it.arguments?.getString("name")
            }

            val productiveUnit = remember {
                it.arguments?.getString("productive_unit")
            }


            EditProductiveUnitScreen(
                navController = navController,
                id = id,
                name = name,
                productiveUnit = productiveUnit,
                modifier = Modifier
                    .fillMaxSize(),
            )
        }

        composable("lots_screen/{name}",
            arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                }
            )
        ) {

            val name = remember {
                it.arguments?.getString("name")
            }

            name?.let { it1 ->
                LotsScreen(
                    navController = navController,
                    modifier = Modifier
                        .fillMaxSize(),
                    name = it1
                )
            }
        }

        composable("lots_form_screen/{name}",
            arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                }
            )
        ) {

            val name = remember {
                it.arguments?.getString("name")
            }

            name?.let { it1 ->
                LotsFormScreen(
                    navController = navController,
                    modifier = Modifier
                        .fillMaxSize(),
                    name = it1
                )
            }
        }

        composable("lot_projection_screen/{name}",
            arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                }
            )
        ) {

            val name = remember {
                it.arguments?.getString("name")
            }

            name?.let { it1 ->
                ProjectionsScreen(
                    navController,
                    modifier = Modifier
                        .fillMaxSize(),
                    name
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CoffeeProjectTheme {

    }
}