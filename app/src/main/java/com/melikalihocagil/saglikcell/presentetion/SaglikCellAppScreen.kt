package com.melikalihocagil.saglikcell.presentetion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.melikalihocagil.saglikcell.presentetion.navigation.Screen
import com.melikalihocagil.saglikcell.presentetion.navigation.authGraph
import com.melikalihocagil.saglikcell.presentetion.navigation.featureGraph
import com.melikalihocagil.saglikcell.presentetion.navigation.mainGraph
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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Bottom Bar gösterilmeli mi? (Sadece mainGraph içindeki ana ekranlarda)
    val shouldShowBottomBar = remember(currentDestination) {
        Screen.bottomNavItems.any { currentDestination?.hasRoute(it::class) == true }
    }

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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (shouldShowBottomBar) {
                NavigationBar {
                    Screen.bottomNavItems.forEach { screen ->
                        val selected = currentDestination?.hierarchy?.any { it.hasRoute(screen::class) } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(screen) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                val icon = when (screen) {
                                    is Screen.Dashboard -> Icons.Default.Home
                                    is Screen.Profile -> Icons.Default.Person
                                    else -> Icons.Default.Home
                                }
                                Icon(icon, contentDescription = null)
                            },
                            label = {
                                val label = when (screen) {
                                    is Screen.Dashboard -> "Ana Sayfa"
                                    is Screen.Profile -> "Profil"
                                    else -> ""
                                }
                                Text(label)
                            }
                        )
                    }
                }
            }
        }
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
                authGraph(navController)
                mainGraph(navController)
                featureGraph(navController)
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
