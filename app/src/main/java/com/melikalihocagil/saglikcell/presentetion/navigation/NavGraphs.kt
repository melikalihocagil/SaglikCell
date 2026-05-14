package com.melikalihocagil.saglikcell.presentetion.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.melikalihocagil.saglikcell.presentetion.screens.AnalyticsScreen
import com.melikalihocagil.saglikcell.presentetion.screens.auth.AuthScreen
import com.melikalihocagil.saglikcell.presentetion.screens.auth.OtpScreen
import com.melikalihocagil.saglikcell.presentetion.screens.DashboardScreen
import com.melikalihocagil.saglikcell.presentetion.screens.MetricEntryScreen
import com.melikalihocagil.saglikcell.presentetion.screens.PremiumScreen
import androidx.navigation.toRoute

/**
 * Kimlik doğrulama akışını içeren navigasyon grafiği.
 */
fun NavGraphBuilder.authGraph(navController: NavController) {
    composable<Screen.Login> {
        AuthScreen(
            onNavigateToOtp = { phoneNumber ->
                navController.navigate(Screen.Otp(phoneNumber))
            }
        )
    }

    composable<Screen.Otp> { backStackEntry ->
        val otpRoute: Screen.Otp = backStackEntry.toRoute()
        OtpScreen(
            phoneNumber = otpRoute.phoneNumber,
            onVerifySuccess = {
                navController.navigate(Screen.Dashboard) {
                    popUpTo(Screen.Login) { inclusive = true }
                }
            }
        )
    }
}

/**
 * Uygulamanın ana ekranlarını içeren navigasyon grafiği.
 */
fun NavGraphBuilder.mainGraph(navController: NavController) {
    composable<Screen.Dashboard> {
        DashboardScreen(
            onNavigateToEntry = { navController.navigate(Screen.MetricEntry) },
            onNavigateToAnalytics = { navController.navigate(Screen.Analytics) },
            onNavigateToPremium = { navController.navigate(Screen.Premium) }
        )
    }
    
    composable<Screen.Profile> {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            androidx.compose.material3.Text("Profile Screen")
        }
    }
}

/**
 * Uygulamanın yan özelliklerini (Analytics, Premium vb.) içeren navigasyon grafiği.
 */
fun NavGraphBuilder.featureGraph(navController: NavController) {
    composable<Screen.MetricEntry> {
        MetricEntryScreen(onBack = { navController.popBackStack() })
    }
    
    composable<Screen.Analytics> {
        AnalyticsScreen(onBack = { navController.popBackStack() })
    }
    
    composable<Screen.Premium> {
        PremiumScreen(onSuccess = { navController.popBackStack() })
    }
}
