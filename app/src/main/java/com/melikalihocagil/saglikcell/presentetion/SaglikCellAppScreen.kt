package com.melikalihocagil.saglikcell.presentetion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.melikalihocagil.saglikcell.presentetion.navigation.Screen
import com.melikalihocagil.saglikcell.presentetion.screens.AnalyticsScreen
import com.melikalihocagil.saglikcell.presentetion.screens.AuthScreen
import com.melikalihocagil.saglikcell.presentetion.screens.DashboardScreen
import com.melikalihocagil.saglikcell.presentetion.screens.MetricEntryScreen
import com.melikalihocagil.saglikcell.presentetion.screens.PremiumScreen
import org.koin.androidx.compose.koinViewModel

/**
 * Uygulamanın ana ekran yapısı.
 * Merkezi Snackbar yönetimi, Global Loading ve Navigasyon barındırır.
 */
@Composable
fun SaglikCellAppScreen(
    viewModel: SaglikCellAppViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()

    // Tek seferlik olayları (Effect) dinleme
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SaglikCellAppEffect.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is SaglikCellAppEffect.NavigateToLogin -> {
                    navController.navigate(Screen.Login) {
                        popUpTo(0) // Tüm backstack'i temizle
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Ana İçerik ve NavHost yönetimi
            NavHost(
                navController = navController,
                startDestination = Screen.Login
            ) {
                composable<Screen.Login> {
                    AuthScreen(
                        onLoginSuccess = {
                            navController.navigate(Screen.Dashboard) {
                                popUpTo(Screen.Login) { inclusive = true }
                            }
                        }
                    )
                }
                composable<Screen.Dashboard> {
                    DashboardScreen(
                        onNavigateToEntry = { navController.navigate(Screen.MetricEntry) },
                        onNavigateToAnalytics = { navController.navigate(Screen.Analytics) },
                        onNavigateToPremium = { navController.navigate(Screen.Premium) }
                    )
                }
                composable<Screen.Profile> {
                    // ProfileScreen(...) yer tutucu
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        androidx.compose.material3.Text("Profile Screen")
                    }
                }
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

            // Global Loading Overlay
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .pointerInput(Unit) {},
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}
