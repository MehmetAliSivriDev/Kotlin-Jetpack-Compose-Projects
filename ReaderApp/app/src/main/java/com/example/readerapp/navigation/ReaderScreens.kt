package com.example.readerapp.navigation

enum class ReaderScreens {
    SplashScreen,
    LoginScreen,
    RegisterScreen,
    HomeScreen,
    HomeNavigator,
    ProfileScreen,
    SearchScreen,
    DetailScreen,
    UpdateScreen,
    StatsScreen;

    companion object {
        fun fromRoute(route: String?): ReaderScreens =
            when(route?.substringBefore("/")){
                SplashScreen.name -> SplashScreen
                LoginScreen.name -> LoginScreen
                RegisterScreen.name -> RegisterScreen
                HomeScreen.name -> HomeScreen
                HomeNavigator.name -> HomeNavigator
                ProfileScreen.name -> ProfileScreen
                SearchScreen.name -> SearchScreen
                DetailScreen.name -> DetailScreen
                UpdateScreen.name -> UpdateScreen
                StatsScreen.name -> StatsScreen
                null -> HomeScreen
                else -> throw IllegalArgumentException("Route $route is not recognized")
            }
    }
}