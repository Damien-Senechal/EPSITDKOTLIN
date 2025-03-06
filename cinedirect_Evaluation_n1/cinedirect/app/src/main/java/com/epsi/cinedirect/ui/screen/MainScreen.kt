package com.epsi.cinedirect.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.epsi.cinedirect.MainViewModel
import com.epsi.cinedirect.ui.theme.CinedirectTheme

@Composable
fun MainScreen(modifier: Modifier = Modifier,viewModel: MainViewModel) {

    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Admin")
    val selectedIcons =
        listOf(Icons.Filled.Home, Icons.Filled.Settings)
    val unselectedIcons =
        listOf(Icons.Outlined.Home, Icons.Outlined.Settings)
    CinedirectTheme {
        val navController = rememberNavController()
        Scaffold(modifier = Modifier.fillMaxSize(),
                 bottomBar = {
                     NavigationBar(
                         modifier = Modifier.fillMaxWidth(),
                         containerColor = MaterialTheme.colorScheme.surfaceContainerLow.copy(
                             alpha = 0.1f
                         )
                     ) {
                         items.forEachIndexed { index, item ->
                             NavigationBarItem(
                                 icon = {
                                     Icon(
                                         if (selectedItem == index) selectedIcons[index] else unselectedIcons[index],
                                         contentDescription = item
                                     )
                                 },
                                 label = { Text(item) },
                                 selected = selectedItem == index,
                                 onClick = { selectedItem = index }
                             )
                         }

                     }
                 }) { innerPadding ->
            LaunchedEffect(selectedItem) {
                when (selectedItem) {
                    0 -> navController.navigate("client")
                    else -> navController.navigate("admin")
                }
            }
            NavHost(
                modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                navController = navController,
                startDestination = "home",
            ) {
                navigation(
                    startDestination = "client",
                    route = "home"
                ) {
                    composable("admin") {
                        AdminBoard(
                            modifier = Modifier.fillMaxSize(), state = viewModel.adminState,
                            viewModel = viewModel
                        )
                    }
                    composable("client") {
                        HomeBoard(
                            modifier = Modifier.fillMaxSize(), state = viewModel.homeState,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}