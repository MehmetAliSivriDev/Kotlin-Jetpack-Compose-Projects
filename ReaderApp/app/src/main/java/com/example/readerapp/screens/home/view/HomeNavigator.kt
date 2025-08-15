package com.example.readerapp.screens.home.view

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.readerapp.components.FABHome
import com.example.readerapp.navigation.NavItem
import com.example.readerapp.navigation.ReaderScreens
import com.example.readerapp.screens.profile.view.ProfileScreen

@Composable
fun HomeNavigator(navController: NavController) {
    val items: List<NavItem> = listOf(
        NavItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unSelectedIcon = Icons.Outlined.Home
        ),
        NavItem(
            title = "Profile",
            selectedIcon = Icons.Filled.Person,
            unSelectedIcon = Icons.Outlined.Person
        ),
    )

    var selectedIndex by remember{
        mutableStateOf(0)
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                        },
                        icon = {
                            if(selectedIndex == index) Icon(imageVector = navItem.selectedIcon , contentDescription = "")
                            else Icon(imageVector = navItem.unSelectedIcon, contentDescription = "")
                        },
                        label = {
                            Text(text = navItem.title)
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            when(selectedIndex){
                0 -> FABHome {
                    navController.navigate(ReaderScreens.SearchScreen.name)
                }
            }
        },
        content = { innerPadding ->
            when(selectedIndex){
                0 -> ReaderHomeScreen(modifier = Modifier.padding(innerPadding) , navController = navController)
                1 -> ProfileScreen(modifier = Modifier.padding(innerPadding), navController = navController)
            }
        }
    )
}

