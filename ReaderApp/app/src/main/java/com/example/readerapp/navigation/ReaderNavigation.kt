package com.example.readerapp.navigation

import android.provider.ContactsContract.Profile
import android.window.SplashScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.readerapp.screens.detail.view.ReaderBookDetailsScreen
import com.example.readerapp.screens.home.view.HomeNavigator
import com.example.readerapp.screens.home.view.ReaderHomeScreen
import com.example.readerapp.screens.home.viewModel.HomeViewModel
import com.example.readerapp.screens.login.view.ReaderLoginScreen
import com.example.readerapp.screens.profile.view.ProfileScreen
import com.example.readerapp.screens.register.view.ReaderRegisterScreen
import com.example.readerapp.screens.search.view.ReaderBookSearchScreen
import com.example.readerapp.screens.search.viewModel.BookSearchViewModel
import com.example.readerapp.screens.splash.view.ReaderSplashScreen
import com.example.readerapp.screens.update.view.ReaderBookUpdateScreen

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ReaderNavigation(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ReaderScreens.SplashScreen.name
    ){
        composable(ReaderScreens.SplashScreen.name) {
            ReaderSplashScreen(navController = navController)
        }

        composable(ReaderScreens.LoginScreen.name) {
            ReaderLoginScreen(navController = navController)
        }

        composable(ReaderScreens.RegisterScreen.name) {
            ReaderRegisterScreen(navController = navController)
        }

        composable(ReaderScreens.HomeScreen.name) {
            val homeViewModel = hiltViewModel<HomeViewModel>()

            ReaderHomeScreen(navController = navController, viewModel = homeViewModel)
        }

        composable(ReaderScreens.ProfileScreen.name) {
            val viewModel = hiltViewModel<HomeViewModel>()

            ProfileScreen(navController = navController, viewModel = viewModel)
        }

        composable(ReaderScreens.HomeNavigator.name) {
            HomeNavigator(navController =  navController)
        }

        composable(ReaderScreens.SearchScreen.name) {
            val viewModel = hiltViewModel<BookSearchViewModel>()

            ReaderBookSearchScreen(navController =  navController, viewModel = viewModel)
        }

        val detailName = ReaderScreens.DetailScreen.name
        composable("$detailName/{bookId}", arguments = listOf(
            navArgument("bookId"){
                type = NavType.StringType
            }
        )) { backStackEntry ->
            backStackEntry.arguments?.getString("bookId").let{
                ReaderBookDetailsScreen(navController =  navController, bookId = it.toString())
            }
        }

        val updateName = ReaderScreens.UpdateScreen.name
        composable("$updateName/{bookItemId}", arguments = listOf(
            navArgument("bookItemId"){
                type = NavType.StringType
            }
        )) { backStackEntry ->
            backStackEntry.arguments?.getString("bookItemId").let{
                ReaderBookUpdateScreen(navController =  navController, bookItemId = it.toString())
            }
        }
    }
}